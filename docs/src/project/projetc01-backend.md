---
title: 学生管理系统-后端
order: 3
icon: /project-02.svg
category:
    - PROJECT
    - MDi在
---

## 配置
### MyBatis-Plus

```java
/**
 * MP 配置类
 *
 * @author caobaoqi
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * MP 分页拦截器
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

### Web-MVC 

```java
/**
 * WebMVC 配置类
 *
 * @author caobaoqi
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Resource
    private LoginInterceptor loginInterceptor;

    /**
     * 跨域
     *
     * @param registry registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    /**
     * 登录拦截器
     *
     * @param registry registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/admin/add")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "v2/**", "swagger-ui.html/**")
                .excludePathPatterns("/logout");
    }
}
```

### Redis

```java
/**
 * Redis 配置类
 *
 * @author caobaoqi
 */
@Configuration
public class RedisConfig {
    @Resource
    private RedisConnectionFactory factory;

    /**
     * RedisTemplate
     *
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        redisTemplate.setValueSerializer(serializer);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getDefault());
        JsonMapper.builder()
                .configure(MapperFeature.USE_ANNOTATIONS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        serializer.setObjectMapper(objectMapper);
        return redisTemplate;
    }
}
```

### Swagger

```java
/**
 * Swagger2 配置类
 *
 * @author caobaoqi
 */
@Configuration
public class SwaggerConfig {

    /**
     * docket
     *
     * @return Docket
     */
    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("MySQL-Homework")
                .version("V1.0.0")
                .description("MySQL-Homework 曹宝琪|曹蓓|程柄惠|焦惠静")
                .contact(new Contact("晋中学院-曹宝琪", "https://gitee.com/cola777jz", "1203952894@qq.com"))
                .license("Apache 2.0")
                .build();
        docket = docket.apiInfo(apiInfo);
        return docket;
    }

}
```

### ThreadPool
```java
/**
 * 线程池配置类
 *
 * @author caobaoqi
 */
@Configuration
public class ThreadPoolConfig implements AsyncConfigurer {

    /**
     * 现场管理
     *
     * @return Executor
     */
    @Bean("taskExecutor")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(Integer.MAX_VALUE);

        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("MySQL-Homework");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();

        return executor;
    }
}
```

## 工具类

### JWTUtils

```java
/**
 * JWT 工具类
 *
 * @author caobaoqi
 */
@Component
public class JWTUtils {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${cbq.jwt-secret}")
    private static final String JWT_SECRET = "CaoBaoQi";

    /**
     * 成绩 token
     * @param userId userId
     * @return token
     */
    public static String createToken(String userId) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        return jwtBuilder.compact();
    }

    /**
     *  解析 token
     * @param token token
     * @return Object
     */
    public static Object getUserIdByToken(String token) {
        Map<String, Object> body = (Map<String, Object>) Jwts.parser().setSigningKey(JWT_SECRET).parse(token).getBody();
        return body.get("userId");
    }

    /**
     * 通过 token 获取 User
     * @param token token
     * @return User
     */
    public Object getUserByToken(String token) {
        Object userIdByToken = getUserIdByToken(token);
        if (null == userIdByToken) {
            return null;
        }
        String userJson = redisTemplate.opsForValue().get(token);
        if (null == userJson) {
            return null;
        }
        return JSON.parseObject(userJson, Object.class);
    }
}
```

### LoginInterceptro

```java
/**
 * 登录拦截器
 *
 * @author caobaoqi
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private JWTUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
         * 1.需要判断请求的接口路径是否为 HandlerMethod（controller方法）
         * 2.判断 token 是否为空，如果为空，未登录
         * 3.如 果token不 为空，登录验证 loginService checkToken
         * 4.如果认证成功，放行即可
         */
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader("Authorization");
        log.info("==============request start========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}", requestURI);
        log.info("request method:{}", request.getMethod());
        log.info("token:{}", token);
        log.info("==============request end==========================");

        if (StringUtils.isBlank(token)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(Result.fail("请登录后再访问")));
            return false;
        }
        Object user = null;
        try {
            user = jwtUtils.getUserByToken(token);
        } catch (Exception e) {
            response.getWriter().print(JSON.toJSONString(Result.fail("请登录后再访问")));
        }
        if (null == user) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(Result.fail("请登录后再访问")));
            return false;
        }

        UserThreadLocal.put(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 删除 ThreadLocal 中用完的信息，避免出现内泄露的风险
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @param ex       ex
     * @throws Exception Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
```

### UserThreadLocal

```java
/**
 * 用户线程
 *
 * @author caobaoqi
 */
public class UserThreadLocal {
    private UserThreadLocal() {
    }

    private static final ThreadLocal<Object> LOCAL = new ThreadLocal<>();

    public static void put(Object user) {
        LOCAL.set(user);
    }

    public static Object get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}
```

### Result 实体集

```java
/**
 * Result 返回操作类
 *
 * @param <T>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {
    /**
     * 状态码
     */
    private String code;
    /**
     * 数据
     */
    private T data;
    /**
     * msg
     */
    private String msg;

    public static <T> Result<T> success() {
        return new Result<>("20000", null, "success");
    }

    public static <T> Result<T> success(T data) {
        return new Result<>("20000", data, "success");
    }

    public static <T> Result<T> success(T data, String msg) {
        return new Result<>("20000", data, msg);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>("20000", null, msg);
    }

    public static <T> Result<T> fail() {
        return new Result<>("20001", null, "fail");
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>("20001", null, msg);
    }
}
```

## 专业模块 Marjo

### Service

```java
/**
 * IMajorService
 *
 * @author caobaoqi
 */
public interface IMajorService extends IService<Major> {

    /**
     * 获取下一个专业 id
     * @return id
     */
    String findNextMajorId();

}
```

```java
/**
 * MajorServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements IMajorService {
    @Resource
    private MajorMapper majorMapper;

    @Override
    public String findNextMajorId() {
        String maxMajorId = majorMapper.findMaxMajorId();
        if (null != maxMajorId) {
            return String.format("%02d", Integer.valueOf(maxMajorId) + 1);
        }
        return "01";
    }
}
```

### Mapper

```java
/**
 * MajorMapper
 *
 * @author caobaoqi
 */
public interface MajorMapper extends BaseMapper<Major> {
    @Select("select major_id from t_major order by major_id desc limit 1")
    String findMaxMajorId();
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="jz.cbq.backend.mapper.MajorMapper">

</mapper>

```

### Controller

```java
/**
 * MajorController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/major")
public class MajorController {
    @Resource
    private IMajorService majorService;

    /**
     * 添加专业
     *
     * @param major major
     * @return INFO
     */
    @PostMapping("/add")
    public Result<String> addMajor(@RequestBody Major major) {
        Major one = majorService.getOne(new LambdaQueryWrapper<Major>().eq(Major::getMajorName, major.getMajorName()).last("LIMIT 1"));
        if (one != null) {
            return Result.fail("该专业已存在");
        }
        String nextMajorId = majorService.findNextMajorId();
        major.setMajorId(nextMajorId);
        major.setCreateTime(new Date());
        major.setClassTotal(0);
        major.setCourseTotal(0);
        major.setStuTotal(0);
        major.setTeaTotal(0);
        boolean save = majorService.save(major);

        return save ? Result.success("添加专业成功") : Result.fail("添加专业失败");
    }

    /**
     * 通过 id 获取专业
     *
     * @param majorId majorId
     * @return Major
     */
    @GetMapping("getByMajorId")
    public Result<Major> getByMajorId(String majorId) {
        Major major = majorService.getOne(new LambdaQueryWrapper<Major>().eq(Major::getMajorId, majorId).last("LIMIT 1"));

        return major != null ? Result.success(major) : Result.fail("获取专业失败");
    }

    /**
     * 编辑专业
     *
     * @param major major
     * @return INFO
     */
    @PutMapping("/edit")
    public Result<String> editMajor(@RequestBody Major major) {
        boolean update = majorService.update(new LambdaUpdateWrapper<Major>().set(Major::getMajorName, major.getMajorName()).eq(Major::getMajorId, major.getMajorId()));

        return update ? Result.success("编辑专业成功") : Result.fail("编辑专业失败");
    }

    /**
     * 条件分页查询专业信息
     *
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @param majorName majorName
     * @return Map<String, Object>
     */
    @GetMapping("/majorPageList")
    public Result<Map<String, Object>> majorPageList(int pageNum, int pageSize, @RequestParam(value = "majorName", required = false) String majorName) {
        Map<String, Object> data = new HashMap<>();
        Page<Major> page = new Page<>(pageNum, pageSize);
        majorService.page(page, new LambdaQueryWrapper<Major>().like(StringUtils.hasLength(majorName), Major::getMajorName, majorName).orderByDesc(Major::getCreateTime));
        data.put("majorList", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    /**
     * 根据 id 删除专业
     *
     * @param majorId majorId
     * @return INFO
     */
    @DeleteMapping("/delById")
    public Result<String> delById(String majorId) {
        boolean remove = majorService.remove(new LambdaQueryWrapper<Major>().eq(Major::getMajorId, majorId));

        return remove ? Result.success("删除专业成功") : Result.fail("删除专业失败");
    }

    /**
     * 获取所有专业名称
     *
     * @return List<Major>
     */
    @GetMapping("/getAllMajorName")
    public Result<List<Major>> getAllMajorName() {
        List<Major> majorList = majorService.list();
        return Result.success(majorList);
    }
}
```

## 班级模块 Class

### Service

```java
/**
 * IClassService
 *
 * @author caobaoqi
 */
public interface IClassService extends IService<Class> {

}
```

```java
/**
 * ClassServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements IClassService {

}
```

### Mapper

```java
/**
 * ClassMapper
 *
 * @author caobaoqi
 */
public interface ClassMapper extends BaseMapper<Class> {
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="jz.cbq.backend.mapper.ClassMapper">

</mapper>

```

### Controller

```java
/**
 * ClassController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/class")
public class ClassController {
    @Resource
    private IMajorService majorService;
    @Resource
    private ITeacherService teacherService;
    @Resource
    private IStudentService studentService;
    @Resource
    private IClassService classService;

    /**
     * 添加班级
     *
     * @param data data
     * @return INFO
     */
    @Transactional
    @PostMapping("/add")
    public Result<String> addClass(@RequestBody Class data) {
        Major major = majorService.getOne(new LambdaQueryWrapper<Major>().eq(Major::getMajorName, data.getMajorName()).last("LIMIT 1"));
        Class maxClass = classService.getOne(new LambdaQueryWrapper<Class>().likeRight(Class::getClassId, data.getClassYear() + major.getMajorId()).orderByDesc(Class::getClassId).last("LIMIT 1"));
        if (maxClass != null) {
            data.setClassId(Integer.valueOf(maxClass.getClassId()) + 1 + "");
        } else {
            data.setClassId(data.getClassYear() + major.getMajorId() + "1");
        }
        data.setClassName(data.getClassYear() + "级" + data.getMajorName() + data.getClassId().charAt(6) + "班");
        data.setCreateTime(new Date());
        data.setMajorId(major.getMajorId());
        data.setStuTotal(0);
        boolean save = classService.save(data);
        if (save) {
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("class_total=class_total+1").eq(Major::getMajorName, data.getMajorName()));
            return Result.success("添加班级成功");
        }
        return Result.fail("添加班级失败");
    }

    /**
     * 条件分页展示班级信息
     *
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @param className className
     * @param teaName   teaName
     * @return Map<String, Object>
     */
    @GetMapping("/classPageList")
    public Result<Map<String, Object>> classPageList(int pageNum, int pageSize, @RequestParam(value = "className", required = false) String className,
                                                     @RequestParam(value = "teaName", required = false) String teaName) {
        Map<String, Object> data = new HashMap<>();
        Page<Class> page = new Page<>(pageNum, pageSize);

        Page<Class> paged = classService.page(page, new LambdaQueryWrapper<Class>().like(StringUtils.hasLength(className), Class::getClassName, className)
                .like(StringUtils.hasLength(teaName), Class::getTeaName, teaName).orderByDesc(Class::getCreateTime));
        if (paged != null) {
            data.put("classList", page.getRecords());
            data.put("total", page.getTotal());
        }
        if (data.keySet().isEmpty()) {
            return Result.fail("分页查询班级失败");
        }
        return Result.success(data);
    }

    /**
     * 通过 id 删除老师
     *
     * @param classId classId
     * @return INFO
     */
    @Transactional
    @DeleteMapping("/delClassById")
    public Result<String> delClassById(String classId) {
        Teacher teacher = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getClassId, classId).last("LIMIT 1"));
        Student student = studentService.getOne(new LambdaQueryWrapper<Student>().eq(Student::getClassId, classId).last("LIMIT 1"));
        if (student != null) {
            Major major = majorService.getOne(new LambdaQueryWrapper<Major>().eq(Major::getMajorName, student.getMajorName()).gt(Major::getClassTotal, 0).last("LIMIT 1"));
            if (major != null) {
                return Result.fail("该班级还有专业数据,无法删除");
            }
        }
        if (teacher != null) {
            return Result.fail("该班级还有老师数据,无法删除");
        } else if (student != null) {
            return Result.fail("该班级还有学生数据,无法删除");
        }

        Class serviceOne = classService.getOne(new LambdaQueryWrapper<Class>().eq(Class::getClassId, classId));
        boolean remove = classService.remove(new LambdaQueryWrapper<Class>().eq(Class::getClassId, classId));
        if (remove) {
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("class_total=class_total-1").eq(Major::getMajorId, serviceOne.getMajorId()));
            return Result.success("删除班级成功");
        }
        return Result.fail("删除班级失败");
    }

    /**
     * 根据专业名称获取班级列表
     *
     * @param majorName majorName
     * @param classYear classYear
     * @return List<Class>
     */
    @GetMapping("getClassesByMajorName")
    public Result<List<Class>> getClassesByMajorName(String majorName, String classYear) {
        List<Class> classList = classService.list(new LambdaQueryWrapper<Class>().eq(Class::getMajorName, majorName).eq(Class::getClassYear, classYear));
        return Result.success(classList);
    }

}
```

## 老师模块 Teacher

### Service

```java
/**
 * ITeacherService
 *
 * @author caobaoqi
 */
public interface ITeacherService extends IService<Teacher> {

}
```

```java
/**
 * TeacherServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

}

```

### Mapper

```java
/**
 * TeacherMapper
 *
 * @author caobaoqi
 */
public interface TeacherMapper extends BaseMapper<Teacher> {

}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="jz.cbq.backend.mapper.TeacherMapper">

</mapper>
```

### Controller

```java
/**
 * TeacherController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    private ITeacherService teacherService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private IStudentService studentService;
    @Resource
    private IClassService classService;
    @Resource
    private IMajorService majorService;

    /**
     * 条件老师
     *
     * @param teacher teacher
     * @return INFO
     */
    @Transactional
    @PostMapping("add")
    public Result<String> addTea(@RequestBody Teacher teacher) {
        String classYear = teacher.getClassYear();
        String majorName = teacher.getMajorName();
        String classId = teacher.getClassId();
        String teaName = teacher.getTeaName();
        String teaIdCard = teacher.getTeaIdCard();
        teacher.setClassName(classYear + "级" + majorName + classId.charAt(6) + "班");
        String className = teacher.getClassName();

        Teacher one = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getClassId, classId));
        Teacher one1 = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getTeaIdCard, teaIdCard).last("LIMIT 1"));
        Teacher one2 = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getClassName, className));
        Student student = studentService.getOne(new LambdaQueryWrapper<Student>().eq(Student::getStuIdCard, teaIdCard).last("LIMIT 1"));

        if (null != one || null != one2) {
            return Result.fail("该班级已有班主任");
        } else if (null != one1) {
            return Result.fail("该用户已注册班主任编号");
        } else if (null != student) {
            return Result.fail("该用户已注册学生");
        }

        teacher.setTeaId("tea" + classId);
        teacher.setMajorId(classId.substring(4, 6));
        teacher.setCreateTime(new Date());
        teacher.setClassNo(classId.substring(6, 7));
        teacher.setTeaPwd(passwordEncoder.encode(teacher.getTeaIdCard().substring(12, 18)));
        boolean save = teacherService.save(teacher);
        if (save) {
            studentService.update(new LambdaUpdateWrapper<Student>().set(Student::getTeaName, teaName).set(Student::getTeaId, teacher.getTeaId()).eq(Student::getClassId, classId));
            classService.update(new LambdaUpdateWrapper<Class>().set(Class::getTeaName, teacher.getTeaName()).eq(Class::getClassId, classId));
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("tea_total=tea_total+1").eq(Major::getMajorId, teacher.getMajorId()));

            return Result.success("添加班主任成功,班主任编号为" + teacher.getTeaId());
        }
        return Result.fail("添加班主任失败");
    }

    /**
     * 条件分页查询老师信息
     *
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @param teaId     teaId
     * @param teaName   teaName
     * @param className className
     * @return Map<String, Object>
     */
    @GetMapping("/teaPageList")
    public Result<Map<String, Object>> teaPageList(int pageNum, int pageSize, @RequestParam(value = "teaId", required = false) String teaId,
                                                   @RequestParam(value = "teaName", required = false) String teaName,
                                                   @RequestParam(value = "className", required = false) String className) {
        Map<String, Object> data = new HashMap<>();
        Page<Teacher> page = new Page<>(pageNum, pageSize);
        teacherService.page(page,
                new LambdaQueryWrapper<Teacher>().like(StringUtils.hasLength(teaName), Teacher::getTeaName, teaName)
                        .like(StringUtils.hasLength(teaId), Teacher::getTeaId, teaId)
                        .like(StringUtils.hasLength(className), Teacher::getClassName, className)
                        .orderByDesc(Teacher::getCreateTime));
        data.put("teaList", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    /**
     * 通过 id 获取老师
     *
     * @param teaId teaId
     * @return Teacher
     */
    @GetMapping("/getTeaById")
    public Result<Teacher> getTeaById(String teaId) {
        Teacher one = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getTeaId, teaId).last("LIMIT 1"));

        return one != null ? Result.success(one) : Result.fail("打开编辑表单失败");
    }

    /**
     * 编辑老师
     *
     * @param teacher teacher
     * @return INFO
     */
    @PutMapping("/editById")
    public Result<String> editById(@RequestBody Teacher teacher) {
        teacher.setTeaPwd(passwordEncoder.encode(teacher.getTeaPwd()));
        boolean update = teacherService.update(teacher, new LambdaUpdateWrapper<Teacher>().eq(Teacher::getTeaId, teacher.getTeaId()));
        if (update) {
            classService.update(new LambdaUpdateWrapper<Class>().set(Class::getTeaName, teacher.getTeaName()).eq(Class::getClassId, teacher.getClassId()));
            studentService.update(new LambdaUpdateWrapper<Student>().set(Student::getTeaName, teacher.getTeaName()).eq(Student::getTeaId, teacher.getTeaId()));
            return Result.success("编辑成功");
        }
        return Result.fail("编辑失败");
    }

    /**
     * 根据 id 删除老师
     *
     * @param teaId teaId
     * @return INFO
     */
    @DeleteMapping("/delTea")
    public Result<String> delTea(String teaId) {
        Teacher teacher = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getTeaId, teaId));
        boolean remove = teacherService.remove(new LambdaQueryWrapper<Teacher>().eq(Teacher::getTeaId, teaId));
        if (remove) {
            studentService.update(new LambdaUpdateWrapper<Student>().set(Student::getTeaName, "").set(Student::getTeaId, "").eq(Student::getTeaId, teaId));
            classService.update(new LambdaUpdateWrapper<Class>().set(Class::getTeaName, "").eq(Class::getClassId, teacher.getClassId()));
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("tea_total=tea_total-1").eq(Major::getMajorId, teacher.getMajorId()));
            return Result.success("删除班主任成功");
        }
        return Result.fail("删除班主任失败");
    }
}
```

## 课程模块 Course

### Service

```java
/**
 * ICourseService
 *
 * @author caobaoqi
 */
public interface ICourseService extends IService<Course> {
    /**
     * 获取下一个课程 id
     * @param majorId 专业 id
     * @return id
     */
    String findNextCourseId(String majorId);

    /**
     * 获取课程列表
     * @param limit1 limit1
     * @param limit2 limit2
     * @param courseName 课程名称
     * @param majorName 专业名称
     * @param schoolPeriodNum  schoolPeriodNum
     * @param coursePeriod 是否选修
     * @return List<Course>
     */
    List<Course> getMyAllCourseList(int limit1,int limit2,String courseName,String majorName,Integer schoolPeriodNum,String coursePeriod);
    /**
     * 获取课程数量
     * @param limit1 limit1
     * @param limit2 limit2
     * @param courseName 课程名称
     * @param majorName 专业名称
     * @param schoolPeriodNum  schoolPeriodNum
     * @param coursePeriod 是否选修
     * @return count
     */
    Integer getMyAllCourseCount(int limit1, int limit2, String courseName, String majorName, Integer schoolPeriodNum,String coursePeriod);

    /**
     * 更新课程数量
     * @param courseId 课程 id
     */
    void updateStuChooseNum(String courseId);

    /**
     * 通过专业 id 获取大四下课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList42(String majorId);
    /**
     * 通过专业 id 获取大三下课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList32(String majorId);
    /**
     * 通过专业 id 获取大二下课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList22(String majorId);
    /**
     * 通过专业 id 获取大一下课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList12(String majorId);
    /**
     * 通过专业 id 获取大四上课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList41(String majorId);
    /**
     * 通过专业 id 获取大三上课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList31(String majorId);
    /**
     * 通过专业 id 获取大二上课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList21(String majorId);
    /**
     * 通过专业 id 获取大一上课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList11(String majorId);

    List<Course> courseList51(String majorId);


    /**
     * 通过专业 id 获取大四下课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList42Must(String majorId);
    /**
     * 通过专业 id 获取大三下课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList32Must(String majorId);
    /**
     * 通过专业 id 获取大二下课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList22Must(String majorId);
    /**
     * 通过专业 id 获取大一下课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList12Must(String majorId);

    /**
     * 通过专业 id 获取大四上课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList41Must(String majorId);
    /**
     * 通过专业 id 获取大三上课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList31Must(String majorId);
    /**
     * 通过专业 id 获取大二上课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList21Must(String majorId);
    /**
     * 通过专业 id 获取大一上课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList11Must(String majorId);

    List<Course> courseList51Must(String majorId);

    /**
     * 更新选课数
     */
    void updateChooseCourseNum();
}
```

```java
/**
 * CourseServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Resource
    private CourseMapper courseMapper;

    @Override
    public String findNextCourseId(String majorId) {//05
        String maxCourseId = courseMapper.findMaxCourseId(majorId);//course 05 02
        int courseIdSuffix = 1;
        if (null != maxCourseId) {
            courseIdSuffix = Integer.parseInt(maxCourseId.substring(8, 10)) + 1;//3
            return "course" + majorId + String.format("%02d", courseIdSuffix);//03
        }
        return "course" + majorId + "01";
    }

    @Override
    public List<Course> getMyAllCourseList(int limit1, int limit2, String courseName, String majorName, Integer schoolPeriodNum, String coursePeriod) {
        return courseMapper.getMyAllCourseList(limit1, limit2, courseName, majorName, schoolPeriodNum, coursePeriod);
    }

    @Override
    public Integer getMyAllCourseCount(int limit1, int limit2, String courseName, String majorName, Integer schoolPeriodNum, String coursePeriod) {
        return courseMapper.getMyAllCourseCount(limit1, limit2, courseName, majorName, schoolPeriodNum, coursePeriod);
    }

    @Async("taskExecutor")
    @Override
    public void updateStuChooseNum(String courseId) {
        System.out.println("线程" + Thread.currentThread().getName() + " 执行异步任务：更新课程："+courseId+"学生选课数量+1");
        courseMapper.updateStuChooseNum(courseId);
    }

    @Override
    public List<Course> courseList42(String majorId) {
        return courseMapper.courseList42(majorId);
    }

    @Override
    public List<Course> courseList32(String majorId) {
        return courseMapper.courseList32(majorId);
    }

    @Override
    public List<Course> courseList22(String majorId) {
        return courseMapper.courseList22(majorId);
    }

    @Override
    public List<Course> courseList12(String majorId) {
        return courseMapper.courseList12(majorId);
    }

    @Override
    public List<Course> courseList41(String majorId) {
        return courseMapper.courseList41(majorId);
    }

    @Override
    public List<Course> courseList31(String majorId) {
        return courseMapper.courseList31(majorId);
    }

    @Override
    public List<Course> courseList21(String majorId) {
        return courseMapper.courseList21(majorId);
    }

    @Override
    public List<Course> courseList11(String majorId) {
        return courseMapper.courseList11(majorId);
    }

    @Override
    public List<Course> courseList51(String majorId) {
        return courseMapper.courseList51(majorId);
    }


    @Override
    public List<Course> courseList42Must(String majorId) {
        return courseMapper.courseList42Must(majorId);
    }

    @Override
    public List<Course> courseList32Must(String majorId) {
        return courseMapper.courseList32Must(majorId);
    }

    @Override
    public List<Course> courseList22Must(String majorId) {
        return courseMapper.courseList22Must(majorId);
    }

    @Override
    public List<Course> courseList12Must(String majorId) {
        return courseMapper.courseList12Must(majorId);
    }

    @Override
    public List<Course> courseList41Must(String majorId) {
        return courseMapper.courseList41Must(majorId);
    }

    @Override
    public List<Course> courseList31Must(String majorId) {
        return courseMapper.courseList31Must(majorId);
    }

    @Override
    public List<Course> courseList21Must(String majorId) {
        return courseMapper.courseList21Must(majorId);
    }

    @Override
    public List<Course> courseList11Must(String majorId) {
        return courseMapper.courseList11Must(majorId);
    }

    @Override
    public List<Course> courseList51Must(String majorId) {
        return courseMapper.courseList51Must(majorId);
    }

    @Async("taskExecutor")
    @Override
    public void updateChooseCourseNum() {
        System.out.println("线程--" + Thread.currentThread().getName() + "  执行异步任务：置空课程表选课数");
        courseMapper.updateChooseCourseNum();
    }
}
```

### Mapper

```java
/**
 * AdminMapper
 *
 * @author caobaoqi
 */
public interface CourseMapper extends BaseMapper<Course> {

    @Select("select course_id from t_course where course_id like concat('course',#{majorId},'%') order by course_id desc limit 1")
    String findMaxCourseId(String majorId);

    List<Course> getMyAllCourseList(int limit1, int limit2, String courseName, String majorName, Integer schoolPeriodNum, String coursePeriod);

    Integer getMyAllCourseCount(int limit1, int limit2, String courseName, String majorName, Integer schoolPeriodNum, String coursePeriod);

    @Update("update t_course set stu_choose_num=stu_choose_num+1 where course_id = #{courseId} ")
    void updateStuChooseNum(String courseId);

    @Select("select * from t_course where major_id=#{majorId} order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList42(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大三下','大三上','大二下','大二上','大一下','大一上') order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList32(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大二下','大二上','大一下','大一上') order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList22(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大一下','大一上') order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList12(String majorId);

    @Select("select * from t_course where major_id=#{majorId} order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList51(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大四上','大三下','大三上','大二下','大二上','大一下','大一上') order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList41(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大三上','大二下','大二上','大一下','大一上') order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList31(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大二上','大一下','大一上') order by  field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList21(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大一上') order by  field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList11(String majorId);


    @Select("select * from t_course where major_id=#{majorId} and if_degree='1'")
    List<Course> courseList42Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and if_degree='1'  and course_period in('大三下','大三上','大二下','大二上','大一下','大一上')")
    List<Course> courseList32Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大二下','大二上','大一下','大一上')")
    List<Course> courseList22Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大一下','大一上')")
    List<Course> courseList12Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1'")
    List<Course> courseList51Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大四上','大三下','大三上','大二下','大二上','大一下','大一上')")
    List<Course> courseList41Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大三上','大二下','大二上','大一下','大一上')")
    List<Course> courseList31Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大二上','大一下','大一上')")
    List<Course> courseList21Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大一上' )")
    List<Course> courseList11Must(String majorId);

    @Update("update t_course set stu_choose_num=0")
    void updateChooseCourseNum();
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="jz.cbq.backend.mapper.CourseMapper">

    <select id="getMyAllCourseList" resultMap="stuChooseCourseMap">
        select distinct a.course_id, a.*, b.state from t_course a left join t_choose_course b
        on a.course_id=b.course_id
        where a.major_name=#{majorName}
        <if test="courseName!='' and courseName!=null">
            and a.course_name like concat('%',#{courseName},'%')
        </if>
        <if test="coursePeriod!='' and coursePeriod!=null">
            and a.course_period =#{coursePeriod}
        </if>
        <if test="schoolPeriodNum==7">
            and a.course_period in('大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')
        </if>
        <if test="schoolPeriodNum==6">
            and a.course_period in('大一上','大一下','大二上','大二下','大三上','大三下','大四上')
        </if>
        <if test="schoolPeriodNum==5">
            and a.course_period in('大一上','大一下','大二上','大二下','大三上','大三下')
        </if>
        <if test="schoolPeriodNum==4">
            and a.course_period in('大一上','大一下','大二上','大二下','大三上')
        </if>
        <if test="schoolPeriodNum==3">
            and a.course_period in('大一上','大一下','大二上','大二下')
        </if>
        <if test="schoolPeriodNum==2">
            and a.course_period in('大一上','大一下','大二上')
        </if>
        <if test="schoolPeriodNum==1">
            and a.course_period in('大一上','大一下')
        </if>
        <if test="schoolPeriodNum==0">
            and a.course_period in('大一上')
        </if>
        order by b.state,field (a.course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下') desc
        limit #{limit1},#{limit2}
    </select>
    <select id="getMyAllCourseCount" resultType="java.lang.Integer">
        select count(DISTINCT a.course_id) from t_course a left join t_choose_course b
        on a.course_id=b.course_id
        where a.major_name=#{majorName}
        <if test="courseName!='' and courseName!=null">
            and a.course_name like concat('%',#{courseName},'%')
        </if>
        <if test="coursePeriod!='' and coursePeriod!=null">
            and a.course_period =#{coursePeriod}
        </if>
        <if test="schoolPeriodNum==7">
            and a.course_period in('大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')
        </if>
        <if test="schoolPeriodNum==6">
            and  a.course_period in('大一上','大一下','大二上','大二下','大三上','大三下','大四上')
        </if>
        <if test="schoolPeriodNum==5">
            and a.course_period in('大一上','大一下','大二上','大二下','大三上','大三下')
        </if>
        <if test="schoolPeriodNum==4">
            and a.course_period in('大一上','大一下','大二上','大二下','大三上')
        </if>
        <if test="schoolPeriodNum==3">
            and a.course_period in('大一上','大一下','大二上','大二下')
        </if>
        <if test="schoolPeriodNum==2">
            and a.course_period in('大一上','大一下','大二上')
        </if>
        <if test="schoolPeriodNum==1">
            and a.course_period in('大一上','大一下')
        </if>
        <if test="schoolPeriodNum==0">
            and a.course_period in('大一上')
        </if>
        limit #{limit1},#{limit2}
    </select>
    <resultMap id="stuChooseCourseMap" type="jz.cbq.backend.entity.StuChooseCourseMap">
        <id column="choose_course_id" property="chooseCourseId"/>
        <result column="course_id" property="courseId"/>
        <result column="major_name" property="majorId"/>
        <result column="course_name" property="courseName"/>
        <result column="if_degree" property="ifDegree"/>
        <result column="major_name" property="majorName"/>
        <result column="create_time" property="createTime"/>
        <result column="course_period" property="coursePeriod"/>
        <result column="stu_choose_num" property="stuChooseNum"/>
        <result column="stu_id" property="stuId"/>
        <result column="state" property="state"/>
        <result column="stu_name" property="stuName"/>

    </resultMap>
</mapper>

```

### Controller

```java
/**
 * CourseController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    @Resource
    private IMajorService majorService;
    @Resource
    private ICourseService courseService;

    /**
     * 添加课程
     *
     * @param course course
     * @return INFO
     */
    @Transactional
    @PostMapping("/add")
    public Result<String> addCourse(@RequestBody Course course) {
        String courseName = course.getCourseName();
        Course courseDatabase = courseService.getOne(new LambdaQueryWrapper<Course>().eq(Course::getCourseName, courseName).last("LIMIT 1"));
        if (courseDatabase != null) {
            return Result.fail("存在同名课程，添加失败");
        }
        String majorName = course.getMajorName();
        Major major = majorService.getOne(new LambdaQueryWrapper<Major>().eq(Major::getMajorName, majorName).last("LIMIT 1"));
        course.setMajorId(major.getMajorId());
        course.setCreateTime(new Date());
        String nextCourseId = courseService.findNextCourseId(major.getMajorId());
        course.setCourseId(nextCourseId);
        course.setStuChooseNum(0);
        boolean save = courseService.save(course);
        if (save) {
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("course_total=course_total+1").eq(Major::getMajorName, course.getMajorName()));
            return Result.success("添加课程成功");
        }
        return Result.fail("添加课程失败");
    }

    /**
     * 条件分页查询课程信息
     *
     * @param pageNum      pageNum
     * @param pageSize     pageSize
     * @param courseName   courseName
     * @param majorName    majorName
     * @param coursePeriod coursePeriod
     * @return Map<String, Object>
     */
    @GetMapping("/coursePageList")
    public Result<Map<String, Object>> coursePageList(int pageNum, int pageSize, @RequestParam(value = "courseName") String courseName,
                                                      @RequestParam(value = "majorName") String majorName,
                                                      @RequestParam(value = "coursePeriod") String coursePeriod) {
        Map<String, Object> data = new HashMap<>();
        Page<Course> page = new Page<>(pageNum, pageSize);
        courseService.page(page,
                new LambdaQueryWrapper<Course>().like(StringUtils.hasLength(courseName), Course::getCourseName, courseName)
                        .like(StringUtils.hasLength(majorName), Course::getMajorName, majorName)
                        .eq(StringUtils.hasLength(coursePeriod), Course::getCoursePeriod, coursePeriod)
                        .orderByDesc(Course::getCreateTime));
        data.put("courseList", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    /**
     * 通过 id 获取课程
     *
     * @param courseId courseId
     * @return Course
     */
    @GetMapping("/getCourseById")
    public Result<Course> getCourseById(String courseId) {
        Course course = courseService.getOne(new LambdaQueryWrapper<Course>().eq(Course::getCourseId, courseId).last("LIMIT 1"));

        return course != null ? Result.success(course) : Result.fail("加载课程失败");
    }

    /**
     * 编辑课程
     *
     * @param course course
     * @return INFO
     */
    @Transactional
    @PutMapping("/edit")
    public Result<String> editCourseById(@RequestBody Course course) {
        boolean update = courseService.update(course, new LambdaUpdateWrapper<Course>().eq(Course::getCourseId, course.getCourseId()));

        return update ? Result.success("修改课程成功") : Result.fail("修改课程失败");
    }

    /**
     * 通过 id 删除课程
     *
     * @param courseId courseId
     * @return INFO
     */
    @Transactional
    @DeleteMapping("/delCourseById")
    public Result<String> delCourseById(String courseId) {
        Course course = courseService.getOne(new LambdaQueryWrapper<Course>().eq(Course::getCourseId, courseId));
        boolean remove = courseService.remove(new LambdaQueryWrapper<Course>().eq(Course::getCourseId, courseId));
        if (remove) {
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("course_total=course_total-1").eq(Major::getMajorId, course.getMajorId()));
            return Result.success("删除课程成功");
        }
        return Result.fail("删除课程失败");
    }

    /**
     * 根据专业 id 和 年纪获取课程列表
     *
     * @param majorId       majorId
     * @param admissionYear admissionYear
     * @return List<Course>
     */
    @GetMapping("/getScoreCourse")
    public Result<List<Course>> getScoreCourse(String majorId, String admissionYear) {
        int nowYear = LocalDate.now().getYear();
        int nowMonth = LocalDate.now().getMonthValue();
        if (nowMonth >= 3 && nowMonth < 9) {
            if (nowYear - Integer.parseInt(admissionYear) >= 4) {
                List<Course> courseList = courseService.courseList42(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 3) {
                List<Course> courseList = courseService.courseList32(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 2) {
                List<Course> courseList = courseService.courseList22(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 1) {
                List<Course> courseList = courseService.courseList12(majorId);
                return Result.success(courseList);
            }
        } else {
            if (nowYear - Integer.parseInt(admissionYear) >= 4) {
                List<Course> courseList = courseService.courseList51(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 3) {
                List<Course> courseList = courseService.courseList41(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 2) {
                List<Course> courseList = courseService.courseList31(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 1) {
                List<Course> courseList = courseService.courseList21(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 0) {
                List<Course> courseList = courseService.courseList11(majorId);
                return Result.success(courseList);
            }
        }
        return Result.fail();
    }
}
```

## 学生模块 Student

### Service

```java
/**
 * ITeaMessageService
 *
 * @author caobaoqi
 */
public interface IStudentService extends IService<Student> {
    /**
     * 获取下一个学生 id
     * @param stuIdPrefix 学生 id 前缀
     * @return id
     */
    String findNextStuId(String stuIdPrefix);

    List<Student> studentList42(int nowYear,String majorId);

    List<Student> studentList32(int nowYear,String majorId);

    List<Student> studentList22(int nowYear,String majorId);

    List<Student> studentList12(int nowYear,String majorId);

    List<Student> studentList41(int nowYear, String majorId);
    List<Student> studentList31(int nowYear, String majorId);
    List<Student> studentList21(int nowYear, String majorId);
    List<Student> studentList11(int nowYear, String majorId);

    List<Student> studentList51(int nowYear, String majorId);

    /**
     * 消息
     * @param stuId 学生 id
     */
    void plusMsgNum(String stuId);
    /**
     * 删除消息
     * @param stuId 学生 id
     */
    void delMsgNum(String stuId);
}
```

```java
/**
 * StudentServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
    @Resource
    private StudentMapper studentMapper;

    @Override
    public String findNextStuId(String stuIdPrefix) {
        int nextNum;
        String maxStuId = studentMapper.findMaxStuId(stuIdPrefix);
        if (null == maxStuId) {
            return stuIdPrefix + "01";
        }
        nextNum = Integer.parseInt(maxStuId.substring(7, 9)) + 1;
        return stuIdPrefix + String.format("%02d", Integer.valueOf(nextNum));
    }


    @Override
    public List<Student> studentList42(int nowYear, String majorId) {
        return studentMapper.studentList42(nowYear, majorId);
    }

    @Override
    public List<Student> studentList32(int nowYear, String majorId) {
        return studentMapper.studentList32(nowYear, majorId);
    }

    @Override
    public List<Student> studentList22(int nowYear, String majorId) {
        return studentMapper.studentList22(nowYear, majorId);
    }

    @Override
    public List<Student> studentList12(int nowYear, String majorId) {
        return studentMapper.studentList12(nowYear, majorId);
    }

    @Override
    public List<Student> studentList41(int nowYear, String majorId) {
        return studentMapper.studentList41(nowYear, majorId);
    }

    @Override
    public List<Student> studentList31(int nowYear, String majorId) {
        return studentMapper.studentList31(nowYear, majorId);
    }

    @Override
    public List<Student> studentList21(int nowYear, String majorId) {
        return studentMapper.studentList21(nowYear, majorId);
    }

    @Override
    public List<Student> studentList11(int nowYear, String majorId) {
        return studentMapper.studentList11(nowYear, majorId);
    }

    @Override
    public List<Student> studentList51(int nowYear, String majorId) {
        return studentMapper.studentList51(nowYear, majorId);
    }

    @Async("taskExecutor")
    @Override
    public void plusMsgNum(String stuId) {
        System.out.println("线程--" + Thread.currentThread().getName() + " 执行异步任务：更新学生表中学生" + stuId + "的消息数+1");
        studentMapper.plusMsgNum(stuId);
    }

    @Async("taskExecutor")
    @Override
    public void delMsgNum(String stuId) {
        System.out.println("线程--" + Thread.currentThread().getName() + " 执行异步任务：更新学生表中学生" + stuId + "的消息数-1");
        studentMapper.delMsgNum(stuId);
    }
}
```

### Mapper

```java
/**
 * AdminMapper
 *
 * @author caobaoqi
 */
public interface StudentMapper extends BaseMapper<Student> {

    @Select("select stu_id from t_student where stu_id like concat(#{stuIdPrefix},'%') order by stu_id desc limit 1")
    String findMaxStuId(String stuIdPrefix);

    @Delete("delete from t_student where stu_id = #{stu_id}")
    int delStuById(String stuId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))>=4 and major_id=#{majorId}")
    List<Student> studentList42(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=3 and major_id=#{majorId}")
    List<Student> studentList32(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=2 and major_id=#{majorId}")
    List<Student> studentList22(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=1 and major_id=#{majorId}")
    List<Student> studentList12(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))>=3 and major_id=#{majorId}")
    List<Student> studentList41(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=2 and major_id=#{majorId}")
    List<Student> studentList31(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=1 and major_id=#{majorId}")
    List<Student> studentList21(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=0 and major_id=#{majorId}")
    List<Student> studentList11(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))>=4 and major_id=#{majorId}")
    List<Student> studentList51(int nowYear, String majorId);
    @Update("update t_student set message_num=message_num+1 where stu_id=#{stuId}")
    void plusMsgNum(String stuId);
    @Update("update t_student set message_num=message_num-1 where stu_id=#{stuId}")
    void delMsgNum(String stuId);
}

```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="jz.cbq.backend.mapper.StudentMapper">

</mapper>
```

### Controller

```java
/**
 * StudentController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private IStudentService studentService;
    @Resource
    private IClassService classService;
    @Resource
    private ITeacherService teacherService;
    @Resource
    private IMajorService majorService;
    @Resource
    private ICourseService courseService;
    @Resource
    private IChooseCourseService chooseCourseService;
    @Resource
    private IScoreService scoreService;
    @Resource
    private IStuMessageService stuMessageService;
    @Resource
    private ITeaMessageService teaMessageService;

    /**
     * 添加学生
     *
     * @param student student
     * @return INFO
     */
    @Transactional
    @PostMapping("/add")
    public Result<String> addStu(@RequestBody Student student) {
        String admissionYear = student.getAdmissionYear();
        String majorName = student.getMajorName();
        String classId = student.getClassId();
        String idCard = student.getStuIdCard();

        Student repeatStuIDCard = studentService.getOne(new LambdaQueryWrapper<Student>().eq(Student::getStuIdCard, idCard));
        if (null != repeatStuIDCard) {
            return Result.fail("该 IDCard 已注册学生");
        }

        Teacher repeatTeaIDCard = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getTeaIdCard, idCard));
        if (null != repeatTeaIDCard) {
            return Result.fail("该 IDCard 已注册班主任");
        }

        String nextStuId = studentService.findNextStuId(classId);
        student.setStuId(nextStuId);

        Class repeatClassId = classService.getOne(new LambdaQueryWrapper<Class>().eq(Class::getClassId, classId));

        if (!StringUtils.hasText(repeatClassId.getTeaName())) {
            student.setTeaName("");
            student.setTeaId("");
        } else {
            Teacher teacher = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getClassId, classId).last("LIMIT 1"));
            student.setTeaId(teacher.getTeaId());
            student.setTeaName(teacher.getTeaName());
        }
        student.setMajorId(classId.substring(4, 6));
        student.setClassName(admissionYear + " 级" + majorName + classId.charAt(6) + " 班");
        student.setClassNo(classId.substring(6, 7));
        student.setCreateTime(new Date());
        student.setStuPwd(passwordEncoder.encode(idCard.substring(12, 18)));
        boolean save = studentService.save(student);
        if (save) {
            classService.update(new LambdaUpdateWrapper<Class>().setSql("stu_total=stu_total+1").eq(Class::getClassId, classId));
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("stu_total=stu_total+1").eq(Major::getMajorId, student.getMajorId()));

            System.out.println("线程--" + Thread.currentThread().getName() + " 执行异步任务：" + student.getStuId());
            return Result.success("添加学生成功");
        }
        return Result.fail("添加学生失败");
    }

    /**
     * 条件分页展示学生列表
     *
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @param stuId     stuId
     * @param stuName   stuName
     * @param className className
     * @return INFO
     */
    @GetMapping("/stuPageList")
    public Result<Map<String, Object>> stuPageList(int pageNum, int pageSize,
                                                   @RequestParam(value = "stuId", required = false) String stuId,
                                                   @RequestParam(value = "stuName", required = false) String stuName,
                                                   @RequestParam(value = "className", required = false) String className) {
        Map<String, Object> data = new HashMap<>();
        Page<Student> page = new Page<>(pageNum, pageSize);
        studentService.page(page,
                new LambdaQueryWrapper<Student>()
                        .like(StringUtils.hasLength(stuId), Student::getStuId, stuId)
                        .like(StringUtils.hasLength(stuName), Student::getStuName, stuName)
                        .like(StringUtils.hasLength(className), Student::getClassName, className)
                        .orderByDesc(Student::getCreateTime)
        );
        data.put("stuList", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    /**
     * 根据 id 删除学生
     *
     * @param stuId stuId
     * @return INFO
     */
    @Transactional
    @DeleteMapping("/delete")
    public Result<String> deleteStu(String stuId) {
        Score scoreDatabase = scoreService.getOne(new LambdaQueryWrapper<Score>().eq(Score::getStuId, stuId).last("LIMIT 1"));
        StuMessage stuMessageDatabase = stuMessageService.getOne(new LambdaQueryWrapper<StuMessage>().eq(StuMessage::getStuId, stuId).last("LIMIT 1"));
        TeaMessage teaMessageDatabase = teaMessageService.getOne(new LambdaQueryWrapper<TeaMessage>().eq(TeaMessage::getStuId, stuId).last("LIMIT 1"));
        ChooseCourse chooseCourseDatabase = chooseCourseService.getOne(new LambdaQueryWrapper<ChooseCourse>().eq(ChooseCourse::getStuId, stuId).last("LIMIT 1"));

        if (scoreDatabase != null) {
            return Result.fail("该学生还有成绩数据,无法删除");
        } else if (stuMessageDatabase != null || teaMessageDatabase != null) {
            return Result.fail("该学生还有消息数据,无法删除");
        } else if (chooseCourseDatabase != null) {
            return Result.fail("该学生还有选课数据,无法删除");
        }

        Student student = studentService.getOne(new LambdaQueryWrapper<Student>().eq(Student::getStuId, stuId));
        boolean delStuById = studentService.remove(new LambdaQueryWrapper<Student>().eq(Student::getStuId, stuId));
        if (delStuById) {
            classService.update(new LambdaUpdateWrapper<Class>().setSql("stu_total=stu_total-1").eq(Class::getClassId, student.getClassId()));
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("stu_total=stu_total-1").eq(Major::getMajorId, student.getMajorId()));
            return Result.success("删除学生成功");
        }
        return Result.fail("删除学生失败");
    }

    /**
     * 通过 id 获取学生
     *
     * @param stuId stuId
     * @return INFO
     */
    @GetMapping("/getByStuId")
    public Result<Student> getByStuId(String stuId) {
        if (!StringUtils.hasText(stuId)) {
            return Result.fail("学生 id 不能为空");
        }
        Student studentDatabase = studentService.getOne(new LambdaQueryWrapper<Student>().eq(Student::getStuId, stuId));

        return studentDatabase != null ? Result.success(studentDatabase) : Result.fail("没有该学生信息");
    }

    /**
     * 通过 id 编辑学生
     *
     * @param student student
     * @return INFO
     */
    @Transactional
    @PutMapping("editByStuId")
    public Result<String> editByStuId(@RequestBody Student student) {
        student.setStuPwd(passwordEncoder.encode(student.getStuPwd()));
        boolean update = studentService.update(student, new LambdaQueryWrapper<Student>().eq(Student::getStuId, student.getStuId()));
        if (update) {
            chooseCourseService.updateStuName(student.getStuId(), student.getStuName());
            scoreService.updateStuName(student.getStuId(), student.getStuName());
            return Result.success("编辑学生成功");
        }
        return Result.fail("编辑学生失败");
    }

    /**
     * 条件分页展示课程信息
     *
     * @param pageNum         pageNum
     * @param pageSize        pageSize
     * @param courseName      courseName
     * @param coursePeriod    coursePeriod
     * @param majorName       majorName
     * @param schoolPeriodNum schoolPeriodNum
     * @return INFO
     */
    @GetMapping("/coursePageList")
    public Result<Map<String, Object>> coursePageList(int pageNum, int pageSize,
                                                      @RequestParam(value = "courseName", required = false) String courseName,
                                                      @RequestParam(value = "coursePeriod", required = false) String coursePeriod,
                                                      String majorName, Integer schoolPeriodNum) {
        Map<String, Object> data = new HashMap<>();
        List<Course> myAllCourseList = courseService.getMyAllCourseList((pageNum - 1) * pageSize, pageSize, courseName, majorName, schoolPeriodNum, coursePeriod);
        Integer total = courseService.getMyAllCourseCount((pageNum - 1) * pageSize, pageSize, courseName, majorName, schoolPeriodNum, coursePeriod);
        data.put("courseList", myAllCourseList);
        data.put("total", total);
        return Result.success(data);
    }
}
```
## 选课模块 ChooseCourse

### Service

```java
/**
 * IChooseCourseService
 *
 * @author caobaoqi
 */
public interface IChooseCourseService extends IService<ChooseCourse> {

    /**
     * 更新学生名称
     * @param stuId 学生 id
     * @param stuName 学生名称
     */
    void updateStuName(String stuId,String stuName);

    /**
     * 取消选择课程
     * @return count
     */
    int cancelChooseCourse();





}
```

```java
/**
 * ChooseCourseServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class ChooseCourseServiceImpl extends ServiceImpl<ChooseCourseMapper, ChooseCourse> implements IChooseCourseService {
    @Resource
    private ChooseCourseMapper chooseCourseMapper;

    @Async("taskExecutor")
    @Override
    public void updateStuName(String stuId, String stuName) {
        System.out.println("线程" + Thread.currentThread().getName() + " 执行异步任务：更新选课表中学生姓名");
        chooseCourseMapper.updateStuName(stuId, stuName);
    }

    @Override
    public int cancelChooseCourse() {
        return chooseCourseMapper.cancelChooseCourse();
    }



}
```

### Mapper

```java
/**
 * ChooseCourseMapper
 *
 * @author caobaoqi
 */
public interface ChooseCourseMapper extends BaseMapper<ChooseCourse> {
    @Update("UPDATE t_choose_course SET stu_name=#{stuName} WHERE stu_id=#{stuId}")
    void updateStuName(String stuId, String stuName);
    
    @Delete("DELETE from t_choose_course")
    int cancelChooseCourse();
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="jz.cbq.backend.mapper.ChooseCourseMapper">

</mapper>

```

### Controller

```java
/**
 * ChooseCourseController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/chooseCourse")
public class ChooseCourseController {
    @Resource
    private IChooseCourseService chooseCourseService;
    @Resource
    private ICourseService courseService;
    @Resource
    private IScoreService scoreService;

    /**
     * 选课
     *
     * @param chooseCourse chooseCourse
     * @return INFO
     */
    @Transactional
    @PostMapping("/add")
    public Result<String> add(@RequestBody ChooseCourse chooseCourse) {
        String courseId = chooseCourse.getCourseId();
        String stuId = chooseCourse.getStuId();
        String chooseCourseId = stuId + "choose" + courseId;
        chooseCourse.setChooseCourseId(chooseCourseId);

        chooseCourse.setCreateTime(new Date());
        chooseCourse.setState(1);

        boolean save = chooseCourseService.save(chooseCourse);
        if (save) {
            courseService.updateStuChooseNum(courseId);
            return Result.success("选课成功");
        }
        return Result.fail("选课失败");
    }

    /**
     * 通过学生 id 获取可选课程列表
     *
     * @param stuId stuId
     * @return List<ChooseCourse>
     */
    @GetMapping("/getCanScoreCourse")
    public Result<List<ChooseCourse>> getCanScoreCourse(String stuId) {
        List<ChooseCourse> canScoreCourseList = chooseCourseService.list(new LambdaQueryWrapper<ChooseCourse>().eq(ChooseCourse::getStuId, stuId));
        return Result.success(canScoreCourseList);
    }

    /**
     * 取消所有选课
     *
     * @return INFO
     */
    @Transactional
    @DeleteMapping("/cancelChooseCourse")
    public Result<String> cancelChooseCourse() {

        if (!StringUtils.isEmpty(scoreService.getMax())) {
            return Result.fail("还有成绩数据无法进行一键退课,请先将分数置空");
        } else {
            long start = System.currentTimeMillis();
            int cancelChooseCourse = chooseCourseService.cancelChooseCourse();
            if (cancelChooseCourse >= 1) {
                courseService.updateChooseCourseNum();
                long end = System.currentTimeMillis();
                return Result.success("一键退课完毕,服务器花费时间" + (end - start) + "ms");
            }
            return Result.fail("一键退课失败");
        }
    }
}

```
## 成绩模块 Score

### Service

```java
/**
 * IScoreService
 *
 * @author caobaoqi
 */
public interface IScoreService extends IService<Score> {
    /**
     * 获取学生成绩列表
     * @param admissionYearMajor 年级
     * @param stuName 学生名称
     * @param stuId 学生 id
     * @param className 班级名称
     * @param limit1 limit1
     * @param limit2 limit2
     * @return List<StuScoreMap>
     */
    List<StuScoreMap> getStuScoreList(String admissionYearMajor,String stuName,String stuId,String className,int limit1,int limit2);
    /**
     * 获取学生成绩数量
     * @param admissionYearMajor 年级
     * @param stuName 学生名称
     * @param stuId 学生 id
     * @param className 班级名称
     * @param limit1 limit1
     * @param limit2 limit2
     * @return count
     */
    int getStuScoreTotal(String admissionYearMajor, String stuName, String stuId, String className, int limit1, int limit2);

    /**
     * 更新学生名称
     * @param stuId 学生 id
     * @param stuName 学生名称
     */
    void updateStuName(String stuId, String stuName);

    /**
     * 取消所有成绩
     * @return count
     */
    int cancelAllScore();

    String getMax();
}
```

```java
/**
 * ScoreServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements IScoreService {
    @Resource
    private ScoreMapper scoreMapper;

    @Override
    public List<StuScoreMap> getStuScoreList(String admissionYearMajor, String stuName, String stuId, String className, int limit1, int limit2) {
        return scoreMapper.getStuScoreList(admissionYearMajor, stuName, stuId, className, limit1, limit2);
    }


    @Override
    public int getStuScoreTotal(String admissionYearMajor, String stuName, String stuId, String className, int limit1, int limit2) {
        return scoreMapper.getStuScoreTotal(admissionYearMajor, stuName, stuId, className, limit1, limit2);
    }

    @Async("taskExecutor")
    @Override
    public void updateStuName(String stuId, String stuName) {
        System.out.println("线程--" + Thread.currentThread().getName() + " 执行异步任务：更新分数表中学生：" + stuId + " 的姓名");
        scoreMapper.updateStuName(stuId, stuName);
    }

    @Override
    public int cancelAllScore() {
        return scoreMapper.cancelAllScore();
    }

    @Override
    public String getMax() {
        return scoreMapper.getMaxId();
    }
}
```

### Mapper

```java
/**
 * ScoreMapper
 *
 * @author caobaoqi
 */
public interface ScoreMapper extends BaseMapper<Score> {

    @Select("SELECT score_id FROM t_score ORDER BY score_id DESC LIMIT 1")
    String getMaxId();

    List<StuScoreMap> getStuScoreList(String admissionYearMajor, String stuName, String stuId, String className, int limit1, int limit2);

    int getStuScoreTotal(String admissionYearMajor, String stuName, String stuId, String className, int limit1, int limit2);


    @Update("update t_score set stu_name=#{stuName} where stu_id=#{stuId}")
    void updateStuName(String stuId, String stuName);

    @Delete("delete from t_score")
    int cancelAllScore();
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="jz.cbq.backend.mapper.ScoreMapper">
        
    <select id="getStuScoreList" resultMap="stuScoreMap">
        SELECT a.stu_id, a.stu_name, b.class_name, sum(score) c
        FROM t_score a INNER JOIN t_student b ON a.stu_id=b.stu_id INNER JOIN t_choose_course d ON a.choose_course_id=d.choose_course_id
        WHERE a.stu_id LIKE concat(#{admissionYearMajor},'%') and d.if_degree='1'
        <if test="stuName!='' and stuName!=null">
            and a.stu_name LIKE concat('%',#{stuName},'%')
        </if>
        <if test="stuId!='' and stuId!=null">
            and a.stu_id LIKE concat('%',#{stuId},'%')
        </if>
        <if test="className!='' and className!=null">
            and substr(b.class_name,5) LIKE concat('%',#{className},'%')
        </if>
        GROUP BY a.stu_id,a.stu_name ORDER BY c DESC
        LIMIT #{limit1},#{limit2}
    </select>

    <select id="getStuScoreTotal" resultType="java.lang.Integer">
        select count(DISTINCT a.stu_id)
        FROM t_score a INNER JOIN t_student b on a.stu_id=b.stu_id
        WHERE a.stu_id LIKE concat(#{admissionYearMajor},'%')
        <if test="stuName!='' and stuName!=null">
            and a.stu_name LIKE concat('%',#{stuName},'%')
        </if>
        <if test="stuId!='' and stuId!=null">
            and a.stu_id LIKE concat('%',#{stuId},'%')
        </if>
        <if test="className!='' and className!=null">
            and substr(b.class_name,5) LIKE concat('%',#{className},'%')
        </if>
    </select>

    <resultMap id="stuScoreMap" type="jz.cbq.backend.entity.StuScoreMap">
        <id column="stu_id" property="stuId"/>
        <result column="stu_id" property="stuName"/>
        <result column="stu_name" property="stuName"/>
        <result column="class_name" property="className"/>
        <result column="c" property="scoreTotal"/>
    </resultMap>
</mapper>

```

### Controller

```java
/**
 * ScoreController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/score")
public class ScoreController {
    @Resource
    private IScoreService scoreService;
    @Resource
    private IChooseCourseService chooseCourseService;
    @Resource
    private ICourseService courseService;

    /**
     * 通过学生 id 和课程名称获取成绩
     *
     * @param courseName courseName
     * @param stuId      stuId
     * @return 成绩
     */
    @GetMapping("getScoreByCourseName")
    public Result<Integer> getScoreByCourseName(String courseName, String stuId) {
        Score score = scoreService.getOne(new LambdaQueryWrapper<Score>().eq(Score::getCourseName, courseName).eq(Score::getStuId, stuId));

        return score != null ? Result.success(score.getScore()) : Result.success();
    }

    /**
     * 打分
     *
     * @param score score
     * @return INFO
     */
    @PutMapping("/giveNewScore")
    public Result<String> giveNewScore(@RequestBody Score score) {

        String stuId = score.getStuId();
        String courseName = score.getCourseName();
        ChooseCourse chooseCourseDatabase = chooseCourseService.getOne(new LambdaQueryWrapper<ChooseCourse>().eq(ChooseCourse::getCourseName, courseName).eq(ChooseCourse::getStuId, stuId));
        String chooseCourseId = chooseCourseDatabase.getChooseCourseId();
        Score scoreDatabase = scoreService.getOne(new LambdaQueryWrapper<Score>().eq(Score::getStuId, stuId).eq(Score::getChooseCourseId, chooseCourseId));

        if (scoreDatabase != null) {
            scoreService.update(score, new LambdaUpdateWrapper<Score>().eq(Score::getStuId, stuId).eq(Score::getChooseCourseId, chooseCourseId));
            return Result.success("更新分数成功");
        } else {
            score.setChooseCourseId(chooseCourseId);
            score.setScoreId(stuId + "-" + chooseCourseId);
            score.setCreateTime(new Date());
            scoreService.save(score);
            return Result.success("初次打分成功");
        }

    }

    /**
     * 条件分页获取学生成绩列表
     *
     * @param stuName            stuName
     * @param stuId              stuId
     * @param className          className
     * @param admissionYearMajor admissionYearMajor
     * @param pageNum            pageNum
     * @param pageSize           pageSize
     * @return HashMap<String, Object>
     */
    @GetMapping("/getStuScoreList")
    public Result<HashMap<String, Object>> getStuScoreList(@RequestParam(value = "stuName", required = false) String stuName,//201901
                                                           @RequestParam(value = "stuId", required = false) String stuId,
                                                           @RequestParam(value = "className", required = false) String className, String admissionYearMajor, int pageNum, int pageSize) {
        HashMap<String, Object> data = new HashMap<>();
        int nowYear = LocalDate.now().getYear() + 1900;
        int nowMonth = LocalDate.now().getMonthValue() + 1;
        String admissionYear = admissionYearMajor.substring(0, 4);
        String majorId = admissionYearMajor.substring(4, 6);
        List<Course> courseList = new ArrayList<>();
        if (nowMonth >= 3 && nowMonth < 9) {
            if (nowYear - Integer.parseInt(admissionYear) >= 4) {
                courseList = courseService.courseList42(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 3) {
                courseList = courseService.courseList32(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 2) {
                courseList = courseService.courseList22(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 1) {
                courseList = courseService.courseList12(majorId);
            }
        } else {
            if (nowYear - Integer.parseInt(admissionYear) >= 4) {
                courseList = courseService.courseList51(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 3) {
                courseList = courseService.courseList41(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 2) {
                courseList = courseService.courseList31(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 1) {
                courseList = courseService.courseList21(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 0) {
                courseList = courseService.courseList11(majorId);
            }
        }
        List<StuScoreMap> stuScoreList = scoreService.getStuScoreList(admissionYearMajor, stuName, stuId, className, (pageNum - 1) * pageSize, pageSize);
        int total = scoreService.getStuScoreTotal(admissionYearMajor, stuName, stuId, className, (pageNum - 1) * pageSize, pageSize);
        for (StuScoreMap stuScoreMap : stuScoreList) {
            List<Integer> courseScoreList = new ArrayList<>();
            for (Course course : courseList) {
                Score scoreDatabase = scoreService.getOne(new LambdaQueryWrapper<Score>().eq(Score::getChooseCourseId, stuScoreMap.getStuId() + "choose" + course.getCourseId()));
                if (null == scoreDatabase) {
                    courseScoreList.add(0);
                } else {
                    courseScoreList.add(scoreDatabase.getScore());
                }
                stuScoreMap.setCourseScore(courseScoreList);
            }
        }
        data.put("stuScoreList", stuScoreList);
        data.put("total", total);
        return Result.success(data);
    }

    /**
     * 取消所有成绩
     *
     * @return INFO
     */
    @DeleteMapping("/cancelAllScore")
    public Result<String> cancelAllScore() {
        long start = System.currentTimeMillis();
        int cancelAllScore = scoreService.cancelAllScore();
        if (cancelAllScore >= 1) {
            long end = System.currentTimeMillis();
            return Result.success("一键分数置空完毕,服务器花费时间" + (end - start) + "ms");
        }
        return Result.fail("一键分数置空失败");
    }
}
```

## 消息模块

### 学生消息 StuMessage

### Service

```java
/**
 * IStuMessageService
 *
 * @author caobaoqi
 */
public interface IStuMessageService extends IService<StuMessage> {

}

```

```java
/**
 * StuMessageServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class StuMessageServiceImpl extends ServiceImpl<StuMessageMapper, StuMessage> implements IStuMessageService {

}
```

### Mapper

```java
/**
 * StuMessageMapper
 *
 * @author caobaoqi
 */
public interface StuMessageMapper extends BaseMapper<StuMessage> {

}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="jz.cbq.backend.mapper.StuMessageMapper">

</mapper>

```
### Controller

```java
/**
 * StuMessageController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/stuMessage")
public class StuMessageController {
    @Resource
    private IStuMessageService stuMessageService;
    @Resource
    private IStudentService studentService;

    /**
     * 添加消息
     * @param stuMessage stuMessage
     * @return INFO
     */
    @Transactional
    @PostMapping("/addMsg")
    public Result<String> addMsg(@RequestBody StuMessage stuMessage) {
        stuMessage.setMsgId(stuMessage.getTeaId() + "msg" + stuMessage.getStuId() + UUID.randomUUID());
        stuMessage.setMsgTime(new Date());
        boolean save = stuMessageService.save(stuMessage);
        if (save) {
            studentService.plusMsgNum(stuMessage.getStuId());
            return Result.success("发消息成功");
        }
        return Result.fail("发消息失败");
    }

    /**
     * 通过学生 id 获取消息列表
     * @param stuId stuId
     * @return List<StuMessage>
     */
    @GetMapping("/getMsgsByStuId")
    public Result<List<StuMessage>> getMsgsByStuId(String stuId) {
        List<StuMessage> msgList = stuMessageService.list(new LambdaQueryWrapper<StuMessage>().eq(StuMessage::getStuId, stuId).orderByDesc(StuMessage::getMsgTime));
        return Result.success(msgList);
    }

    /**
     * 通过 id 删除
     *
     * @param msgId msgId
     * @param stuId stuId
     * @return INFO
     */
    @DeleteMapping("/delById")
    public Result<String> delById(String msgId, String stuId) {
        boolean remove = stuMessageService.remove(new LambdaQueryWrapper<StuMessage>().eq(StuMessage::getMsgId, msgId));
        if (remove) {
            studentService.delMsgNum(stuId);
            return Result.success("删除消息成功");
        }
        return Result.fail("删除消息失败");
    }

    /**
     * 已读
     *
     * @param stuMessage stuMessage
     * @return INFO
     */
    @PutMapping("/hasRead")
    public Result<String> hasRead(@RequestBody StuMessage stuMessage) {
        stuMessageService.update(new LambdaUpdateWrapper<StuMessage>().set(StuMessage::getMsgState, "已读").eq(StuMessage::getStuId, stuMessage.getStuId()));
        return Result.success();
    }

    /**
     * 通过学生 id 获取未读消息数
     *
     * @param stuId stuId
     * @return 未读消息数
     */
    @GetMapping("/getStuUnreadNum")
    public Result<Long> getStuUnreadNum(String stuId) {
        long count = stuMessageService.count(new LambdaQueryWrapper<StuMessage>().eq(StuMessage::getMsgState, "未读").eq(StuMessage::getStuId, stuId));
        return Result.success(count);
    }
}
```
### 老师消息 TeaMessage

### Service

```java
/**
 * ITeaMessageService
 *
 * @author caobaoqi
 */
public interface ITeaMessageService extends IService<TeaMessage> {

    List<TeaMsgMap> getAllTeaMsg(String teaId, String stuName);

}
```

```java
/**
 * TeaMessageServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class TeaMessageServiceImpl extends ServiceImpl<TeaMessageMapper, TeaMessage> implements ITeaMessageService {
    @Resource
    private TeaMessageMapper teaMessageMapper;

    @Override
    public List<TeaMsgMap> getAllTeaMsg(String teaId, String stuName) {
        return teaMessageMapper.getAllTeaMsg(teaId, stuName);
    }
}
```

### Mapper

```java
/**
 * TeaMessageMapper
 *
 * @author caobaoqi
 */
public interface TeaMessageMapper extends BaseMapper<TeaMessage> {
    List<TeaMsgMap> getAllTeaMsg(String teaId,String stuName);
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="jz.cbq.backend.mapper.TeaMessageMapper">

    <select id="getAllTeaMsg" resultMap="TeaMsgMap">
        SELECT a.*, b.stu_name
        FROM t_tea_message a
        INNER JOIN t_student b
        ON a.stu_id = b.stu_id
        WHERE a.tea_id = #{teaId}
        <if test="stuName!='' and stuName!=null">
            AND b.stu_name LIKE concat('%',#{stuName},'%')
        </if>
        ORDER BY msg_time DESC
    </select>
    <resultMap id="TeaMsgMap" type="jz.cbq.backend.entity.TeaMsgMap">
        <id property="msgId" column="msg_id"/>
        <id property="stuId" column="stu_id"/>
        <id property="teaId" column="tea_id"/>
        <id property="stuName" column="stu_name"/>
        <id property="msgContent" column="msg_content"/>
        <id property="msgTime" column="msg_time"/>
    </resultMap>
</mapper>
```
### Controller

```java
/**
 * TeaMessageController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("teaMessage")
public class TeaMessageController {
    @Resource
    private ITeaMessageService teaMessageService;

    /**
     * 通过老师 id 和学生姓名 获取消息列表
     *
     * @param teaId   teaId
     * @param stuName stuName
     * @return List<TeaMsgMap>
     */
    @GetMapping("/getMsgsByTeaId")
    public Result<List<TeaMsgMap>> getMsgsByTeaId(String teaId, @RequestParam(value = "stuName", required = false) String stuName) {
        List<TeaMsgMap> allTeaMsg = teaMessageService.getAllTeaMsg(teaId, stuName);
        return Result.success(allTeaMsg);
    }

    /**
     * 添加消息
     *
     * @param teaMessage teaMessage
     * @return INFO
     */
    @PostMapping("/addMsg")
    public Result<String> addMsg(@RequestBody TeaMessage teaMessage) {
        teaMessage.setMsgId(teaMessage.getTeaId() + "msg" + teaMessage.getStuId() + UUID.randomUUID());
        teaMessage.setMsgTime(new Date());
        boolean save = teaMessageService.save(teaMessage);
        if (save) {
            return Result.success("发消息成功");
        }
        return Result.fail("发消息失败");
    }

    /**
     * 根据 id 删除
     *
     * @param msgId msgId
     * @return INFO
     */
    @DeleteMapping("/delById")
    public Result<String> delById(String msgId) {
        boolean remove = teaMessageService.remove(new LambdaQueryWrapper<TeaMessage>().eq(TeaMessage::getMsgId, msgId));

        return remove ? Result.success("删除消息成功") : Result.fail("删除消息失败");
    }
}
```
## 登录模块 Login

### Service

```java
/**
 * ILoginService
 *
 * @author caobaoqi
 */
public interface ILoginService{
    /**
     * 退出登录
     * @param token token
     */
    void logout(String token);
}
```

```java
/**
 * LoginServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Async("taskExecutor")
    @Override
    public void logout(String token) {
        System.out.println("线程 --" + Thread.currentThread().getName() + " 执行异步任务：从 redis 中删除 token");
        redisTemplate.delete(token);
    }
}
```

### Controller

```java
/**
 * LoginController
 *
 * @author caobaoqi
 */
@RestController
public class LoginController {
    @Resource
    private IAdminService adminService;
    @Resource
    private IStudentService studentService;
    @Resource
    private ITeacherService teacherService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private ILoginService loginService;

    /**
     * 登录
     *
     * @param id       id
     * @param password password
     * @param state    state
     * @return HashMap<String, Object>
     */
    @PostMapping("/login")
    public Result<HashMap<String, Object>> login(String id, String password, String state) {
        HashMap<String, Object> data = new HashMap<>();
        String token = JWTUtils.createToken(id);
        data.put("token", token);
        data.put("state", state);
        switch (state) {
            case "管理员":
                Admin adminDatabase = adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getAdminId, id));
                if (null != adminDatabase && passwordEncoder.matches(password, adminDatabase.getAdminPwd())) {
                    redisTemplate.opsForValue().set(token, JSON.toJSONString(adminDatabase), 1, TimeUnit.DAYS);
                    data.put("user", adminDatabase);
                    return Result.success(data, state + "登录成功");
                }
                return Result.fail(state + "不存在或密码错误");
            case "学生": {
                Student stuDatabase = studentService.getOne(new LambdaQueryWrapper<Student>().eq(Student::getStuId, id));
                if (null != stuDatabase && passwordEncoder.matches(password, stuDatabase.getStuPwd())) {
                    redisTemplate.opsForValue().set(token, JSON.toJSONString(stuDatabase), 1, TimeUnit.DAYS);
                    data.put("user", stuDatabase);
                    return Result.success(data, state + "登录成功");
                }
                return Result.fail(state + "不存在或密码错误");
            }
            case "班主任": {
                Teacher teaDatabase = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getTeaId, id));
                if (null != teaDatabase && passwordEncoder.matches(password, teaDatabase.getTeaPwd())) {
                    redisTemplate.opsForValue().set(token, JSON.toJSONString(teaDatabase), 1, TimeUnit.DAYS);
                    data.put("user", teaDatabase);
                    return Result.success(data, state + "登录成功");
                }
                return Result.fail(state + "不存在或密码错误");
            }
        }
        return Result.fail("没有选择身份");
    }

    /**
     * 退出登录
     *
     * @param token token
     * @return INO
     */
    @GetMapping("/logout")
    public Result<String> adminLogout(@RequestHeader("Authorization") String token) {
        loginService.logout(token);
        return Result.success("退出登录成功");
    }
}
```

## 管理员模块 Admin

### Service

```java
/**
 * IAdminService
 *
 * @author caobaoqi
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 获取下一个 ID
     * @return id
     */
    String getNextId();
}
```

```java
/**
 * AdminServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Resource
    private AdminMapper adminMapper;

    /**
     * 获取下一个 id
     * @return id
     */
    @Override
    public String getNextId() {
        String nowYear = Calendar.getInstance().get(Calendar.YEAR) + "";
        String maxId = getMaxId();
        int nextNum;
        if (maxId != null) {
            String maxYear = maxId.substring(3, 7);
            if (nowYear.equals(maxYear)) {
                nextNum = Integer.parseInt(maxId.substring(7, 9)) + 1;
                return nowYear + String.format("%02d", nextNum);
            } else {
                return nowYear + "01";
            }
        }
        return nowYear + "01";
    }

    /**
     * 获取 MaxID
     * @return id
     */
    public String getMaxId() {
        return adminMapper.getMaxId();
    }
}
```

### Mapper

```java
/**
 * AdminMapper
 *
 * @author caobaoqi
 */
public interface AdminMapper extends BaseMapper<Admin> {
    @Select("SELECT admin_id FROM t_admin ORDER BY admin_id DESC LIMIT 1")
    String getMaxId();
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="jz.cbq.backend.mapper.AdminMapper">

</mapper>

```
### Controller

```java
/**
 * AdminController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Value("${cbq.admin-secret}")
    private String adminSecret;
    @Resource
    private IAdminService adminService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private IStudentService studentService;
    @Resource
    private ICourseService courseService;
    @Resource
    private IMajorService majorService;
    @Resource
    private IChooseCourseService chooseCourseService;
    @Resource
    private IScoreService scoreService;
    @Resource
    private ILoginService loginService;

    /**
     * 添加管理员
     *
     * @param admin       admin
     * @param adminSecret Secret
     * @return INFO
     */
    @PostMapping("/add")
    public Result<String> addAdmin(@RequestBody Admin admin, String adminSecret) {
        if (adminSecret.equals(this.adminSecret)) {

            admin.setAdminId(adminService.getNextId());
            admin.setAdminPwd(passwordEncoder.encode(admin.getAdminPwd()));
            admin.setCreateTime(new Date());

            boolean save = adminService.save(admin);

            return save ? Result.success("注册管理员成功,你的管理员账号为 " + admin.getAdminId()) : Result.fail("系统错误，注册管理员失败");
        } else {
            return Result.fail("管理员密钥错误");
        }
    }

    /**
     * 通过 id 获取 admin
     *
     * @param adminId id
     * @return Admin
     */
    @GetMapping("/getAdminById")
    public Result<Admin> getAdminById(String adminId) {
        if (StringUtils.isBlank(adminId)) {
            return Result.fail("获取管理员信息失败");
        }
        Admin adminDatabase = adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getAdminId, adminId));

        return adminDatabase != null ? Result.success(adminDatabase) : Result.fail("找不到指定 id 的管理员");
    }

    /**
     * 编辑管理员
     *
     * @param admin admin
     * @return INFO
     */
    @PutMapping("/editAdmin")
    public Result<String> editAdmin(@RequestBody Admin admin) {
        admin.setAdminPwd(passwordEncoder.encode(admin.getAdminPwd()));
        boolean update = adminService.update(admin, new LambdaQueryWrapper<Admin>().eq(Admin::getAdminId, admin.getAdminId()));

        return update ? Result.success("更新个人信息成功") : Result.fail("更新个人信息失败");
    }

    /**
     * 删除管理员
     *
     * @param token   token
     * @param adminId adminId
     * @return INFO
     */
    @DeleteMapping("/delAdmin")
    public Result<String> delAdmin(@RequestHeader("Authorization") String token, String adminId) {
        boolean removeById = adminService.removeById(adminId);
        if (removeById) {
            loginService.logout(token);
            return Result.success("注销账号成功");
        }
        return Result.fail("注销账号失败");
    }

    /**
     * 一键选课
     *
     * @return INFO
     */
    @Transactional
    @GetMapping("/adminChooseCourse")
    public Result<String> adminChooseCourse() {
        Long start = System.currentTimeMillis();
        List<Major> majorList = majorService.list();

        int nowYear = LocalDate.now().getYear() + 1900;
        int nowMonth = LocalDate.now().getMonthValue() + 1;


        if (nowMonth >= 3 && nowMonth < 9) {
            for (Major major : majorList) {
                List<Student> studentList42 = studentService.studentList42(nowYear, major.getMajorId());//大四下
                List<Student> studentList32 = studentService.studentList32(nowYear, major.getMajorId());//大三下
                List<Student> studentList22 = studentService.studentList22(nowYear, major.getMajorId());//大二下
                List<Student> studentList12 = studentService.studentList12(nowYear, major.getMajorId());//大一下
                List<Course> courseList42Must = courseService.courseList42Must(major.getMajorId());
                List<Course> courseList32Must = courseService.courseList32Must(major.getMajorId());
                List<Course> courseList22Must = courseService.courseList22Must(major.getMajorId());
                List<Course> courseList12Must = courseService.courseList12Must(major.getMajorId());

                listChooseCourse(studentList42, courseList42Must);
                listChooseCourse(studentList32, courseList32Must);
                listChooseCourse(studentList22, courseList22Must);
                listChooseCourse(studentList12, courseList12Must);
            }
        } else {
            for (Major major : majorList) {
                List<Student> studentList51 = studentService.studentList51(nowYear, major.getMajorId());
                List<Student> studentList41 = studentService.studentList41(nowYear, major.getMajorId());//大四上
                List<Student> studentList31 = studentService.studentList31(nowYear, major.getMajorId());//大三上
                List<Student> studentList21 = studentService.studentList21(nowYear, major.getMajorId());//大二上
                List<Student> studentList11 = studentService.studentList11(nowYear, major.getMajorId());//大一上

                List<Course> courseList51Must = courseService.courseList51Must(major.getMajorId());
                List<Course> courseList41Must = courseService.courseList41Must(major.getMajorId());
                List<Course> courseList31Must = courseService.courseList31Must(major.getMajorId());
                List<Course> courseList21Must = courseService.courseList21Must(major.getMajorId());
                List<Course> courseList11Must = courseService.courseList11Must(major.getMajorId());

                listChooseCourse(studentList51, courseList51Must);
                listChooseCourse(studentList41, courseList41Must);
                listChooseCourse(studentList31, courseList31Must);
                listChooseCourse(studentList21, courseList21Must);
                listChooseCourse(studentList11, courseList11Must);
            }
        }
        Long end = System.currentTimeMillis();
        return Result.success("一键选课完毕,服务器花费时间" + (end - start) + "ms");
    }


    public void listChooseCourse(List<Student> studentList, List<Course> courseList) {
        ChooseCourse chooseCourse = new ChooseCourse();
        for (Student student : studentList) {
            for (Course course : courseList) {
                chooseCourse.setChooseCourseId(student.getStuId() + "choosecourse" + course.getCourseId().substring(6, 10));
                LambdaQueryWrapper<ChooseCourse> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ChooseCourse::getChooseCourseId, chooseCourse.getChooseCourseId());
                ChooseCourse one = chooseCourseService.getOne(queryWrapper);
                if (null != one) {
                    continue;
                }
                chooseCourse.setStuId(student.getStuId());
                chooseCourse.setCourseId(course.getCourseId());
                chooseCourse.setCreateTime(new Date());
                chooseCourse.setState(1);
                chooseCourse.setStuName(student.getStuName());
                chooseCourse.setCourseName(course.getCourseName());
                chooseCourse.setMajorName(student.getMajorName());
                chooseCourse.setCoursePeriod(course.getCoursePeriod());
                chooseCourse.setIfDegree(course.getIfDegree());
                chooseCourseService.save(chooseCourse);//有事务，不怕部分成功
                courseService.updateStuChooseNum(course.getCourseId());
            }
        }
    }

    @Transactional
    @GetMapping("/adminScoreRandomly")
    public Result<String> adminScoreRandomly() {
        Long start = System.currentTimeMillis();
        Random random = new Random();
        Score score = new Score();
        List<ChooseCourse> chooseCourseList = chooseCourseService.list();//select * from t_choose_course
        for (ChooseCourse chooseCourse : chooseCourseList) {
            score.setScoreId(chooseCourse.getStuId() + "-" + chooseCourse.getChooseCourseId());
            LambdaQueryWrapper<Score> scoreQueryWrapper = new LambdaQueryWrapper<>();
            scoreQueryWrapper.eq(Score::getScoreId, score.getScoreId());
            Score scoreDatabase = scoreService.getOne(scoreQueryWrapper);
            if (null != scoreDatabase)//针对此选课记录打过分
            {
                continue;
            }
            score.setChooseCourseId(chooseCourse.getChooseCourseId());
            score.setStuId(chooseCourse.getStuId());
            score.setCourseName(chooseCourse.getCourseName());
            score.setStuName(chooseCourse.getStuName());
            score.setCreateTime(new Date());
            score.setScore(random.nextInt(101) + 50);
            scoreService.save(score);
        }
        Long end = System.currentTimeMillis();
        return Result.success("一键随机打分完毕,服务器花费时间" + (end - start) + "ms");
    }


}
```

## X. 开发环境

```yaml
version: '3.1'
services:
  mysql:
    container_name: mysql-3306
    image: mysql:8.0
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: mysql-homework
    ports:
      - "3306:3306"
  redis:
    container_name: redis-6379
    image: redis
    restart: always
    ports:
      - "6379:6379"

```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209202913434.png" alt="image-20231209202913434" style="zoom:67%;" />

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209202932472.png" alt="image-20231209202932472" style="zoom:67%;" />

## X. 部署

### Yaml

```yaml
server:
  port: 8091
spring:
  mvc:
    path match:
      matching-strategy: ant_path_matcher
  redis:
    host: YOUR_HOST
    port: 6379
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: YOUR_URL
    username: YOUR_USERNAME
    password: YOUR_PASSWORD
    druid:
      initial-size: 5
      max-active: 20
      max-wait: 60000
      min-idle: 1
      validation-query: select 1
  devtools:
    restart:
      enabled: true
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  flyway:
    baseline-description:
    baseline-version: 20231209001
    baseline-on-migrate: true
    default-schema: mysql-homework
mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: jz.cbq.backend.entity
  configuration:
    map-underscore-to-camel-case: true
#    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-not-delete-value: 0
      logic-delete-value: 1
      table-prefix: t_
logging:
  level:
    jz:
      cbq:
        backend:
          mapper: INFO
cbq:
  admin-secret: TODO
  jwt-secret: TODO
```

### Docker

```dockerfile
FROM eclipse/centos_jdk8
LABEL authors="caobaoqi"


COPY target/backend-0.0.1-SNAPSHOT.jar .

CMD java -jar backend-0.0.1-SNAPSHOT.jar

EXPOSE 8091

```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209202959539.png" alt="image-20231209202959539" style="zoom:67%;" />

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231210171858827.png" alt="image-20231210171858827" style="zoom:67%;" />

## X. 依赖 .xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.10</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <!--INFO-->
    <groupId>jz.cbq</groupId>
    <artifactId>backend</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <url>https://gitee.com/cola777jz/mysql-homework</url>
    <description>MySQL Homework</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!--stater-redis-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!--stater-web-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--Security + JWT-->
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-core</artifactId>
            <version>5.6.2</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.1</version>
        </dependency>
        <!--Flyway-->
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-mysql</artifactId>
        </dependency>
        <!--DB-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.2.6</version>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!--MP-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.1</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.5.1</version>
        </dependency>
        <!--Swagger2-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.9.2</version>
        </dependency>
        <!--Freemarker-->
        <dependency>
            <groupId>org.freemarker</groupId>
            <artifactId>freemarker</artifactId>
            <version>2.3.28</version>
        </dependency>
        <!--Bootstarp-UI-->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>swagger-bootstrap-ui</artifactId>
            <version>1.9.6</version>
        </dependency>
        <!--FastJson-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>2.0.17</version>
        </dependency>
        <!--Ngrok-->
        <dependency>
            <groupId>io.github.kilmajster</groupId>
            <artifactId>ngrok-spring-boot-starter</artifactId>
            <version>0.6.0</version>
        </dependency>
        <!--Lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!--DevTools-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <!--Test-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```

## X. Entity 实体

### Admin 管理员
```java
/**
 * 管理员 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "管理员")
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 管理员 id
     */
    @TableId(value = "admin_id")
    private String adminId;
    /**
     * 管理员名称
     */
    private String adminName;
    /**
     * 管理员密码
     */
    private String adminPwd;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;
}
```
### Major 专业

```java
/**
 * 专业 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "专业")
public class Major implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 专业 id
     */
    @TableId(value = "major_id")
    private String majorId;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 班级数量
     */
    private Integer classTotal;
    /**
     * 课程数量
     */
    private Integer courseTotal;
    /**
     * 学生数量
     */
    private Integer stuTotal;
    /**
     * 老师数量
     */
    private Integer teaTotal;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;
}
```
### Class 班级

```java
/**
 * 班级 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "班级")
public class Class implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 班级 id
     */
    @TableId(value = "class_id")
    private String classId;
    /**
     * 年级
     */
    private String classYear;
    /**
     * 专业 id
     */
    private String majorId;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 老师名称
     */
    private String teaName;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 学生数量
     */
    private Integer stuTotal;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;

}
```
### Teacher 老师
```java
/**
 * 老师 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "老师")
public class Teacher implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 老师 id
     */
    @TableId(value = "tea_id")
    private String teaId;
    /**
     * 专业 id
     */
    private String majorId;
    /**
     * 年级
     */
    private String classYear;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 老师名称
     */
    private String teaName;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 老师密码
     */
    private String teaPwd;
    /**
     * 班级 id
     */
    private String classId;
    /**
     * 老师 IDCard
     */
    private String teaIdCard;
    /**
     * 班级编号
     */
    private String classNo;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;
}
```
### Student 学生
```java
/**
 * 学生 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "学生")
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 学生 id
     */
    @TableId(value = "stu_id")
    private String stuId;
    /**
     * 老师 id
     */
    private String teaId;
    /**
     * 专业 id
     */
    private String majorId;
    /**
     * 班级 id
     */
    private String classId;
    /**
     * 学生名称
     */
    private String stuName;
    /**
     * 年级
     */
    private String admissionYear;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 学生 IDCard
     */
    private String stuIdCard;
    /**
     * 学生密码
     */
    private String stuPwd;
    /**
     * 班级编号
     */
    private String classNo;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 老师名称
     */
    private String teaName;
    /**
     * 信息数量
     */
    private String messageNum;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;
}
```
### Course 课程
```java
/**
 * 课程 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "课程")
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 课程 id
     */
    @TableId(value = "course_id")
    private String courseId;
    /**
     * 专业 id
     */
    private String majorId;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 是否选修
     */
    private String ifDegree;
    /**
     * 课程时间
     */
    private String coursePeriod;
    /**
     * 选择该课程的学生数量
     */
    private Integer stuChooseNum;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
	private Integer deleted;
}

```
### ChooseCourse 选课
```java
/**
 * 选课 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "选课")
public class ChooseCourse {
    /**
     * 选课 id
     */
    @TableId(value = "choose_course_id")
    private String ChooseCourseId;
    /**
     * 学生 id
     */
    private String stuId;
    /**
     * 课程 id
     */
    private String CourseId;
    /**
     * 创建时间
     */
    private Date CreateTime;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 学生名称
     */
    private String stuName;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 课程时间
     */
    private String coursePeriod;
    /**
     * 是否选修
     */
    private String ifDegree;
    /**
     * 是否删除
     */
	private Integer deleted;  
}
```
### Score 成绩
```java
/**
 * 成绩 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "成绩")
public class Score implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 成绩 id
     */
    @TableId(value = "score_id")
    private String scoreId;
    /**
     * 课程 id
     */
    private String ChooseCourseId;
    /**
     * 学生 id
     */
    private String stuId;
    /**
     * 成绩
     */
    private Integer score;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 学生名称
     */
    private String stuName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;

}
```
### StuMessage 学生消息
```java
/**
 * 学生信息 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "学生信息")
public class StuMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 信息 id
     */
    @TableId(value = "msg_id")
    private String msgId;
    /**
     * 学生 id
     */
    private String stuId;
    /**
     * 老师 id
     */
    private String teaId;
    /**
     * 信息内容
     */
    private String msgContent;
    /**
     * 消息时间
     */
    private Date msgTime;
    /**
     * 消息状态(已读|未读)
     */
    private String msgState;
    /**
     * 是否删除
     */
    private Integer deleted;

}
```
### TeaMessage 老师消息
```java
/**
 * 老师信息 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "老师信息")
public class TeaMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 消息 id
     */
    @TableId(value = "msg_id")
    private String msgId;
    /**
     * 学生 id
     */
    private String stuId;
    /**
     * 老师 id
     */
    private String teaId;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 消息时间
     */
    private Date msgTime;
    /**
     * 是否删除
     */
    private Integer deleted;

}
```

### StuChooseCourseMap

```java
@Data
public class StuChooseCourseMap {
    /**
     * 选课 id
     */
    private String chooseCourseId;
    /**
     * 课程 id
     */
    private String courseId;
    /**
     * 专业 id
     */
    private String majorId;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 是否必修
     */
    private String ifDegree;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 课程时间
     */
    private String coursePeriod;
    /**
     * 学生数
     */
    private String stuChooseNum;
    /**
     * 学生 id
     */
    private String stuId;
    /**
     * 状态
     */
    private String state;
    /**
     * 学生名称
     */
    private String stuName;
}
```

### StuScoreMap

```java
@Data
public class StuScoreMap {
    /**
     * 学生 id
     */
    private String stuId;
    /**
     * 学生名称
     */
    private String stuName;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 课程成绩
     */
    private List<Integer> courseScore;
    /**
     * 课程数量
     */
    private Integer scoreTotal;
}
```


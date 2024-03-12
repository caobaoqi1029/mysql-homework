package jz.cbq.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jz.cbq.backend.vo.Result;
import jz.cbq.backend.entity.Admin;
import jz.cbq.backend.entity.Student;
import jz.cbq.backend.entity.Teacher;
import jz.cbq.backend.service.IAdminService;
import jz.cbq.backend.service.ILoginService;
import jz.cbq.backend.service.IStudentService;
import jz.cbq.backend.service.ITeacherService;
import jz.cbq.backend.utils.JWTUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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

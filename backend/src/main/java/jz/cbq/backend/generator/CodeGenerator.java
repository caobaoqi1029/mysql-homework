package jz.cbq.backend.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 代码生成器
 *
 * @author caobaoqi
 */
public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql:///mysql-homework?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=GMT%2b8";
        String username = "root";
        String password = "root";
        String mapperLocation = "D:\\jz-cbq\\Projects\\IDEA\\mysql-homework\\backend\\src\\main\\resources\\mapper\\";
        String[] tables = {"t_admin", "t_course", "t_message", "t_major", "t_score", "t_student", "t_teacher", "t_class"};

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder.author("CaoBaoQi")
                        .enableSwagger()
                        .outputDir("D:\\jz-cbq\\Projects\\IDEA\\mysql-homework\\backend\\src\\main\\java"))
                .packageConfig(builder -> builder.parent("jz.cbq.backend")
                        .pathInfo(Collections.singletonMap(OutputFile.mapperXml, mapperLocation)))
                .strategyConfig(builder -> builder.addInclude(tables)
                        .addTablePrefix("t_"))
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}

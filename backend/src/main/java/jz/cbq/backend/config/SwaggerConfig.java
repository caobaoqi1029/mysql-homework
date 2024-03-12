package jz.cbq.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

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

package jz.cbq.bookdemobackend;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy
@MapperScan("jz.cbq.bookdemobackend.mapper")
public class BookDemoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookDemoBackendApplication.class, args);
        log.info("BookDemoBackendApplication run success");
    }

}

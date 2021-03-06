package cn.demo.beike;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
@Order(12)//顺序注解
public class MyApplicationRunner implements ApplicationRunner {

    /**
     * 在spring boot启动完毕之后，会自动运行run方法
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("启动完成。。。");
    }
}

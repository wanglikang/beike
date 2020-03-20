package cn.demo.beike;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
@MapperScan(basePackages = {"cn.demo.beike.entity.dao"})
public class DemoApplication {
    public static void main(String[] args) {
//        System.out.println("args:"+ Strings.join( Arrays.asList(args), ','));
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
    }


    /**
     * 以下两个是将http请求自动转换为https请求的
     * @return
     */

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector){
        TomcatServletWebServerFactory tomcat=new TomcatServletWebServerFactory(){
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint=new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection=new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }

    @Bean
    public Connector connector(){
        System.out.println("创建了自定义的http->https的connector");
        Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8088);
        connector.setSecure(false);
        connector.setRedirectPort(443);
        return connector;
    }
}

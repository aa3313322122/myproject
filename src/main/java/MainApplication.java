import com.alibaba.druid.pool.DruidDataSource;
import com.naruto.kafka.TestKafkaProducer;
import com.sun.tools.extcheck.Main;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Created by yue.yuan on 2017/4/11.
 */
@Configuration()
@ComponentScan(basePackages = {"com.naruto.*"})
@EnableAutoConfiguration
@EnableAsync
@EnableKafka
@ImportResource({"classpath:spring/application-profile.xml"})
public class MainApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        System.out.println("main start...");
        SpringApplication springApplication = new SpringApplication(MainApplication.class);
        springApplication.run(args);
    }
}

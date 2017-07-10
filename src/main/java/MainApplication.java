import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by yue.yuan on 2017/4/11.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.naruto.*"})
@EnableAsync
@EnableKafka
public class MainApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        System.out.println("main start...");
        SpringApplication springApplication = new SpringApplication(MainApplication.class);
        springApplication.run(args);
    }
}

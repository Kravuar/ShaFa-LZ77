package net.kravuar.shafalz77;

import net.kravuar.shafalz77.application.props.AppProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
@EnableConfigurationProperties(AppProps.class)
public class ShaFaLZ77Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ShaFaLZ77Application.class);
    }
    public static void main(String[] args) {
         SpringApplication.run(ShaFaLZ77Application.class, args);
    }
}

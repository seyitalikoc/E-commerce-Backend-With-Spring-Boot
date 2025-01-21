package com.seyitkoc.starter;

import com.seyitkoc.config.GlobalProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.seyitkoc.entity"})
@ComponentScan(basePackages = {"com.seyitkoc"})
@EnableJpaRepositories(basePackages = {"com.seyitkoc.repository"})
@EnableConfigurationProperties(value = GlobalProperties.class)
public class SpringProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProjectApplication.class, args);
	}

}

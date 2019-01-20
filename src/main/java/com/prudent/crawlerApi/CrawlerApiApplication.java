package com.prudent.crawlerApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class CrawlerApiApplication extends SpringBootServletInitializer {
	   String test;
	   @Override
	   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	      return application.sources(CrawlerApiApplication.class);
	   }

	public static void main(String[] args) {
		SpringApplication.run(CrawlerApiApplication.class, args);
	}
}

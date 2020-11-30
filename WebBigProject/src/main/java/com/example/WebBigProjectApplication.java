package com.example;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.Login.CouponExpirationDailyJob;

@SpringBootApplication

public class WebBigProjectApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx=SpringApplication.run(WebBigProjectApplication.class, args);
		CouponExpirationDailyJob t1 = ctx.getBean(CouponExpirationDailyJob.class);
		t1.start();

		
	}

}

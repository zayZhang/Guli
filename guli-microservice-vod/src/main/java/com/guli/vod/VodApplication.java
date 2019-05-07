package com.guli.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author helen
 * @since 2019/4/26
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@ComponentScan(basePackages={"com.guli.vod","com.guli.common"})
public class VodApplication {

	public static void main(String[] args) {
		SpringApplication.run(VodApplication.class, args);
	}
}
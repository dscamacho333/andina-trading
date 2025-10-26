package co.edu.unbosque.microservice_investor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class MicroserviceInvestorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceInvestorApplication.class, args);
	}

}

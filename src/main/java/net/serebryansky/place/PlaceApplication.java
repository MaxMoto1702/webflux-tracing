package net.serebryansky.place;

import net.serebryansky.common.filter.RequestIdFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlaceApplication {

	@Bean
	public RequestIdFilter requestIdFilter() {
		return new RequestIdFilter();
	}

	public static void main(String[] args) {
		SpringApplication.run(PlaceApplication.class, args);
	}

}

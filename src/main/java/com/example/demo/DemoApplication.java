package com.example.demo;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.spring.embedded.provider.SpringEmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableCaching
public class DemoApplication {

	@Autowired
	SpringEmbeddedCacheManager cacheManager;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@RequestMapping("/")
	public String hello() {
		return "Hello World";
	}

	@RequestMapping("/catch")
	public String catchT() {
		cacheManager.getCacheNames().iterator().forEachRemaining(c -> System.out.println(c));
		return "Hello World";
	}

	@Bean
	public SpringEmbeddedCacheManager cacheManager() throws Exception {
		Configuration config = new ConfigurationBuilder().eviction().strategy(EvictionStrategy.LRU).maxEntries(150)
				.build();

		return new SpringEmbeddedCacheManager(new DefaultCacheManager(config));
	}

}

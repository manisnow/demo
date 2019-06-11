package com.example.demo;

import java.util.Properties;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableCaching
public class DemoApplication {

	@Autowired
	CachePlayground cachePlayground;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

	@RequestMapping("/")
	public String hello() {
		return "Hello World1";
	}

	@RequestMapping("/catch")
	public String catchT() {
		cachePlayground.add("Infinispan", "Is cool!");
		System.out.println(cachePlayground.getContent("Infinispan"));
		return cachePlayground.getContent("Infinispan");
	}

	@Bean
	RemoteCacheManager remoteCacheManager() {

		Properties properties = new Properties();
		properties.setProperty(ConfigurationProperties.SERVER_LIST, "datagrid-app-hotrod.sbx-992325.svc:11333");

		return new RemoteCacheManager(properties);
	}
	/*
	 * @Bean public SpringEmbeddedCacheManagerFactoryBean springCache() { return new
	 * SpringEmbeddedCacheManagerFactoryBean(); }
	 */

	@Bean
	public CachePlayground playground() {
		return new CachePlayground();
	}

	public static class CachePlayground {

		@Autowired
		private RemoteCacheManager remoteCacheManager;

		public void add(String key, String value) {
			remoteCacheManager.getCache("default").put(key, value);
		}

		public String getContent(String key) {
			return (String) remoteCacheManager.getCache("default").get(key);
		}
	}

}

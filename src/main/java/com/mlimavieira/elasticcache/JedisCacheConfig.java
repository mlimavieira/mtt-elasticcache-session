package com.mlimavieira.elasticcache;

import java.lang.Long;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

//@EnableCaching
//@Configuration
//@Profile({ "local" })
public class JedisCacheConfig extends CachingConfigurerSupport {

	@Value("${custom.spring.cache.hostName}")
	private String cacheHostName;

	@Value("${custom.spring.cache.port}")
	private int cachePort;

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
		redisConnectionFactory.setHostName(cacheHostName);
		redisConnectionFactory.setPort(cachePort);
		return redisConnectionFactory;
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
		redisTemplate.setConnectionFactory(cf);
		return redisTemplate;
	}

	@Bean
	public CacheManager cacheManager(RedisTemplate<String, String> redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(30);
		Map<String, Long> expirationConfig = new HashMap<>();
		// TTL 60 seconds
		expirationConfig.put("listAirports", 60L);
		expirationConfig.put("airportCity", 60L);
		expirationConfig.put("airportCountry", 60L);
		expirationConfig.put("airportCountries", 60L);
		expirationConfig.put("airportCities", 60L);

		cacheManager.setExpires(expirationConfig);

		return cacheManager;
	}
}

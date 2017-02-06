package com.mlimavieira.elasticcache;

import java.util.HashMap;
import java.util.Map;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class RedissonCacheConfig extends CachingConfigurerSupport {
	private static final Logger LOG = LoggerFactory.getLogger(RedissonCacheConfig.class);

	@Value("${custom.spring.cache.address}")
	private String cacheAddress;

	private RedissonClient redisson() {
		LOG.info("Startin Redisson Client CacheAddress: {}", cacheAddress);
		Config config = new Config();
		config.useSingleServer().setAddress(cacheAddress);
		return Redisson.create(config);
	}

	@Bean
	@Override
	public CacheManager cacheManager() {
		Map<String, CacheConfig> config = new HashMap<>();

		// TTL 5 minutes and MaxIdleTime 10 minutes
		config.put("airport", new CacheConfig(6000 * 5, 6000*10));
		
		
		// TTL 30 seconds and MaxIdleTime 60 seconds
		config.put("listAirports", new CacheConfig(30000L, 60000));
		
		
		config.put("airportCity", new CacheConfig(30000L, 60000));
		config.put("airportCities", new CacheConfig(30000L, 60000));
		
		config.put("airportCountry", new CacheConfig(30000L, 60000));
		config.put("airportCountries", new CacheConfig(30000L, 60000));
		
		
		RedissonSpringCacheManager redissonSpringCacheManager = new RedissonSpringCacheManager(redisson(), config);

		return redissonSpringCacheManager;
	}
}



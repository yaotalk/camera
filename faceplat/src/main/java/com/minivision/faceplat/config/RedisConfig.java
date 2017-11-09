package com.minivision.faceplat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.minivision.faceplat.entity.Face;
import com.minivision.faceplat.repository.redis.FutureSerializer;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;


@Configuration
public class RedisConfig {

  @Autowired
  private CacheManager cacheManager;

  @Bean
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
    StringRedisTemplate template = new StringRedisTemplate(factory);
    return template;
  }
  
  
  @Bean
  public RedisTemplate<String, Face> faceTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Face> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    Jackson2JsonRedisSerializer<Face> serializer = new Jackson2JsonRedisSerializer<>(Face.class);
    template.setValueSerializer(serializer);
    return template;
  }
  
  @Bean
  public RedisTemplate<String, float[]> futureTemplate(RedisConnectionFactory factory){
    RedisTemplate<String, float[]> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    FutureSerializer serializer = new FutureSerializer();
    template.setValueSerializer(serializer);
    template.setKeySerializer(new StringRedisSerializer());
    template.setDefaultSerializer(serializer);
    return template;
  }
  

  @Bean
  public Cache faceCache() {
    return cacheManager.getCache("face");
  }

}

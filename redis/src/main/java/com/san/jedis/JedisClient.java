package com.san.jedis;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisClient {
	
	/**
	 * 单个版
	 */
	@Test
	public void jedisClient(){
		Jedis jedis = new Jedis("101.132.158.84",6379);
		//通过set赋值
		jedis.set("s3","3333");
		//jedis.select("");选择数据库
		//通过get取值
		String result = jedis.get("s3");
		System.out.println(result);
		//关闭jedis
		jedis.close();
	}
	
	/**
	 * 连接池
	 */
	@Test
	public void jedisPool(){
		JedisPool pool = new JedisPool("101.132.158.84",6379);
		//通过连接池获取jedis对象
		Jedis jedis = pool.getResource();
		jedis.set("s4", "444");
		String result = jedis.get("s4");
		System.out.println(result);
		//关闭
		jedis.close();
		pool.close();
	}
	
}

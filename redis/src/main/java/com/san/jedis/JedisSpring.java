package com.san.jedis;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisSpring {
	
	/**
	 * redis整合spring
	 */
	@Test
	public void JedisPool(){
		String xmlPath="applicationContext.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
		JedisPool pool = (JedisPool) applicationContext.getBean("jedisPool");
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			jedis.set("name", "张三");
			String name=jedis.get("name");
			System.out.println(name);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis!=null){
				//关闭连接
				jedis.close();
			}
		}
		
	}
}

package com.hxc.upload.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * @Description 
 * 2019年3月17日  下午12:08:46
 * @Author Huangxiaocong
 */
public class JedisDemo {
	
	public static void main(String[] args) {
		testSingleJedis();
		//testJedisPool();
	}
	
	public static void testJedisPool() {
		//1.获得连接池配置对象，设置配置项
		JedisPoolConfig config = new JedisPoolConfig();
		//1.1 设置最大连接数
		config.setMaxTotal(30);
		//1.2 最大空闲数
		config.setMaxIdle(10);
		
		//2.获得连接池
		JedisPool jedisPool = new JedisPool(config, "192.168.230.152", 6379);
		
		//3.获得核心对象
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			//设置数据
			jedis.set("name", "Huang Xiaocong");
			//获得数据
			String name = jedis.get("name");
			System.out.println(name);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(jedis != null) {
				jedis.close();
			}
			//虚拟机关闭时，释放pool资源
			if(jedisPool != null) {
				jedisPool.close();
			}
		}
		
	}
	
	/**
	 * 单实例连接 
	 * @Author Huangxiaocong 2019年3月17日 下午12:22:28
	 */
	public static void testSingleJedis() {
		//1.设置ip地址和端口
		Jedis jedis = new Jedis("192.168.230.152", 6379);
		//2.获取数据
		String string = jedis.get("username");
		jedis.set("age", "22");
		jedis.hset("name", "sys_name", "platform");
		System.out.println(jedis.hget("name", "sys_name"));
		//jedis.hmset(key, hash)
		try {
			System.out.println(jedis.get("age"));
			
			System.out.println(jedis.get("username"));
			System.out.println(jedis.incrBy("age", 10));
			System.out.println(jedis.decrBy("age", 5));
		} catch (JedisDataException e) {
			System.out.println("数据出错啦，小伙子" + e);
		}
		
		
		jedis.close();
	}
	
	
}

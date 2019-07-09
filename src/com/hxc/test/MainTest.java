package com.hxc.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainTest {
	
	public static void main(String[] args) throws Exception {
		System.out.println("hahha devolpment");
		long curr = System.currentTimeMillis();
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = "2019-04-30 16:55:00";
//		long ll = new Date(2019, 4, 30).getTime();
		if(curr > sdfs.parse(time).getTime()) {
			System.out.println(curr + "=== " + time + " === " + sdfs.parse(time).getTime());
			System.out.println(curr + " ===sss " + time + " === " + sdfs.parse(time).getTime());
		} else {
			System.out.println(66);
		}
		/**
		 * 针对网络通信的不同层次，Java提供了不同的API，其提供的网络功能有四大类:
		 * InetAddress:用于标识网络上的硬件资源，主要是IP地址
		 * URL：统一资源定位符，通过URL直接读取或写入网络上的数据
		 * Sockets：使用TCP协议实现的网络通信Socket相关的类
		 * Datagram:使用UDP协议，将数据保存在用户数据报中，通过网络进行通信
		 * 
		 */
		System.exit(0);
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
		
		RnTest rTest = new RnTest();
		
		//ses.schedule(rTest, delay, unit);
		
		//initialDelay 初始化延时	period：两次开始执行最小间隔时间	 unit：计时单位
		//ses.scheduleAtFixedRate(command, initialDelay, period, unit);
		ses.scheduleAtFixedRate(rTest, 5, 1, TimeUnit.SECONDS);
		//initialDelay:初始化延时， delay：前一次执行结束到下一次执行开始的间隔时间 	unit：计时单位
		ses.scheduleWithFixedDelay(rTest, 2, 5, TimeUnit.SECONDS);
		//周期定时执行某个任务。在指定的时间点执行任务：如凌晨2点
		long onDay = 24 * 60 * 60 * 1000;
		long initDelay = getTimeMillis("20:00:00") - System.currentTimeMillis();
		initDelay = initDelay > 0 ? initDelay : onDay + initDelay;
		ses.scheduleAtFixedRate(rTest, initDelay, onDay, TimeUnit.MILLISECONDS);
		
		//获取指定时间对应的毫秒数
		
	}
	private static long getTimeMillis(String time) {
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
		Date curDate = null;
		try {
			curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return curDate.getTime();
		
	}
	
	
	
	
	
	
	
}

class RnTest implements Runnable {

	@Override
	public void run() {
		System.out.println("我是线程" + new Date());
	}
	
	
}
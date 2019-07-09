package com.hxc.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTest {

	public static void main(String[] args) {
		Socket client = null;
		ServerSocket server = null;
		try {
			server = new ServerSocket(9090);
			client = server.accept();
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			int count = 1;
			boolean flag = true;
			
			String line = "";
			while(flag) {
				System.out.println("这是第" + count + "次数据" );
				count ++;
				line = reader.readLine();
				if(line.equals("exit")) {
					flag = false;
					System.out.println("客户端说不想玩了" + line);
				} else {
					System.out.println("客户端说：" + line);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}

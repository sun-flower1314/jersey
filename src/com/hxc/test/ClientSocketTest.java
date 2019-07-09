package com.hxc.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ClientSocketTest {

	public static void main(String[] args) {
		try {
			Socket client = new Socket("127.0.0.1", 9090);
			PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
			Scanner scanner = new Scanner(System.in);
			String words = "";
			while(scanner.hasNext()) {
				words = scanner.nextLine();
				printWriter.println(words);
				System.out.println("向服务端发送了数据" + words);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

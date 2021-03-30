package com.example.demoProject;

import com.example.demoProject.service.Handler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
public class DemoProjectApplication extends Thread {

	public static void main(String[] args) {
		SpringApplication.run(DemoProjectApplication.class, args);
		(new DemoProjectApplication()).run();
	}
	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(9999)) {
			Socket socket;
			try {
				while ((socket = serverSocket.accept()) != null) {
					(new Handler(socket)).start();
				}
			} catch (IOException e) {
				e.printStackTrace();  // TODO: implement catch
			}
		} catch (IOException e) {
			e.printStackTrace();  // TODO: implement catch
			return;
		}
	}
}

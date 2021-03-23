package com.proxyserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.proxyserver.service.HandlerService;

/**
 * 
 * @author nhut.to
 *
 */
public class ProxyServer extends Thread {

	final static Logger LOGGER = Logger.getLogger(ProxyServer.class);
	final static int PORT = 9999;
	private ServerSocket serverSocket;

	public static void main(String[] args) {
		(new ProxyServer()).run();
	}

	public ProxyServer() {
		super("Server Thread");
	}

	@Override
	public void run() {
		LOGGER.info("Proxy is ready");
		try {
			serverSocket = new ServerSocket(PORT);
			Socket socket;
			while ((socket = serverSocket.accept()) != null) {
				(new HandlerService(socket)).start();
			}
		} catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage() + e.getStackTrace());
			return;
		}
	}
}
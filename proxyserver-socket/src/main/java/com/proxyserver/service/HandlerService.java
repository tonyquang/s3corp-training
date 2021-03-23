package com.proxyserver.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.proxyserver.ProxyServer;
import com.proxyserver.kafka.producers.ProducerCreator;

public class HandlerService extends Thread {

	final static Logger LOGGER = Logger.getLogger(ProxyServer.class);
	private final String MSG502 = " 502 Bad Gateway";
	private final String MSG200 = " 200 Connection established";
	private final String ISO_FORMAT = "ISO-8859-1";

	public static final Pattern CONNECT_PATTERN = Pattern.compile("CONNECT (.+):(.+) HTTP/(1\\.[01])",
			Pattern.CASE_INSENSITIVE);

	private final Socket clientSocket;
	private boolean previousWasR = false;
	private ProducerCreator producerCreator = new ProducerCreator();
	private InetAddress addr;

	public HandlerService(Socket clientSocket) {
		this.clientSocket = clientSocket;
		setAddr(this.getInetAddress());
	}

	@Override
	public void run() {
		try {
			final String request = readLine(clientSocket);
			LOGGER.info("Request: " + request);

			if (request.contains("CONNECT")) {
				if (!request.contains(" api.") && !request.contains(" apis.") && !request.contains(" apps.")
						&& !request.contains(" azwcus1-client-s.") && !request.contains(" adservice.")) {
					String url = request.substring(request.indexOf(" "), request.indexOf(":"));

					final String msg = " {\"user_id\":\"" + addr.getHostName() + "\", \"url\":\"" + url + "\"} ";
					producerCreator.runProducer(msg);
					LOGGER.info("Message: " + msg);
				}
			}
			Matcher matcher = CONNECT_PATTERN.matcher(request);
			if (matcher.matches()) {
				String header;
				do {
					header = readLine(clientSocket);

				} while (!"".equals(header));
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream(),
						ISO_FORMAT);

				final Socket forwardSocket;
				try {
					forwardSocket = new Socket(matcher.group(1), Integer.parseInt(matcher.group(2)));
				} catch (IOException | NumberFormatException e) {
					LOGGER.error(e.getLocalizedMessage() + e.getStackTrace());
					writeInforStateProxy(outputStreamWriter, matcher, MSG502);
					return;
				}
				try {
					writeInforStateProxy(outputStreamWriter, matcher, MSG200);
					Thread remoteToClient = new Thread() {
						@Override
						public void run() {
							forwardData(forwardSocket, clientSocket);
						}
					};
					remoteToClient.start();
					try {
						if (previousWasR) {
							int read = clientSocket.getInputStream().read();
							if (read != -1) {
								if (read != '\n') {
									forwardSocket.getOutputStream().write(read);
								}
								forwardData(clientSocket, forwardSocket);
							} else {
								if (!forwardSocket.isOutputShutdown()) {
									forwardSocket.shutdownOutput();
								}
								if (!clientSocket.isInputShutdown()) {
									clientSocket.shutdownInput();
								}
							}
						} else {
							forwardData(clientSocket, forwardSocket);
						}
					} finally {
						try {
							remoteToClient.join();
						} catch (InterruptedException e) {
							LOGGER.error(e.getLocalizedMessage() + e.getStackTrace());
						}
					}
				} finally {
					forwardSocket.close();
				}
			}
		} catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage() + e.getStackTrace());
		} catch (ExecutionException e) {
			LOGGER.error(e.getLocalizedMessage() + e.getStackTrace());
		} catch (InterruptedException e) {
			LOGGER.error(e.getLocalizedMessage() + e.getStackTrace());
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				LOGGER.error(e.getLocalizedMessage() + e.getStackTrace());
			}
		}
	}

	/**
	 * 
	 * @param inputSocket
	 * @param outputSocket
	 */
	private void forwardData(Socket inputSocket, Socket outputSocket) {
		try {
			InputStream inputStream = inputSocket.getInputStream();
			try {
				OutputStream outputStream = outputSocket.getOutputStream();
				try {
					byte[] buffer = new byte[4096];
					int read;
					do {
						read = inputStream.read(buffer);
						if (read > 0) {
							outputStream.write(buffer, 0, read);
							if (inputStream.available() < 1) {
								outputStream.flush();
							}
						}
					} while (read >= 0);
				} finally {
					if (!outputSocket.isOutputShutdown()) {
						outputSocket.shutdownOutput();
					}
				}
			} finally {
				if (!inputSocket.isInputShutdown()) {
					inputSocket.shutdownInput();
				}
			}
		} catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage() + e.getStackTrace());
		}
	}

	/**
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private String readLine(Socket socket) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int next;
		readerLoop: while ((next = socket.getInputStream().read()) != -1) {
			if (previousWasR && next == '\n') {
				previousWasR = false;
				continue;
			}
			previousWasR = false;
			switch (next) {
			case '\r':
				previousWasR = true;
				break readerLoop;
			case '\n':
				break readerLoop;
			default:
				byteArrayOutputStream.write(next);
				break;
			}
		}
		return byteArrayOutputStream.toString(ISO_FORMAT);
	}

	/**
	 * 
	 * @param outputStreamWriter
	 * @param matcher
	 * @param msgState
	 * @throws IOException
	 */
	private void writeInforStateProxy(OutputStreamWriter outputStreamWriter, final Matcher matcher,
			final String msgState) throws IOException {

		outputStreamWriter.write("HTTP/" + matcher.group(3) + msgState + "\\r\\n");
		outputStreamWriter.write("Proxy-agent: Simple/0.1\r\n");
		outputStreamWriter.write("\r\n");
		outputStreamWriter.flush();
	}

	/**
	 * 
	 * @return
	 */
	private InetAddress getInetAddress() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			LOGGER.error(e.getLocalizedMessage() + e.getStackTrace());
		}
		return null;
	}

	public InetAddress getAddr() {
		return addr;
	}

	public void setAddr(InetAddress addr) {
		this.addr = addr;
	}
}

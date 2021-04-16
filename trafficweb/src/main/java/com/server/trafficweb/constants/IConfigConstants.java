package com.server.trafficweb.constants;

public interface IConfigConstants {
	public static String HOSTNAME = "199.30.30.98";

	public static int PORT = 9200;

	public static String PROTOCOL = "http";

	public static String INDEX_NAME = "network_packet";

	public static String USER_ID_FIELD = "user_id";

	public static String URL_FIELD = "url";

	public static String TIME_STAMP_FIELD = "@timestamp";

	public static String LOCALDATE_FIELD = "localdate";

	public static int MAX_NUM_USER = 200;

	public static int MAX_NUM_URL = 5000;

	public static int MAX_NUM_TIMES_TRAFFIC = 500;
	
	public static int MAX_SIZE_SHOW_DOCUMENT = 1000;
}

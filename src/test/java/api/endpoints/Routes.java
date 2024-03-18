package api.endpoints;

import api.utilities.PropertiesFileUtility;

public class Routes {

	public static String baseUrl = PropertiesFileUtility.readProperty("baseUrl");

	// User API URLs

	public static String user_post_url=baseUrl +"user";
	public static String user_get_url=baseUrl +"user/{username}";
	public static String user_put_url=baseUrl +"user/{username}";
	public static String user_delete_url=baseUrl +"user/{username}";
}

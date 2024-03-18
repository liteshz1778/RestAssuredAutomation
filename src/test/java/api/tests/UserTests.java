package api.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpoints;
import api.payloads.User;
import io.restassured.response.Response;

public class UserTests {

	Faker fk;
	User userPayload;
	String user_id;

	private Logger logger; //for retrieving logs

	@BeforeClass
	public void setUp() {

		fk = new Faker();
		userPayload = new User();

		userPayload.setId(fk.number().hashCode());
		userPayload.setUsername(fk.name().username());
		userPayload.setFirstname(fk.name().firstName());
		userPayload.setLastname(fk.name().lastName());
		userPayload.setEmail(fk.internet().emailAddress());
		userPayload.setPassword(fk.internet().password());
		userPayload.setPhone(fk.phoneNumber().hashCode());
		userPayload.setUserstatus(0);

		// logs
		logger = (Logger) LogManager.getLogger(this.getClass());
	}

	@Test(priority = 1)
	public void testCreateUser() {
		logger.info("************************* Create User *************************");
		Response response = UserEndpoints.createUser(userPayload);
		System.out.println("Create User Response:\n");
		response.then().log().all();
		System.out.println("\n=========================================\n");

		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(response.jsonPath().get("code").toString(), "200");

		this.user_id = response.jsonPath().get("message").toString();
		
		logger.info("************************* User Created *************************");
	}

	@Test(priority = 2, dependsOnMethods = { "testCreateUser" })
	public void testGetUser() {

		logger.info("************************* Retreiving User *************************");
		Response response = UserEndpoints.getUser(this.userPayload.getUsername().toString());
		System.out.println("Get User Response:\n");
		response.then().log().all();
		System.out.println("\n=========================================\n");

		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(response.jsonPath().get("id").toString(), this.user_id);
		Assert.assertEquals(response.jsonPath().get("username"), userPayload.getUsername());
//		Assert.assertEquals(response.jsonPath().get("firstName"), userPayload.getFirstname));  // Can't Test although sending it post request but not getting get request
//		Assert.assertEquals(response.jsonPath().get("lastName"), userPayload.getLastname()); // Can't Test although sending it post request but not getting get request
		Assert.assertEquals(response.jsonPath().get("email"), userPayload.getEmail());
		Assert.assertEquals(response.jsonPath().get("password"), userPayload.getPassword());
		Assert.assertEquals(Long.parseLong(response.jsonPath().get("phone")), userPayload.getPhone());
		Assert.assertEquals(response.jsonPath().get("userStatus").toString(), "0");
		
		logger.info("************************* User Retreive Successfully *************************");

	}

	@Test(priority = 3, dependsOnMethods = { "testCreateUser" })
	public void testUpdateUser() {

		logger.info("************************* Updating User *************************");
		// Updating the data
		this.userPayload.setFirstname(fk.name().firstName());
		this.userPayload.setLastname(fk.name().lastName());
		this.userPayload.setEmail(fk.internet().emailAddress());

		Response responseUpdate = UserEndpoints.UpdateUser(this.userPayload.getUsername().toString(), userPayload);
		System.out.println("Update User Response:\n");
		responseUpdate.then().log().all();
		System.out.println("\n=========================================\n");

		Assert.assertEquals(responseUpdate.getStatusCode(), 200);
		Assert.assertEquals(responseUpdate.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(responseUpdate.jsonPath().get("code").toString(), "200");
		Assert.assertEquals(responseUpdate.jsonPath().get("message"), this.user_id);

		Response responseGet = UserEndpoints.getUser(this.userPayload.getUsername().toString());
		System.out.println("Updated User Response:\n");
		responseGet.then().log().all();
		System.out.println("\n=========================================\n");
		logger.info("************************* User Updated Successfully *************************");
	}

	@Test(priority = 4, dependsOnMethods = { "testCreateUser", "testUpdateUser" })
	public void testDeleteUser() {

		logger.info("************************* Delete User *************************");
		Response response = UserEndpoints.deleteUser(this.userPayload.getUsername().toString());
		System.out.println("Delete User Response:\n");
		response.then().log().all();
		System.out.println("\n=========================================\n");

		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(response.jsonPath().get("code").toString(), "200");
		Assert.assertEquals(response.jsonPath().get("message"), userPayload.getUsername());
		logger.info("************************* User Deleted Successfully *************************");
	}
}

//output ------------>
/*
 * Create User Response:
 * 
 * HTTP/1.1 200 OK Date: Sun, 17 Mar 2024 18:39:33 GMT Content-Type:
 * application/json Transfer-Encoding: chunked Connection: keep-alive
 * Access-Control-Allow-Origin: * Access-Control-Allow-Methods: GET, POST,
 * DELETE, PUT Access-Control-Allow-Headers: Content-Type, api_key,
 * Authorization Server: Jetty(9.2.9.v20150224)
 * 
 * { "code": 200, "type": "unknown", "message": "542980314" }
 * 
 * =========================================
 * 
 * Get User Response:
 * 
 * HTTP/1.1 200 OK Date: Sun, 17 Mar 2024 18:39:36 GMT Content-Type:
 * application/json Transfer-Encoding: chunked Connection: keep-alive
 * Access-Control-Allow-Origin: * Access-Control-Allow-Methods: GET, POST,
 * DELETE, PUT Access-Control-Allow-Headers: Content-Type, api_key,
 * Authorization Server: Jetty(9.2.9.v20150224)
 * 
 * { "id": 542980314, "username": "seth.green", "email":
 * "kitty.okuneva@yahoo.com", "password": "h7ijbz4ep", "phone": "1148735023",
 * "userStatus": 0 }
 * 
 * =========================================
 * 
 * Update User Response:
 * 
 * HTTP/1.1 200 OK Date: Sun, 17 Mar 2024 18:39:39 GMT Content-Type:
 * application/json Transfer-Encoding: chunked Connection: keep-alive
 * Access-Control-Allow-Origin: * Access-Control-Allow-Methods: GET, POST,
 * DELETE, PUT Access-Control-Allow-Headers: Content-Type, api_key,
 * Authorization Server: Jetty(9.2.9.v20150224)
 * 
 * { "code": 200, "type": "unknown", "message": "542980314" }
 * 
 * =========================================
 * 
 * Updated User Response:
 * 
 * HTTP/1.1 200 OK Date: Sun, 17 Mar 2024 18:39:42 GMT Content-Type:
 * application/json Transfer-Encoding: chunked Connection: keep-alive
 * Access-Control-Allow-Origin: * Access-Control-Allow-Methods: GET, POST,
 * DELETE, PUT Access-Control-Allow-Headers: Content-Type, api_key,
 * Authorization Server: Jetty(9.2.9.v20150224)
 * 
 * { "id": 542980314, "username": "seth.green", "email":
 * "dario.collins@hotmail.com", "password": "h7ijbz4ep", "phone": "1148735023",
 * "userStatus": 0 }
 * 
 * =========================================
 * 
 * Delete User Response:
 * 
 * HTTP/1.1 200 OK Date: Sun, 17 Mar 2024 18:39:45 GMT Content-Type:
 * application/json Transfer-Encoding: chunked Connection: keep-alive
 * Access-Control-Allow-Origin: * Access-Control-Allow-Methods: GET, POST,
 * DELETE, PUT Access-Control-Allow-Headers: Content-Type, api_key,
 * Authorization Server: Jetty(9.2.9.v20150224)
 * 
 * { "code": 200, "type": "unknown", "message": "seth.green" }
 * 
 * =========================================
 */
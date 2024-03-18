package api.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpoints;
import api.payloads.User;
import api.utilities.DataProviders;
import io.restassured.response.Response;

public class DataDrivenTests {

	Faker fk;
	User userPayload;
	String userId;

	@BeforeMethod
	public void setUp() {
		fk = new Faker();
		userPayload = new User();

	}

	// Passing multiple values using DataProvider
	@Test(priority = 1, dataProvider = "Data", dataProviderClass = DataProviders.class)
	public void testCreateUser(String username, String fname, String lname, String passwd, String phone) {

		// Assigning values using excelReader & DataProvider
		userPayload.setUsername(username);
		userPayload.setFirstname(fname);
		userPayload.setLastname(lname);
		userPayload.setPassword(passwd);
		userPayload.setPhone(Long.parseLong(phone));

		// Assigning values using Faker class
		userPayload.setId(fk.number().hashCode());
		userPayload.setEmail(fk.internet().emailAddress());
		userPayload.setUserstatus(0);

		Response createResp = UserEndpoints.createUser(this.userPayload);
		System.out.println("Create User Response ===========>");
		createResp.then().log().body();

		Assert.assertEquals(createResp.getStatusCode(), Integer.parseInt("200"));
		Assert.assertEquals(createResp.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(createResp.jsonPath().get("code").toString(), String.valueOf(200));

		this.userId = createResp.jsonPath().get("message").toString();

		Response getResp = UserEndpoints.getUser(this.userPayload.getUsername().toString());
		System.out.println("Get User Response ===========>");
		getResp.then().log().body();

		Assert.assertEquals(getResp.getStatusCode(), 200);
		Assert.assertEquals(getResp.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(getResp.jsonPath().get("id").toString(), this.userId);
		Assert.assertEquals(getResp.jsonPath().get("username"), this.userPayload.getUsername());
//		Assert.assertEquals(getResp.jsonPath().get("firstName").toString(), this.userPayload.getFirstname());
//		Assert.assertEquals(getResp.jsonPath().get("lastName"), this.userPayload.getLastname());
		Assert.assertEquals(getResp.jsonPath().get("email"), this.userPayload.getEmail());
		Assert.assertEquals(getResp.jsonPath().get("password").toString(), this.userPayload.getPassword());
		Assert.assertEquals(getResp.jsonPath().get("userStatus").toString(), "0");

	}

	@Test(priority = 2, dependsOnMethods = {
			"testCreateUser" }, dataProvider = "UserNames", dataProviderClass = DataProviders.class)
	public void deleteUser(String username) {

		Response delResp = UserEndpoints.deleteUser(username);
		System.out.println("Delete User Response ===========>");
		delResp.then().log().body();

		Assert.assertEquals(delResp.getStatusCode(), 200);
		Assert.assertEquals(delResp.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(Integer.parseInt(delResp.jsonPath().get("code").toString()), 200);
		Assert.assertEquals(delResp.jsonPath().get("message"), username);

	}

}

/*
 * Output -------------------------->
 * 
 * Create User Response ===========> { "code": 200, "type": "unknown",
 * "message": "1778801068" } Get User Response ===========> { "id": 1778801068,
 * "username": "ABCD01", "email": "tobias.koelpin@hotmail.com",
 * "password":"Abcd", "phone": "123456789", "userStatus": 0 } Create User
 * Response===========> { "code": 200, "type": "unknown", "message":
 * "1387681307" } Get User Response ===========> { "id": 1387681307, "username":
 * "ABCD02", "email": "tarsha.zboncak@yahoo.com", "password": "Bcde", "phone":
 * "234567890", "userStatus": 0 } Create User Response ===========> { "code":
 * 200, "type": "unknown", "message": "2074544955" } Get User Response
 * ===========> { "id": 2074544955, "username": "ABCD03", "email":
 * "brittany.ebert@yahoo.com", "password": "Cdef", "phone": "345678901",
 * "userStatus": 0 } Create User Response ===========> { "code": 200, "type":
 * "unknown", "message":"330758189" } Get User Response ===========> { "id":
 * 330758189, "username": "ABCD04", "email": "bethel.schiller@gmail.com",
 * "password": "Defg", "phone": "456789012", "userStatus": 0 } Delete User
 * Response ===========> { "code": 200, "type": "unknown", "message": "ABCD01" }
 * Delete User Response ===========> { "code": 200, "type": "unknown",
 * "message": "ABCD02" } Delete User Response ===========> { "code": 200,
 * "type": "unknown", "message": "ABCD03" } Delete User Response ===========> {
 * "code": 200, "type": "unknown", "message": "ABCD04" }
 */

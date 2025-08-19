package api.test;

import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
	
	Faker faker;
	User userPayload;
	public Logger logger; // for logs
	
	
	@BeforeClass
	public void setup()
	{
		faker=new Faker();
		userPayload=new User();
		
		userPayload.setId(faker.number().numberBetween(1, 1000));
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		//logs
				logger= LogManager.getLogger(this.getClass());
	
	}
	
	@Test(priority=1)
	public void testPostUser()
	{
		logger.info("********** Creating user  ***************");
		Response response=UserEndpoints.createUser(userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200);
		System.out.println("testPostUser completed");
		logger.info("**********User is created  ***************");
			
	}
	
	@Test(priority=2)
	public void testGetUserByName() throws InterruptedException
	{
		
		Thread.sleep(2000);
		logger.info("********** Reading User Info ***************");
		Response response=UserEndpoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200);	
		System.out.println("testGetUserByName completed");
		logger.info("**********User info  is displayed ***************");
	}
	
	@Test(priority = 3)
	public void testUpdateUserByName() throws InterruptedException
	{
		logger.info("********** Updating User ***************");
		//update data using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response=UserEndpoints.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().all();
		//response.then().log().body().statusCode(200);
		Assert.assertEquals(response.getStatusCode(),200);
		
		logger.info("********** User updated ***************");
	//	checking data after update
		
		Thread.sleep(2000);
		Response updatedResponse=UserEndpoints.readUser(this.userPayload.getUsername());
		updatedResponse.then().log().all();
		Assert.assertEquals(updatedResponse.getStatusCode(),200);
		System.out.println("testUpdateUserByName completed");
		
	}
	
	@Test(priority = 4)
	public void testDeleteUserByName() throws InterruptedException
	{
		logger.info("**********   Deleting User  ***************");
		Response response=UserEndpoints.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(),200);
		System.out.println("testDeleteUserByName completed");
		logger.info("********** User deleted ***************");
	}
}
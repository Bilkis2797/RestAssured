package TestCases;

import io.restassured.RestAssured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ReadAllProducts {

	String baseURI;

	public ReadAllProducts() {
		baseURI = "https://techfios.com/api-prod/api/product";
	}

	@Test
	public void readAllProducts() {

		/*
		 * 01: ReadAllProducts HTTP method = GET EndPoint URL =
		 * https://techfios.com/api-prod/api/product/read.php Header: Content-Type =
		 * application/json; charset=UTF-8 Authorizationtype = Basic Auth username =
		 * demo@techfios.com password = abc123 StatusCode = 200
		 */

		Response response =
				given()
				    .log().all()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json; charset=UTF-8")
				    .auth().preemptive().basic("demo@techfios.com", "abc123").
				when()
				    .log().all()
				    .get("/read.php").
				then()
				/*.statusCode(200) .header("Content-Type", "application/json; charset=UTF-8") */
				    .log().all()
				    .extract().response();
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time: "+responseTime);
		
		if(responseTime <=2500) {
			System.out.println("Response time is within range");
		}else {
			System.out.println("Response time is out of range.");
		}
		
		int statusCode = response.getStatusCode();
		System.out.println("Status code: "+statusCode);
		Assert.assertEquals(statusCode, 200, "Status codes are not matching");

		String responseHeaderContentType = response.getHeader("Content-Type");
	    System.out.println("Response Header ContentType : "+ responseHeaderContentType);
		Assert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-9", "Response content type are not matching");

//		response.getBody().prettyPrint();
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body :"+ responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String firstProductID = jp.getString("records[0].id");
		System.out.println("First Product ID :"+firstProductID);
		
		if(firstProductID != null) {
			System.out.println("Product list is not empty");
		}else {
			System.out.println("Product list is empty!");
		}
		
	}

}

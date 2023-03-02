package TestCases;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct {

	String baseURI;
	SoftAssert softAssert;

	public ReadOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
	}

	@Test
	public void readOneProducts() {

		/*
		 *  ReadOneProduct
            HTTP method = GET
            EndPoint URL = https://techfios.com/api-prod/api/product/read_one.php
            Header:
            Content-Type = application/json
            Authorizationtype = Basic Auth
            username = demo@techfios.com
            password = abc123
            Query Params:
            id = 6765
            StatusCode = 200
            responseTime <= 1500ms
		 */

		Response response =
				given()
				   // .log().all()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json")
				    .auth().preemptive().basic("demo@techfios.com", "abc123")
				    .queryParam("id", "6765").
				when()
				  //  .log().all()
				    .get("/read_one.php").
				then()
				/*.statusCode(200) .header("Content-Type", "application/json; charset=UTF-8") */
				  //  .log().all()
				    .extract().response();
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time: "+responseTime);
		
		if(responseTime <=1500) {
			System.out.println("Response time is within range");
		}else {
			System.out.println("Response time is out of range.");
		}
		
		int statusCode = response.getStatusCode();
//		Assert.assertEquals(statusCode, 200, "Status codes are not matching");
		softAssert.assertEquals(statusCode, 200, "Status codes are not matching");
		System.out.println("Status code: "+statusCode);


		String responseHeaderContentType = response.getHeader("Content-Type");
//		Assert.assertEquals(responseHeaderContentType, "application/json", "Response content type are not matching");
		softAssert.assertEquals(responseHeaderContentType, "application/json 8", "Response content type are not matching");
	    System.out.println("Response Header ContentType : "+ responseHeaderContentType);


		response.getBody().prettyPrint();
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body :"+ responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String productName = jp.getString("name");
//		Assert.assertEquals(productName, "Amazing Pillow 2.0");
		softAssert.assertEquals(productName, "Amazing Pillow 2.0");
		System.out.println("Product name :"+productName);

		
		String productDescription = jp.getString("description");
//		Assert.assertEquals(productDescription, "The best pillow for amazing QAs.");
		softAssert.assertEquals(productDescription, "The best pillow for amazing QAs.");
		System.out.println("Product Description :"+productDescription);

		
		String productPrice = jp.getString("price");
//		Assert.assertEquals(productPrice, "99");
		softAssert.assertEquals(productPrice, "99");
		System.out.println("Product price :"+productPrice);

		
		softAssert.assertAll();
	}

}

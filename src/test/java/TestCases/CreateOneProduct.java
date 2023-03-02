package TestCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateOneProduct {

	String baseURI;
	SoftAssert softAssert;
	String createPayloadPath;
	HashMap<String, String> createPayload;
	String firstProductID;

	public CreateOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		createPayloadPath = "src\\main\\java\\data\\CreatePayload.json";
		createPayload = new HashMap<String, String>();
	}
	
	public Map<String , String> createPayloadMap(){
	
		createPayload.put("name", "Amazing Pillow created");
		createPayload.put("price", "999");
		createPayload.put("description", "The best pillow for amazing QA's.");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
	}

	@Test(priority =1)
	public void createOneProduct() {

		/*
		 * CreateOneProduct 
		 * HTTP method = POST 
		 * EndPoint URL =https://techfios.com/api-prod/api/product/create.php  
		 * Header: Content-Type = application/json
		 * Authorizationtype = Basic Auth 
		 * username = demo@techfios.com
		 * password = abc123 
		 * StatusCode = 201 
		 * RequestBody/payload: { 
		 * "name" :"Amazing Pillow created",
		 * "price" : "999", 
		 * "description" :"The best pillow for amazing QA's.", 
		 * "category_id" : 2, 
		 * "category_name": "Electronics" }
		 */

		Response response =
				given()
				   // .log().all()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json")
				    .auth().preemptive().basic("demo@techfios.com", "abc123")
//				    .body(new File(createPayloadPath)).
				    .body(createPayloadMap()).
			    when()
				  //  .log().all()
				    .post("/create.php ").
				then()
				/*.statusCode(200) .header("Content-Type", "application/json; charset=UTF-8") */
				  //  .log().all()
				    .extract().response();
		
		
		int statusCode = response.getStatusCode();
//		Assert.assertEquals(statusCode, 201, "Status codes are not matching");
		softAssert.assertEquals(statusCode, 201, "Status codes are not matching");
		System.out.println("Status code: "+statusCode);


		String responseHeaderContentType = response.getHeader("Content-Type");
//		Assert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response content type are not matching");
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response content type are not matching");
	    System.out.println("Response Header ContentType : "+ responseHeaderContentType);

//
//	    {
//	        "message": "Product was created."
//	    }
//	    
		response.getBody().prettyPrint();
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body :"+ responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String productMessage = jp.getString("message");
//		Assert.assertEquals(productName, "Amazing Pillow 2.0");
		softAssert.assertEquals(productMessage, "Product was created.","Product massage is not matching!");
		System.out.println("Product message :"+productMessage);

		JsonPath jp2 = new JsonPath(new File(createPayloadPath));
		String name = jp2.getString("name");
		System.out.println("Expected product name : "+ name);

		
		softAssert.assertAll();
	}
	
	@Test(priority =2)
	public void readAllProducts() {


		Response response =
				given()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json; charset=UTF-8")
				    .auth().preemptive().basic("demo@techfios.com", "abc123").
				when()
				    .get("/read.php").
				then()
				    .extract().response();
		
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body :"+ responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		firstProductID = jp.getString("records[0].id");
		System.out.println("First Product ID :"+firstProductID);
		
	}
	
	@Test(priority = 3)
	public void readOneProducts() {

		
		Response response =
				given()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json")
				    .auth().preemptive().basic("demo@techfios.com", "abc123")
				    .queryParam("id", firstProductID).
				when()
				    .get("/read_one.php").
				then()
				
				    .extract().response();
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body :"+ responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String actualProductName = jp.getString("name");
		String expectedProductName = createPayloadMap().get("name");
		softAssert.assertEquals(actualProductName, expectedProductName, "Product names are not matching");
		System.out.println("Actual Product name :"+actualProductName);

		
		String actualProductDescription = jp.getString("description");
		String expectedProductDescription = createPayloadMap().get("description");
		softAssert.assertEquals(actualProductDescription, expectedProductDescription,"Product description are not matching");
		System.out.println("Actual Product Description :"+actualProductDescription);

		
		String actualProductPrice = jp.getString("price");
		String expectedProductPrice = createPayloadMap().get("price");
		softAssert.assertEquals(actualProductPrice, expectedProductPrice);
		System.out.println("Actual Product price :"+actualProductPrice);

		softAssert.assertAll();
	}
}

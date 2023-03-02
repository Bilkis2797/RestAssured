package TestCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateOneProduct {
	String baseURI;
	SoftAssert softAssert;
	String createPayloadPath;
	HashMap<String, String> createPayload;
	String firstProductID;
	HashMap<String, String> updatePayload;


	public UpdateOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		createPayloadPath = "src\\main\\java\\data\\CreatePayload.json";
		createPayload = new HashMap<String, String>();
		updatePayload = new HashMap<String, String>();

	}
	
	public Map<String , String> createPayloadMap(){
	
		createPayload.put("name", "Amazing Pillow created");
		createPayload.put("price", "999");
		createPayload.put("description", "The best pillow for amazing QA's.");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
	}
	
	public Map<String , String> updatePayloadMap(){
	
		updatePayload.put("id", "6827");
		updatePayload.put("name", "Amazing Pillow update");
		updatePayload.put("price", "555");
		updatePayload.put("description", "The best pillow for amazing QA's.");
		updatePayload.put("category_id", "2");
		updatePayload.put("category_name", "Electronics");
		
		return updatePayload;
	}

	@Test(priority =1)
	public void createOneProduct() {

		
		Response response =
				given()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json")
				    .auth().preemptive().basic("demo@techfios.com", "abc123")
//				    .body(new File(createPayloadPath)).
				    .body(createPayloadMap()).
			    when()
				    .post("/create.php ").
				then()
				
				    .extract().response();
		
		
		int statusCode = response.getStatusCode();
		softAssert.assertEquals(statusCode, 201, "Status codes are not matching");
		System.out.println("Status code: "+statusCode);


		String responseHeaderContentType = response.getHeader("Content-Type");
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response content type are not matching");
	    System.out.println("Response Header ContentType : "+ responseHeaderContentType);

//	   {
//	    "message": "Product was created."
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
	
	@Test(priority =4)
	public void updateOneProduct() {

/*		 UpdateOneProduct
		 HTTP method = PUT
		 EndPoint URL = https://techfios.com/api-prod/api/product/update.php
		 Header:
		 Content-Type = application/json; charset=UTF-8
		 Authorizationtype = Basic Auth
		 username = demo@techfios.com
		 password = abc123
		 Query Params:
		 id = 6827
		 StatusCode = 200
		 RequestBody/payload:
		 {
		     "id" : "6827",
		     "name" : "Amazing updated Pillow 3.0",
		     "price" : "255",
		     "description" : "The best pillow for amazing QA's.",
		     "category_id" : 2,
		 	"category_name": "Electronics"
		 }*/
		
		Response response =
				given()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json; charset=UTF-8")
				    .auth().preemptive().basic("demo@techfios.com", "abc123")
//				    .body(new File(createPayloadPath)).
				    .body(updatePayloadMap()).
			    when()
				    .put("/update.php ").
				then()
				
				    .extract().response();
		
		
		int statusCode = response.getStatusCode();
		softAssert.assertEquals(statusCode, 200, "Status codes are not matching");
		System.out.println("Status code: "+statusCode);


		String responseHeaderContentType = response.getHeader("Content-Type");
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response content type are not matching");
	    System.out.println("Response Header ContentType : "+ responseHeaderContentType);

//	   {
//	    "message": "Product was updated."
//	    }
//	    
		response.getBody().prettyPrint();
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body :"+ responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String productMessage = jp.getString("message");
//		Assert.assertEquals(productName, "Amazing Pillow 2.0");
		softAssert.assertEquals(productMessage, "Product was updated.","Product massage is not matching!");
		System.out.println("Product message :"+productMessage);

		JsonPath jp2 = new JsonPath(new File(createPayloadPath));
		String name = jp2.getString("name");
		System.out.println("Expected product name : "+ name);

		
		softAssert.assertAll();
	}
	
	@Test(priority = 5)
	public void readOneUpdatedProduct() {

		
		Response response =
				given()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json")
				    .auth().preemptive().basic("demo@techfios.com", "abc123")
				    .queryParam("id", updatePayloadMap().get("id")).
				when()
				    .get("/read_one.php").
				then()
				
				    .extract().response();
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body :"+ responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String actualProductName = jp.getString("name");
		String expectedProductName = updatePayloadMap().get("name");
		softAssert.assertEquals(actualProductName, expectedProductName, "Product names are not matching");
		System.out.println("Actual Product name :"+actualProductName);

		
		String actualProductDescription = jp.getString("description");
		String expectedProductDescription = updatePayloadMap().get("description");
		softAssert.assertEquals(actualProductDescription, expectedProductDescription,"Product description are not matching");
		System.out.println("Actual Product Description :"+actualProductDescription);

		
		String actualProductPrice = jp.getString("price");
		String expectedProductPrice = updatePayloadMap().get("price");
		softAssert.assertEquals(actualProductPrice, expectedProductPrice);
		System.out.println("Actual Product price :"+actualProductPrice);

		softAssert.assertAll();
	}
}

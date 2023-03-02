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

public class UpdateOneProductAdvanced {
	String baseURI;
	SoftAssert softAssert;
	String firstProductID;
	HashMap<String, String> updatePayload;


	public UpdateOneProductAdvanced() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		updatePayload = new HashMap<String, String>();

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

		softAssert.assertAll();
	}
	
	@Test(priority = 2)
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

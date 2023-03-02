package TestCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DeleteOneProductAdvance {

	String baseURI;
	SoftAssert softAssert;
	String createPayloadPath;
	HashMap<String, String> createPayload;
	String firstProductId;
	HashMap<String, String> deletePayload;
    String deleteProductId;
    
    public DeleteOneProductAdvance() {
    	baseURI = "https://techfios.com/api-prod/api/product";
    	softAssert = new SoftAssert();
    	createPayloadPath = "src\\\\main\\\\java\\\\data\\\\CreatePayload.json";
    	createPayload = new HashMap<String, String>();
    	deletePayload = new HashMap<String, String>();
    }
    
    public Map<String, String> createPayloadMap(){
    	

		createPayload.put("name", "Amazing Pillow created");
		createPayload.put("price", "999");
		createPayload.put("description", "The best pillow for amazing QA's.");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
    	
    }
    
    public Map<String, String> deletePayloadMap(){
    	
    	deletePayload.put("id", deleteProductId);
    	
		return deletePayload;
    	
    }
    
    @Test(priority =1)
	public void createOneProduct() {

	
		Response response =
				given()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json")
				    .auth().preemptive().basic("demo@techfios.com", "abc123")				    .body(createPayloadMap()).
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
  
		response.getBody().prettyPrint();
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body :"+ responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String productMessage = jp.getString("message");
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
		firstProductId = jp.getString("records[0].id");
		System.out.println("First Product ID :"+firstProductId);
		deleteProductId = firstProductId;
	}
	
	@Test(priority =3)
	public void deleteOneProduct() {

		Response response =
				given()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json; charset=UTF-8")
				    .auth().preemptive().basic("demo@techfios.com", "abc123")
				    .body(deletePayloadMap()).
			    when()
				    .delete("/delete.php ").
				then()
				
				    .extract().response();
		
		
		int statusCode = response.getStatusCode();
		softAssert.assertEquals(statusCode, 200, "Status codes are not matching");
		System.out.println("Status code: "+statusCode);


		String responseHeaderContentType = response.getHeader("Content-Type");
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response content type are not matching");
	    System.out.println("Response Header ContentType : "+ responseHeaderContentType);

//	   {
//	    "message": "Product was deleted."
//	    }
//	    
		response.getBody().prettyPrint();
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body :"+ responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String productMessage = jp.getString("message");
		softAssert.assertEquals(productMessage, "Product was deleted.","Product massage is not matching!");
		System.out.println("Product message :"+productMessage);

		softAssert.assertAll();
	}
	@Test(priority = 4)
	public void readOneDeletedProduct() {

		
		Response response =
				given()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json")
				    .auth().preemptive().basic("demo@techfios.com", "abc123")
				    .queryParam("id", deletePayloadMap().get("id")).
				when()
				    .get("/read_one.php").
				then()
				
				    .extract().response();
		
		int statusCode = response.getStatusCode();
		softAssert.assertEquals(statusCode, 404, "Status codes are not matching");
		System.out.println("Status code: "+statusCode);


		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body :"+ responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String actualDeletemessage = jp.getString("message");
		String expectedDeletemessage = "Product does not exist.";
		softAssert.assertEquals(actualDeletemessage, expectedDeletemessage, "Product message are not matching");
		System.out.println("Actual delete message :"+actualDeletemessage);

		
		softAssert.assertAll();
	}
}

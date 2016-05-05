package onGoByRestAssured;


import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.*;

import java.io.IOException;
import java.io.InputStream;





public class ApiTest {
	
	ExtentReports report;
	ExtentTest logger;
	
	
	private String uri = "http://ongo.dev.lezgro.com/api/v1";
	private String access_token = null;
	public String br = "----------------------------------\n";
	
	
	@BeforeSuite
	public void init(){
		report = new ExtentReports("./Report/OnGoReport.html");
	}
	
	@Test
	public void login01(){
				
//		given()
//			.contentType(ContentType.JSON)
//			.body("{\"user\":{\"email\":\"trv01@cw.blueemails.com\", \"password\":\"qwerty\" }}")
//		.when()
//			.post("http://ongo.dev.lezgro.com/auth/sign_in")
//		.then()
//			.statusCode(200);
		
		
		logger = report.startTest(currentMethodName(2));
		
		logger.log(LogStatus.INFO, "api is sent");
		RequestSpecification login = RestAssured.given()
				.contentType(ContentType.JSON)
				.body("{\"user\":{\"email\":\"trv01@cw.blueemails.com\", \"password\":\"qwerty\" }}");
		
		logger.log(LogStatus.INFO, "response is got");
		Response response = login.accept(ContentType.JSON).post("http://ongo.dev.lezgro.com/auth/sign_in");
		
		System.out.println("token = " + response.body().jsonPath().getJsonObject("access_token"));
		print_to(uri, response);
//		System.out.println("method_name - " + (new Exception().getStackTrace()[0].getMethodName()));
				
				
		setAccess_token(response.body().jsonPath().getJsonObject("access_token"));
		Assert.assertEquals(response.statusCode(), 200);
		logger.log(LogStatus.PASS, "response is got");
	}
	
		
	@Test
	public void show_all_roles(){
				
		String url = uri + "/roles";
		
		logger = report.startTest(currentMethodName(2));
		
		RequestSpecification show_all_roles = RestAssured.given()
				.contentType(ContentType.JSON)
				.header("Access-Token", getAccess_token());
		Response response = show_all_roles.accept(ContentType.JSON).get(url);
		
		print_to(url, response);
		Assert.assertEquals(response.statusCode(), 200);
		logger.log(LogStatus.PASS, "response is got");
	}
	
	
	@Test
	public void show_current_user(){
				
		String url = uri + "/users/me";
		
		logger = report.startTest(currentMethodName(2));
		
		RequestSpecification show_current_user = RestAssured.given()
				.contentType(ContentType.JSON)
				.header("Access-Token", getAccess_token());
		Response response = show_current_user.accept(ContentType.JSON).get(url);
		
		print_to(url, response);
		Assert.assertEquals(response.statusCode(), 200, "Fucking shit is happened! ");
		
		logger.log(LogStatus.PASS, "response is got");
	}
	
	@Test 
	public void show_Guide_Braintree_Customer() throws IOException{
		String url = uri + "/users/me/customer";
		
		logger = report.startTest(currentMethodName(2));
		
		final RequestSpecification show_Guide_Braintree_Customer = RestAssured.given()
				.contentType(ContentType.JSON)
				.header("Access-Token", getAccess_token());
		final Response response = show_Guide_Braintree_Customer.accept(ContentType.JSON).get(url);
		
		print_to(url, response);
		
		InputStream schema = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("show_Guide_Braintree_Customer.json");
		
//		BufferedReader in = new BufferedReader(new InputStreamReader(schema));
//		String line = null;
//		while((line = in.readLine()) != null) {
//			  System.out.println(line);
//			  
//			}
		
		response.then().assertThat().body(matchesJsonSchema(schema));
				
		Assert.assertEquals(response.statusCode(), 200, "Fucking shit is happened! ");
				
		logger.log(LogStatus.PASS, "response is got");

		

		
	}
	
//	private Schema fis() throws IOException{
//		
//		 InputStream is = null;
//
//		    try {
//		        is = new FileInputStream("./Jsons/show_Guide_Braintree_Customer.json");
//		        System.out.println("inputStream - " + is.read());
//		         
//		    } catch (FileNotFoundException e) {
//		        
//		        e.printStackTrace();
//		    } catch (IOException e) {
//		        
//		        e.printStackTrace();
//		    }
//		    JSONObject rawSchema = new JSONObject(new JSONTokener(is));
//			Schema schema = SchemaLoader.load(rawSchema);
//			is.close();
//		    return schema;
//	}
	
	
	

	@AfterMethod
	public void ifAllBad(ITestResult result){
		if(result.getStatus()==ITestResult.FAILURE){
//			ITestContext testContext = result.getTestContext();
			logger.log(LogStatus.FAIL, currentMethodName(2), result.getThrowable().getMessage());
		}
		report.endTest(logger);
		report.flush();
	}
	
	 

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
	
	public static String currentMethodName(int actulaLevel) {

	    StackTraceElement stack[] = Thread.currentThread().getStackTrace();
	    StackTraceElement element = stack[actulaLevel];
	    return element.getMethodName().toString();
	  }

	
	public void print_to(String url, Response response){
		System.out.println("method_name - " + currentMethodName(3));
		System.out.println("url = " + url);
		System.out.println("response = " + response.asString());
		System.out.println("responseCode = " + response.statusCode());
		System.out.println(br);
	}
	

}

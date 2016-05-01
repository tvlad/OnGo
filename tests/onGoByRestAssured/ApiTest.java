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


public class ApiTest {
	
	ExtentReports report;
	ExtentTest logger;
	
	
	private String uri = "http://ongo.dev.lezgro.com/api/v1";
	private String access_token = null;
	public String br = "----------------------------------\n";
	
	
	@BeforeSuite
	public void init(){
		report = new ExtentReports("D:\\Reports\\OnGoReport.html");
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
		final RequestSpecification login = RestAssured.given()
				.contentType(ContentType.JSON)
				.body("{\"user\":{\"email\":\"trv01@cw.blueemails.com\", \"password\":\"qwerty\" }}");
		
		logger.log(LogStatus.INFO, "response is got");
		final Response response = login.accept(ContentType.JSON).post("http://ongo.dev.lezgro.com/auth/sign_in");
		
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
		
		final RequestSpecification show_all_roles = RestAssured.given()
				.contentType(ContentType.JSON)
				.header("Access-Token", getAccess_token());
		final Response response = show_all_roles.accept(ContentType.JSON).get(url);
		
		print_to(url, response);
		Assert.assertEquals(response.statusCode(), 200);
		logger.log(LogStatus.PASS, "response is got");
	}
	
	
	@Test
	public void show_current_user(){
				
		String url = uri + "/users/me";
		
		logger = report.startTest(currentMethodName(2));
		
		final RequestSpecification show_current_user = RestAssured.given()
				.contentType(ContentType.JSON)
				.header("Access-Token", getAccess_token());
		final Response response = show_current_user.accept(ContentType.JSON).get(url);
		
		print_to(url, response);
		Assert.assertEquals(response.statusCode(), 201, "Fucking shit is happened! ");
		
		logger.log(LogStatus.PASS, "response is got");
	}
	
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

import io.restassured.config.SSLConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;

import com.david.mongorest.models.Invoice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class InvoiceRestTest {
	static Invoice invoice;
	static String tempID;
	static JSONObject jsonInvoice;
	
	@BeforeClass
	public static void setup() {
//		RestAssured.config = RestAssured.newConfig().sslConfig(new SSLConfig("/keystore.p12", "Ineed$100"));
	}
	//before each test create an invoice and record its id.  serves as a create test
	@Before
	public void createNewInvoiceWithValidJSON() throws JSONException{
		InvoiceRestTest.invoice = new Invoice();
		InvoiceRestTest.tempID = InvoiceRestTest.invoice.get_id();
		InvoiceRestTest.jsonInvoice = InvoiceRestTest.invoice.createJSONObject();
		RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();
		request.contentType(ContentType.JSON);
		request.body(jsonInvoice.toString());
		request.post("invoice/");
	}
	
	//afterwards delete the entry to not alter the database serves as a delete test
	@After
	public void deleteInvoiceWithValidID() {
		RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();
		request.delete("/invoice/" + InvoiceRestTest.tempID);
	}
	
	@Test
	public void requestInvoiceWithValidID() {
		get("/invoice/" + InvoiceRestTest.tempID)
		.then().statusCode(200).assertThat()
		.body("_id", equalTo(InvoiceRestTest.tempID));
	}
	@Test
	public void requestInvoiceWithBadID() {
		get("/invoice/thisWillNeverBeAValidID")
		.then().statusCode(404);
	}
	
	@Test
	public void updateInvoiceWithValidID() throws JSONException {
		jsonInvoice.put("customerID", "1234");
		
		RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();
		request.contentType(ContentType.JSON);
		request.body(jsonInvoice.toString());
		
		Response response = request.put("invoice/" + InvoiceRestTest.tempID);
		jsonInvoice = new JSONObject(response.asString());
		int customerID = jsonInvoice.getInt("customerID");
		Assert.assertEquals(customerID, 1234);
	}
	
	@Test
	public void updateInvoiceWithBadID() throws JSONException {
		RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();
		request.contentType(ContentType.JSON);
		request.body(jsonInvoice.toString());
		Response response = request.put("invoice/" + "thisWillNeverBeAValidId");
		int statusCode = response.getStatusCode();
		Assert.assertEquals(404, statusCode);
	}
}

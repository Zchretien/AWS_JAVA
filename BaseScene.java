package finalproject;

//Java Imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//Aws Imports
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class BaseScene {
	
	protected Map<String, AttributeValue> newUser(String email, String password,String phone)
	{
		//Create the user HashMap
		Map<String, AttributeValue> user = new HashMap<String, AttributeValue>();
		
		//Attributes
		user.put("email", new AttributeValue(email));
		user.put("password",new AttributeValue(password));
		user.put("phone",new AttributeValue(phone));
		
		//Return user
		return user;
	}
	
	protected Map<String, AttributeValue> newItem(String itemID,String itemName, int aNumber)
	{
		//Make Hashmap
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		//Attributes
		item.put("ItemID", new AttributeValue(itemID));
		item.put("a-number",new AttributeValue().withN(Integer.toString(aNumber)));
		item.put("word", new AttributeValue(itemName));
		
		return item;
	}
	
	protected void SendEmail(String email)
	{
		//Emails will be sent from this address
		final String FROM = "zrchretien@email.neit.edu";
		
		//This is the address the email will be sent to
		final String TO = email;
		
		//Email Attributes
		//Subject
		final String SUBJECT = "Thanks for Signing up!!";
		
		//Body
		final String HTMLBODY = "<h2>Thank you</h2> for signing up for my program. I hope it works!!";
		final String TEXTBODY = "Thank you for signing up for my program. I hope it works!!";
		
		//Create Email Client
		AmazonSimpleEmailService emailClient = AmazonSimpleEmailServiceClientBuilder.standard()
				.withRegion(Regions.US_EAST_1).build();
		
		//Create Email Request
		SendEmailRequest emailRequest = new SendEmailRequest()
				.withDestination(
						new Destination().withToAddresses(TO))
				.withMessage(new Message()
						.withBody(new Body()
								.withHtml(new Content()
										.withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content()
										.withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content()
								.withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		try {
			
			//Send Email to registered email
			emailClient.sendEmail(emailRequest);
			
			System.out.println("Email Sent!");
			
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	protected void SendEmail(String subject,String htmlBody,String textBody,String email)
	{
		//Emails will be sent from this address
		final String FROM = "zrchretien@email.neit.edu";
		
		//This is the address the email will be sent to
		final String TO = email;
		
		//Email Attributes
		//Subject
		final String SUBJECT = subject;
		
		//Body
		final String HTMLBODY = htmlBody;
		final String TEXTBODY = textBody;
		
		//Create Email Client
		AmazonSimpleEmailService emailClient = AmazonSimpleEmailServiceClientBuilder.standard()
				.withRegion(Regions.US_EAST_1).build();
		
		//Create Email Request
		SendEmailRequest emailRequest = new SendEmailRequest()
				.withDestination(
						new Destination().withToAddresses(TO))
				.withMessage(new Message()
						.withBody(new Body()
								.withHtml(new Content()
										.withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content()
										.withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content()
								.withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		try {
			
			//Send Email to registered email
			emailClient.sendEmail(emailRequest);
			
			System.out.println("Email Sent!");
			
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}	
	}
	
	protected void SendSMS(String phone)
	{
		//Create Client
		AmazonSNSClient snsClient = new AmazonSNSClient();
		
		//Text to send to phone
		String msg = "Thanks for signing up for my program!! I hope it works!!";
		
		//Create a Map object for all the variables
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		
		try {
			
			PublishResult result = snsClient.publish(new PublishRequest()
					.withMessage(msg)
					.withPhoneNumber(phone)
					.withMessageAttributes(smsAttributes));

		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	protected void SendSMS(String phone,String msg)
	{
		AmazonSNSClient client = new AmazonSNSClient();
		
		//Create a Map object for all the variables
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		
		try {
			
			PublishResult result = client.publish(new PublishRequest()
					.withMessage(msg)
					.withPhoneNumber(phone)
					.withMessageAttributes(smsAttributes));

		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	protected String getAdjective()
	{
    	String adj = "";
		
		//Link to adjective api
    	String adjAPIlink = "https://ahdzt7w221.execute-api.us-east-1.amazonaws.com/prod/adjective";
    	
    	//Grab an adjective from the API Gateway
    	try {
			URL adjURL = new URL(adjAPIlink);
			
			try {
				
    			HttpURLConnection connection = (HttpURLConnection) adjURL.openConnection();
    			connection.setRequestMethod("GET");
    			
    			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    			
    			String line;
    			String result = "";
    			
    			while((line = rd.readLine()) != null)
    			{
    				result += line;
    			}
    			
    			rd.close();
    			
    			StringBuilder finalResult = new StringBuilder(result);
    			
    			//Trim quotes off payload
    			finalResult.deleteCharAt(0);
    			finalResult.deleteCharAt(finalResult.length()-1);
    			
    			adj += finalResult;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
    	
		return adj;
	}
	
	protected String getNoun()
	{
		String noun = "";
		
    	//Link to noun api
    	String nounAPIlink = "https://ahdzt7w221.execute-api.us-east-1.amazonaws.com/prod/noun";
    	
    	//Grab a noun from the API Gateway
    	try {
    		URL nounURL = new URL(nounAPIlink);
    		
    		try {
    			HttpURLConnection connection = (HttpURLConnection) nounURL.openConnection();
    			connection.setRequestMethod("GET");
    			
    			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    			
    			String line;
    			String result = "";
    			
    			while((line = rd.readLine()) != null)
    			{
    				result += line;
    			}
    			
    			rd.close();
    			
    			StringBuilder finalResult = new StringBuilder(result);
    			
    			//Trim quotes
    			finalResult.deleteCharAt(0);
    			finalResult.deleteCharAt(finalResult.length() - 1);
    			
    			noun += finalResult;
    			
    		}catch(IOException e)
    		{
    			e.printStackTrace();
    		}
    		
    	} catch(MalformedURLException e)
    	{
    		e.printStackTrace();
    	}
		
		return noun;
	}
	
	protected AmazonS3 getS3Client()
	{
    	//Create client to access S3 services
    	AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    	
    	return s3Client;
	}
	
	protected AmazonDynamoDB getDBClient()
	{
		//Create the DB Client with the credentials and region
		AmazonDynamoDB db = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		
		return db;
	}
	
	protected CreateTableRequest createTableRequest(String tableName,String attributeName)
	{
		CreateTableRequest ctr = new CreateTableRequest().withTableName(tableName)
				.withKeySchema(new KeySchemaElement().withAttributeName(attributeName).withKeyType(KeyType.HASH))
				.withAttributeDefinitions(new AttributeDefinition().withAttributeName(attributeName).withAttributeType(ScalarAttributeType.S))
				.withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
		
		return ctr;
	}
	
	protected PutItemRequest createPutItemRequest(String tableName, Map<String,AttributeValue> item)
	{
		PutItemRequest piRequest = new PutItemRequest(tableName,item);
		
		return piRequest;
	}
	
}

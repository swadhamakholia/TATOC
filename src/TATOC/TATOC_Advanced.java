//Automate TATOC advanced course.

package TATOC;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Properties;


public class TATOC_Advanced {

	static final String DB_URL = "jdbc:mysql://10.0.1.86/tatoc"; 
	static final String USER = "tatocuser";
	static final String PASS = "tatoc01";


	public static void main(String[] args) throws Exception {
		// *** FIRST PART***

		WebDriver driver = new FirefoxDriver();             
		driver.get("http://10.0.1.86/tatoc/advanced/hover/menu");
		Actions actions = new Actions(driver);
		WebElement mainMenu = driver.findElement(By.xpath("/html/body/div/div[2]/div[2]"));
		actions.moveToElement(mainMenu);

		WebElement subMenu = driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/span[5]"));
		actions.moveToElement(subMenu);
		actions.click().build().perform();

		// *** SECOND PART ***

		//  driver.get("http://10.0.1.86/tatoc/advanced/query/gate");
		String sym= driver.findElement(By.cssSelector("#symbol")).getAttribute("value");
		System.out.println("Symbol generated :   "+ sym);
		Connection con = null;

		Statement stmt = null;
		Class.forName("com.mysql.jdbc.Driver");  
		System.out.println("Connecting to a selected database...");
		con = DriverManager.getConnection(DB_URL, USER, PASS);
		System.out.println("Connected database successfully...");
		Class.forName("com.mysql.jdbc.Driver"); 
		System.out.println("Creating statement...");
		stmt = con.createStatement();

		String sql = "SELECT id, symbol FROM identity WHERE  symbol= '"+sym+"'";

		ResultSet rs = stmt.executeQuery(sql);
		int id=0;

		while(rs.next()){

			id  = rs.getInt("id");
			String symbol = rs.getString("symbol");

			System.out.print("ID: " + id);
			System.out.print(", Symbol: " + symbol);

		}
		rs.close();
		String name, passkey;
		String sql1 = "SELECT name, passkey FROM credentials WHERE  id= '"+id+"'";
		ResultSet rs1 = stmt.executeQuery(sql1);
		while(rs1.next()){

			name  = rs1.getString("name");
			passkey  = rs1.getString("passkey");

			System.out.print(" ,Name: " + name);
			System.out.print(", passkey: " + passkey);
			driver.findElement(By.cssSelector("#name")).sendKeys(name);
			driver.findElement(By.cssSelector("#passkey")).sendKeys(passkey);
		}
		rs1.close();
		con.close();
		driver.findElement(By.cssSelector("#submit")).click();


		// *** THIRD PART ***

		JavascriptExecutor js = (JavascriptExecutor) driver;  
		Thread.sleep(4000);
		js.executeScript("document.getElementsByClassName('video')[0].getElementsByTagName('object')[0].playMovie();");
		Thread.sleep(26000);
		driver.findElement(By.linkText("Proceed")).click();


		// *** FOURTH PART ***

		String session= driver.findElement(By.cssSelector("#session_id")).getText();
		String[] sess_id= session.split(": ");
		System.out.println(" \n\nsession id:"+  sess_id[1]);

		//REST
		URL geturl = new URL("http://10.0.1.86/tatoc/advanced/rest/service/token/" + sess_id[1]);
		HttpURLConnection getconn = (HttpURLConnection) geturl.openConnection();
		getconn.setRequestMethod("GET");
		getconn.setRequestProperty("Accept", "application/json");

		if (getconn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ getconn.getResponseCode());
		}


		BufferedReader br = new BufferedReader(new InputStreamReader((getconn.getInputStream())));
		String output;
		String restful = new String();

		while ((output = br.readLine()) != null) {
			restful=restful.concat(output);
		}
		br.close();
		System.out.println(restful);
		String response[]= restful.split(":\"");
		System.out.println("response: "+response[1]);
		String token[]= response[1].split("\"");
		String jsonToken= token[0];
		System.out.println("jsontoken: "+ jsonToken);

		//Post     
		try {

			URL posturl = new URL("http://10.0.1.86/tatoc/advanced/rest/service/register");
			HttpURLConnection postconn = (HttpURLConnection) posturl.openConnection();
			postconn.setDoOutput(true);
			postconn.setRequestMethod("POST");

			postconn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String input = "id="+sess_id[1]+"& signature="+jsonToken+"&allow_access=1";

			DataOutputStream wr = new DataOutputStream(postconn.getOutputStream());
			wr.writeBytes(input);
			wr.flush();
			wr.close();

			int responseCode = postconn.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + posturl);
			System.out.println("Post parameters : " + input);
			System.out.println("Response Code : " + responseCode); System.out.println(postconn.getResponseMessage());
			postconn.disconnect();
			Thread.sleep(5000);
			driver.findElement(By.partialLinkText("Proceed")).click();     
		}
		catch(Exception e) {
			System.out.println("exception: "+ e);
		}


		// *** FIFTH PART ***

		driver.findElement(By.partialLinkText("Download File")).click();
		Thread.sleep(5000);

		File file = new File("/home/swadhamakholia/Downloads/file_handle_test.dat");

		FileInputStream fileInput=null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();

		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}

		driver.findElement(By.id("signature")).sendKeys(prop.getProperty("Signature"));
		driver.findElement(By.className("submit")).click();
		System.out.println("property: "+prop.getProperty("Signature"));
		driver.close(); 

	}
}


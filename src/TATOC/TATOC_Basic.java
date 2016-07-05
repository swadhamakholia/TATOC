// Automate TATOC basic course.

package TATOC;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

public class TATOC_Basic {
	public static void main(String[] args) {

		// *** FIRST PART ***

		WebDriver driver = new FirefoxDriver();             
		driver.get("http://10.0.1.86/tatoc/basic/grid/gate");
		driver.findElement(By.className("greenbox")).click();

		// *** SECOND PART ***

		driver.switchTo().frame("main");
		String box1= driver.findElement(By.cssSelector("#answer")).getAttribute("class");

		driver.switchTo().frame("child");
		String box2= driver.findElement(By.cssSelector("#answer")).getAttribute("class");

		driver.switchTo().defaultContent();
		driver.switchTo().frame("main");
		while(!box1.equals(box2))
		{
			driver.findElement(By.linkText("Repaint Box 2")).click();
			driver.switchTo().defaultContent();
			driver.switchTo().frame("main");
			driver.switchTo().frame("child");
			box2= driver.findElement(By.cssSelector("#answer")).getAttribute("class");
			driver.switchTo().defaultContent();
			driver.switchTo().frame("main");
		}
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main");
		driver.findElement(By.linkText("Proceed")).click();    


		// *** THIRD PART***

		WebElement sbox = driver.findElement(By.id("dragbox")); 
		WebElement bbox = driver.findElement(By.id("dropbox"));

		(new Actions(driver)).dragAndDrop(sbox, bbox).perform();
		driver.findElement(By.partialLinkText("Proceed")).click();


		// *** FOURTH PART ***

		driver.findElement(By.linkText("Launch Popup Window")).click();
		String handle = driver.getWindowHandle(); //saves original window
		for(String winhand: driver.getWindowHandles()) {
			driver.switchTo().window(winhand);
		}

		driver.findElement(By.cssSelector("#name")).sendKeys("Swadha Makholia");
		driver.findElement(By.id("submit")).click();

		driver.switchTo().window(handle);
		driver.findElement(By.linkText("Proceed")).click();


		// *** FIFTH PART ***

		driver.findElement(By.linkText("Generate Token")).click();
		String token= driver.findElement(By.cssSelector("#token")).getText();

		System.out.println(token);
		String[] tok= token.split(": ");
		Cookie cookie = new Cookie("Token",tok[1]);
		driver.manage().addCookie(cookie);
		driver.findElement(By.linkText("Proceed")).click();
		System.out.println("cookie set");

	}
}

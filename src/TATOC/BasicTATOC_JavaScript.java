// Automate TATOC basic course using Java Script.

package TATOC;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BasicTATOC_JavaScript {
	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new FirefoxDriver();  
		JavascriptExecutor js = (JavascriptExecutor)driver;

		driver.get("http://10.0.1.86/tatoc/basic/grid/gate");

		// *** FIRST PART ***

		js.executeScript("document.getElementsByClassName(\"greenbox\")[0].click();");

		// *** SECOND PART ***

		String color1, color2;
		do{
			color1 = (String) js.executeScript("return document.getElementById(\"main\").contentWindow.document.getElementById(\"answer\").className;");
			color2 = (String) js.executeScript("return document.getElementById(\"main\").contentWindow.document.getElementById(\"child\").contentWindow.document.getElementById(\"answer\").className;");
			if(color1.equals(color2))
				break;
			js.executeScript("$(\"#main\").contents().find(\"a:contains(Repaint Box 2)\").click();");
			Thread.sleep(200);
		}while(true);
		js.executeScript("$(\"#main\").contents().find(\"a:contains(Proceed)\").click();");


		// *** THIRD PART***

		js.executeScript("document.getElementById(\"dropbox\").appendChild(document.getElementById(\"dragbox\"));"+
				"var d=document.getElementById(\"dragbox\");"+
				"d.style.position = \"absolute\";"+
				"d.style.left = document.getElementById(\"dropbox\").getBoundingClientRect().left;"+
				"$(\"a:contains(Proceed)\")[0].click();");	


		// *** FOURTH PART ***

		String parent_window = driver.getWindowHandle();
		js.executeScript("$(\"a:contains(Launch Popup Window)\").click();");
		for(String handle:driver.getWindowHandles()){
			driver.switchTo().window(handle);
		}
		js.executeScript("document.getElementById(\"name\").value=\"Harry Styles\";"+
				"document.getElementById(\"submit\").click();");
		driver.switchTo().window(parent_window);
		js.executeScript("$(\"a:contains(Proceed)\").click();");


		// *** FIFTH PART ***

		js.executeScript("$(\"a:contains(Generate Token)\").click();"+
				"var token = document.getElementById(\"token\").innerText.split(\": \")[1];"+
				"\"Token=\"+token;"+
				"document.cookie=\"Token=\"+token;"+
				"$(\"a:contains(Proceed)\").click();");
		driver.close();

	}
}



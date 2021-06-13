/**
 * 
 */
package hrcassignment4;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * @author padmanabha.das
 *
 */
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebDriverManager.chromedriver().setup();
		WebDriver chromeDriver = new ChromeDriver();
		chromeDriver.manage().window().maximize();
		chromeDriver.get("https://www.makemytrip.com/flight/reviewDetails/?itineraryId=19716f664b87e1099781c3a52fe7ef5f06ef9c51&rKey=RKEY:648a5822-9048-4763-b5b2-325bfcb715cf:25_0&crId=b66812b2-d96d-45a1-be1c-2534ace16d9a&cur=INR&openFF=undefined&xflt=eyJjIjoiRSIsInAiOiJBLTFfQy0yX0ktMSIsInQiOiIiLCJzIjoiQ0NVLU1BQS0yMDIxMDkwMyJ9&ccde=IN");
		
		chromeDriver.findElement(By.xpath("//span[contains(text(), '₹ 10')]")).click();
		JavascriptExecutor javascriptExecutor = (JavascriptExecutor) chromeDriver;
		javascriptExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		chromeDriver.findElement(By.xpath("//label[@for='charity']")).click();
	}

}

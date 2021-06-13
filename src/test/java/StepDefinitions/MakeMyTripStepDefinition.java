/**
 * 
 */
package StepDefinitions;

import static org.testng.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * @author padmanabha.das
 *
 */
public class MakeMyTripStepDefinition {

	static WebDriverWait wait;
	static WebDriver driver;
	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@SuppressWarnings({ "deprecation" })
	@Before
	public void setUpDriver() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		// Navigate to the website
		wait = new WebDriverWait(driver, 30);
	}

	@After
	public void quit() throws InterruptedException {
		Thread.sleep(7000);
		// driver.navigate().back();
		driver.quit();
	}

	@Given("^user navigates to makemytrip website$")
	public void user_navigates_to_makemytrip_website() throws InterruptedException {
		// Write code here that turns the phrase above into concrete actions
		driver.navigate().to("https://www.makemytrip.com/");
		System.out.println("Navigate to Make My Trip website" + ""
		                      + dateTimeFormatter.format(LocalDateTime.now()));
		Thread.sleep(1500);
		Actions actions = new Actions(driver);
		actions.moveByOffset(50, 0).click().perform();
		driver.findElement(By.xpath("//ul[@class='makeFlex font12']/descendant::a[1]")).click();
		Thread.sleep(2000);
	}

	@Given("^It is a \"([^\"]*)\" trip$")
	public void it_is_a_trip(String triptype) throws InterruptedException {
		// Write code here that turns the phrase above into concrete actions
		if (triptype.equals("round")) { // for RoundTrip
			/**
			 * To validate Round Trip 1. Click on Round Trip checkbox 2. Click on the cross
			 * of Return Date Calendar 3. Check Oneway checkbox is enabled/selected or not,
			 * if selected then Round Trip validated 4. Click on Round Trip checkbox again
			 */
			driver.findElement(By.xpath(
			                      "//div[@class='makeFlex']/descendant::li[contains(text(), 'Round Trip')]"))
			                      .click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//span[@class='returnCross landingSprite']")).click();
			assertTrue(driver.findElement(By.xpath(
			                      "//div[@class='makeFlex']/descendant::li[contains(text(), 'OneWay')]"))
			                      .isEnabled());
			Thread.sleep(3000);
			driver.findElement(By.xpath("//li[@data-cy='roundTrip']")).click();
			System.out.println(triptype + " validated");
			Thread.sleep(1000);
		} else if (triptype.equals("oneway")) { // Oneway
			/**
			 * To validate Oneway 1. Click on Return Date 2. Check Round Trip checkbox is
			 * enabled/selected or not, if selected then Oneway validated 3. Click outside
			 * to dismiss the return date calendar 4. Click on Oneway checkbox again
			 */
			driver.findElement(By.xpath(
			                      "//div[@class='fsw_inputBox dates reDates inactiveWidget ']"))
			                      .click();
			Thread.sleep(1000);
			assertTrue(driver.findElement(By.xpath(
			                      "//div[@class='makeFlex']/descendant::li[contains(text(), 'Round Trip')]"))
			                      .isEnabled());
			Actions actions = new Actions(driver);
			actions.moveByOffset(50, 0).click().perform();
			Thread.sleep(1000);
			driver.findElement(By.xpath(
			                      "//div[@class='makeFlex']/descendant::li[contains(text(), 'OneWay')]"))
			                      .click();
			System.out.println(triptype + " validated");
		}
	}

	@When("^Selecting options \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
	public void selecting_options(String fromcity, String tocity, String departuredate, String returndate,
	                      String triptype) throws ParseException, InterruptedException {
		// Write code here that turns the phrase above into concrete actions
		Thread.sleep(2000);
		selectFromCity(fromcity);
		selectToCity(tocity);
		selectDepartureDate(departuredate);
		if (triptype.equals("round"))
			selectReturnDate(departuredate, returndate);
	}

	@When("^Enter traveller type like \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
	public void enter_traveller_type_like(String adult, String children, String infant)
	                      throws InterruptedException {
		// Write code here that turns the phrase above into concrete actions
		Thread.sleep(2500);
		driver.findElement(By.xpath("//label[@for='travellers']")).click();
		Thread.sleep(1000);
		wait.until(ExpectedConditions
		                      .elementToBeClickable(By.xpath("//li[@data-cy='adults-" + adult + "']")))
		                      .click();

		wait.until(ExpectedConditions.elementToBeClickable(
		                      By.xpath("//li[@data-cy='children-" + children + "']"))).click();

		wait.until(ExpectedConditions
		                      .elementToBeClickable(By.xpath("//li[@data-cy='infants-" + infant + "']")))
		                      .click();

		wait.until(ExpectedConditions
		                      .elementToBeClickable(By.xpath("//button[@data-cy='travellerApplyBtn']")))
		                      .click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Search')]")))
		                      .click();
	}

	@And("^Handling Alert Box$")
	public void handling_alert_box() {
		// Write code here that turns the phrase above into concrete actions
		try {
			driver.findElement(By.xpath("//div[contains(@class, 'hsBackDrop')]")).click();
		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
		}
	}

	@And("^Selecting \"([^\"]*)\" flight$")
	public void selectingFlight(String flightName) {
		// Write code here that turns the phrase above into concrete actions
		driver.findElement(By.xpath("//span[text()='" + flightName + "']")).click();
	}

	@And("^Validating \"([^\"]*)\", fromcity, tocity in each rows of selection$")
	public void validating_in_each_rows_of_selection(String airline) throws InterruptedException {
		String flight = airline.toUpperCase();
		String filter = driver.findElement(By.xpath("//div[@class='filtersOuter']/descendant::li[1]"))
		                      .getText();
		Assert.assertEquals(filter, flight);
		String frmcity = driver.findElement(By.xpath("//input[@id='fromCity']")).getAttribute("value");
		String frm[] = frmcity.split(",");
		String tocity = driver.findElement(By.xpath("//input[@id='toCity']")).getAttribute("value");
		String to[] = tocity.split(",");
		List<WebElement> cardViews = driver.findElements(By.className("listingCard"));
		String[] splitStrings = new String[7];
		for (WebElement webElement : cardViews) {
			String str = null;
			splitStrings = webElement.getText().split("\n");
			if (splitStrings[6].equals("+ 1 DAY")) {
				splitStrings[6] = splitStrings[7];
			}
			Assert.assertTrue(splitStrings[0].contains(airline));
			if ((splitStrings[2].contains(frm[0]) && splitStrings[6].contains(to[0]))
			                      || (splitStrings[2].contains(to[0])
			                                            && splitStrings[6].contains(frm[0]))) {
				str = "Validation Successful";
			} else {
				str = "Validation not Successful";
			}
			Assert.assertEquals(str, "Validation Successful");
			System.out.println("Validation Successful");
		}
	}

	@And("^Validating \"([^\"]*)\", returndate of \"([^\"]*)\"$")
	public void validating_of(String departuredate, String triptype)
	                      throws InterruptedException, ParseException {
		String day = null;
		String month = null;
		String date = null;
		String year = null;
		String sday = null;
		String smonth = null;
		String sdate = null;
		String rday = null;
		String rmonth = null;
		String rdate = null;
		String returnday = null;
		String returnmonth = null;
		String returndate = null;
		String srday = null;
		String srmonth = null;
		String srdate = null;
		String dpardate = driver.findElement(By.xpath("//input[@id='departure']")).getAttribute("value");
		Pattern p1 = Pattern.compile("([\\w]+)\\,\\s([\\w]+)\\s(\\d)+\\,\\s([\\d]+)");
		Matcher m1 = p1.matcher(dpardate);
		if (m1.find()) {
			day = m1.group(1);
			month = m1.group(2);
			date = m1.group(3);
			year = m1.group(4);
		}
		if (triptype.equals("oneway")) {
			try {
				String sdparate = driver.findElement(By
				                      .xpath("//div[@class='weeklyFareItems active']/a/p"))
				                      .getText();
				Pattern p2 = Pattern.compile("([\\w]+)\\,\\s([\\w]+)\\s(\\d)");
				Matcher m2 = p2.matcher(sdparate);
				if (m2.find()) {
					sday = m2.group(1);
					smonth = m2.group(2);
					sdate = m2.group(3);
				}
				Assert.assertEquals(sday, day);
				Assert.assertEquals(smonth, month);
				Assert.assertEquals(sdate, date);
			} catch (Exception e) {
				// TODO: handle exception
				String dateParts[] = departuredate.split("-");
				String departureDate = dateParts[0];
				String departuremonth = dateParts[1];
				String departureyear = dateParts[2];
				SimpleDateFormat monthParse = new SimpleDateFormat("MM");
				SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM");
				departuremonth = monthDisplay.format(monthParse.parse(departuremonth));
				Assert.assertTrue(departuremonth.contains(month));
				Assert.assertEquals(date, departureDate);
				Assert.assertEquals(departureyear, year);
			}
		} else {
			// Thread.sleep(2000);
			String rodate = driver.findElement(By.xpath(
			                      "(//p[@class='fontSize16 blackText appendLR20 appendBottom20 paddingTop20'])[1]"))
			                      .getText();
			Pattern p3 = Pattern.compile("\\s+([\\w]+)\\,\\s(\\d)\\s([\\w]+)");
			Matcher m3 = p3.matcher(rodate);
			if (m3.find()) {
				rday = m3.group(1);
				rdate = m3.group(2);
				rmonth = m3.group(3);
			}
			Assert.assertEquals(rday, day);
			Assert.assertEquals(rmonth, month);
			Assert.assertEquals(rdate, date);
			String retrnndate = driver.findElement(By.xpath("//input[@id='return']"))
			                      .getAttribute("value");
			Matcher m4 = p1.matcher(retrnndate);
			if (m4.find()) {
				returnday = m4.group(1);
				returnmonth = m4.group(2);
				returndate = m4.group(3);
			}
			String srodate = driver.findElement(By.xpath(
			                      "(//p[@class='fontSize16 blackText appendLR20 appendBottom20 paddingTop20'])[2]"))
			                      .getText();
			Matcher m5 = p3.matcher(srodate);
			if (m5.find()) {
				srday = m5.group(1);
				srdate = m5.group(2);
				srmonth = m5.group(3);
			}
			Assert.assertEquals(srday, returnday);
			Assert.assertEquals(srmonth, returnmonth);
			Assert.assertEquals(srdate, returndate);
		}
	}

	@And("^Sort the grid in order to \"([^\"]*)\"$")
	public void sort_the_grid_in_order_to(String sorter) throws InterruptedException {
		// Write code here that turns the phrase above into concrete actions
		@SuppressWarnings("deprecation")
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By
		                      .xpath("//div[@class='sortby-dom-sctn " + sorter + "_sorter ']/span/span")))
		                      .click();
		System.out.println("Sorted");
	}

	@SuppressWarnings("deprecation")
	@And("^Selecting \"([^\"]*)\" row, \"([^\"]*)\" fare of \"([^\"]*)\"$")
	public void selecting_row_of(String nth, String fareOption, String triptype) throws InterruptedException {
		// Write code here that turns the phrase above into concrete actions
		if (triptype.equals("oneway")) {
			driver.findElement(By.xpath("(//span[@class='appendRight8'])[" + nth + "]")).click();
			if (fareOption.equals("Saver")) {
				driver.findElement(By.xpath("((//div[@class='viewFaresOuter '])[" + nth
				                      + "]//child::button)[1]")).click();
			} else if (triptype.equals("Flexi Plus")) {
				driver.findElement(By.xpath("((//div[@class='viewFaresOuter '])[" + nth
				                      + "]//child::button)[2]")).click();
			}
		} else if (triptype.equals("round")) {
			JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
			javascriptExecutor.executeScript("window.scrollBy(0, 250)");
			driver.findElement(By.xpath(
			                      "(((//div[@class='paneView'])[1])//child::span[@class='outer'])["
			                                            + nth + "]"))
			                      .click();
			Thread.sleep(2000);
			driver.findElement(By.xpath(
			                      "(((//div[@class='paneView'])[2])//child::span[@class='outer'])["
			                                            + nth + "]"))
			                      .click();
			driver.findElement(By.xpath("//button[contains(text(), 'Book Now')]")).click();
			System.out.println("Book Now");
			Thread.sleep(2000);
			new WebDriverWait(driver,
			                      20).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='OverlayContent loader-full-page-overlay']")));
			Thread.sleep(2000);
			driver.findElement(By.xpath(
			                      "(//div[@class='multifareContentRight']/descendant::div[@class='multifareCardHead makeFlex appendBottom5'])[1]"))
			                      .click();
			Thread.sleep(2000);
			driver.findElement(By.xpath(
			                      "(//div[@class='multifareContentRight']/descendant::div[@class='multifareCardHead makeFlex appendBottom5'])[3]"))
			                      .click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//button[text()='Continue']")).click();
			System.out.println("Continue");
		}
	}

	@And("^Verifying the details of \"([^\"]*)\", \"([^\"]*)\" of  \"([^\"]*)\", \"([^\"]*)\"$")
	public void verifying_the_details_of_of(String fromcity, String tocity, String Airline, String triptype)
	                      throws InterruptedException {
		// Write code here that turns the phrase above into concrete actions
		Set<String> window_name = driver.getWindowHandles();
		int window = 0;
		for (String i : window_name) {
			if (window == 1)
				driver.switchTo().window(i);
			window++;
		}

		ArrayList<String> Dept_name = new ArrayList<String>();
		ArrayList<String> arrival_nameList = new ArrayList<String>();
		List<WebElement> dept_name = driver.findElements(By.xpath(
		                      "//p[text()='DEPART']//ancestor::div[@class='rvw-sctn append_bottom15 ']//p[@class='dept-city']//span[@class='LatoBold']"));
		for (WebElement k : dept_name) {
			Dept_name.add(k.getText().toString());
		}

		List<WebElement> arrival_name = driver.findElements(By.xpath(
		                      "//p[text()='DEPART']//ancestor::div[@class='rvw-sctn append_bottom15 ']//p[@class='arrival-city']//span[@class='LatoBold']"));
		for (WebElement k : arrival_name) {
			Dept_name.add(k.getText().toString());
		}
		if (triptype.equals("Round Trip")) {
			List<WebElement> dept_name_ret = driver.findElements(By.xpath(
			                      "//p[text()='RETURN']//ancestor::div[@class='rvw-sctn append_bottom15 ']//p[@class='dept-city']//span[@class='LatoBold']"));
			for (WebElement k : dept_name_ret) {
				arrival_nameList.add(k.getText().toString());
			}

			List<WebElement> arrival_name_ret = driver.findElements(By.xpath(
			                      "//p[text()='RETURN']//ancestor::div[@class='rvw-sctn append_bottom15 ']//p[@class='arrival-city']//span[@class='LatoBold']"));
			for (WebElement k : arrival_name_ret) {
				arrival_nameList.add(k.getText().toString());
			}
			Assert.assertEquals(arrival_nameList.get(0), tocity);
			Assert.assertEquals(arrival_nameList.get(arrival_nameList.size() - 1), fromcity);
		}

		Assert.assertEquals(Dept_name.get(0), fromcity);
		Assert.assertEquals(Dept_name.get(Dept_name.size() - 1), tocity);

		List<WebElement> flight = driver
		                      .findElements(By.xpath("//p[@class='append_bottom5 font14 LatoBold']"));
		for (WebElement k : flight) {
			Assert.assertEquals(k.getText().toString(), Airline);
		}
		Thread.sleep(2000);
	}

	@And("^Remove Charity option$")
	public void remove_charity_option() throws InterruptedException {
		// Write code here that turns the phrase above into concrete actions
		Thread.sleep(2000);
		wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Other Services']")))
		                      .click();
		Thread.sleep(2000);
		String otherService = driver.findElement(By.xpath(
		                      "//span[text()='Other Services']//ancestor::div[@class='fareSmry-header LatoBold']//following-sibling::div/p/span"))
		                      .getText().toString();
		if (otherService.equals("Charity")) {
			System.out.println("true");
			wait.until(ExpectedConditions.elementToBeClickable(
			                      By.xpath("//label[@for= 'charity']/span[@class='box']")))
			                      .click();
		}
		Thread.sleep(2000);
		Actions actions = new Actions(driver);

		actions.sendKeys(Keys.HOME).build().perform();
		// System.out.println(otherServicerem);
		try {
			driver.findElement(By.xpath(
			                      "//span[text()='Other Services']//ancestor::div[@class='fareSmry-header LatoBold']//following-sibling::div/p/span"));
			System.out.println("charity not removed");
		} catch (Exception e) {
			System.out.println("Charity  removed ");
		}
		Thread.sleep(2000);
		actions.sendKeys(Keys.PAGE_DOWN).build().perform();
	}

	@And("^Validate total fare$")
	public void validate_total_fare() throws InterruptedException {
		// Write code here that turns the phrase above into concrete actions
		Thread.sleep(2000);

		Pattern priceRegex = Pattern.compile("₹\\s([\\d\\,]*)");
		int totalPrice = 0;
		int displayedPrice = 0;
		for (int i = 1; i <= driver.findElements(
		                      By.xpath("//div[@class='fareSmry-sctn']//span[contains(@class,'font16')]"))
		                      .size(); i++) {
			Matcher price = priceRegex.matcher(driver.findElement(By.xpath(
			                      "(//div[@class='fareSmry-sctn']//span[contains(@class,'font16')])["
			                                            + i + "]"))
			                      .getText());
			if (price.find()) {
				totalPrice += Integer.parseInt(price.group(1).replaceAll(",", ""));
			}
		}

		Matcher displayedTotalPrice = priceRegex.matcher(driver.findElement(
		                      By.xpath("(//div[@class='fareSmry-sctn reqPad-fareSmry-sctn']//span)[6]"))
		                      .getText());
		if (displayedTotalPrice.find()) {
			displayedPrice = Integer.parseInt(displayedTotalPrice.group(1).replaceAll(",", ""));
		}
		Assert.assertEquals(totalPrice, displayedPrice, "Prices do not match");
		System.out.println("Fare Summary Validation");

	}

	@And("^Check the insurance$")
	public void check_the_insurance() {
		// Write code here that turns the phrase above into concrete actions
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			driver.findElement(By.xpath(
			                      "//div[@class=\"darkGrayText\"]//span[@class=\"labeltext LatoBold\"]"))
			                      .click();
		} catch (Exception e) {
			System.out.println("Insurance not present");
		}
	}

	@Then("^Navigating back$")
	public void navigating_back() {
		// Write code here that turns the phrase above into concrete actions
		driver.close();
	}

	public void selectFromCity(String fromcity) throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[contains(@class, 'inputBox searchCity inactiveWidget ')]"))
		                      .click();
		driver.findElement(By.xpath("//input[@placeholder='From']")).sendKeys(fromcity);
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//p[contains(@class, 'font14 appendBottom5 blackText')])[1]"))
		                      .click();
	}

	public void selectToCity(String tocity) throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[contains(@class, 'inputBox searchToCity inactiveWidget ')]"))
		                      .click();
		driver.findElement(By.xpath("//input[@placeholder='To']")).sendKeys(tocity);
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//p[contains(@class, 'font14 appendBottom5 blackText')])[1]"))
		                      .click();
	}

	@SuppressWarnings("deprecation")
	public void selectDepartureDate(String departureDate) throws ParseException, InterruptedException {
		new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(
		                      By.xpath("//div[@class='fsw_inputBox dates inactiveWidget ']"))).click();
		String departureDateParts[] = departureDate.split("-");
		String departureDateString = departureDateParts[0];
		String departureMonthString = departureDateParts[1];
		String departureYearString = departureDateParts[2];
		String localDate = LocalDate.now().toString();
		String localDateString[] = localDate.split("-");
		String localMonthString = localDateString[1];
		int monthDiff = Integer.parseInt(departureMonthString) - Integer.parseInt(localMonthString);
		System.out.println(monthDiff);
		while (monthDiff > 0) {
			Thread.sleep(1500);
			driver.findElement(By.xpath("//span[@aria-label='Next Month']")).click();
			monthDiff--;
		}
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[contains(@aria-label, '" + departureDateString + " "
		                      + departureYearString + "')]")).click();
	}

	public void selectReturnDate(String departureDate, String returnDate) throws InterruptedException {
		Thread.sleep(3000);
		Actions actions = new Actions(driver);
		actions.moveByOffset(50, 0).click().perform();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@class='fsw_inputBox dates reDates inactiveWidget  shiftModal']")).click();
		String returnDateParts[] = returnDate.split("-");
		String returnDateString = returnDateParts[0];
		String returnMonthString = returnDateParts[1];
		String returnYearString = returnDateParts[2];
		String departureDateParts[] = departureDate.split("-");
		String departureMonthString = departureDateParts[1];
		int monthDiff = Integer.parseInt(returnMonthString) - Integer.parseInt(departureMonthString);
		System.out.println(monthDiff);
		while (monthDiff > 0) {
			Thread.sleep(1500);
			driver.findElement(By.xpath("//span[@aria-label='Next Month']")).click();
			monthDiff--;
		}
		Thread.sleep(2000);
		if (Integer.parseInt(returnDateString)<10) {
			returnDateString = 0 + returnDateString;
		}
		driver.findElement(By.xpath("//div[contains(@aria-label, '" + returnDateString + " "
		                      + returnYearString + "')]")).click();
	}

}

package ui;

import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HomePage;

public class TagHeaderTest
{
  private WebDriver driver;

  @BeforeMethod
  void init()
  {
    System.setProperty("webdriver.chrome.driver", "C:\\Users\\ibelyakov\\Downloads\\chromedriver\\chromedriver.exe");
    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    driver.manage().window().maximize();
    driver.get("https://demo.realworld.io");
  }

  @Owner("Iurii Belyakov")
  @Severity(SeverityLevel.MINOR)
  @Test(description = "Global Feed tag test")
  void filterHeaderTest()
  {
    SoftAssert softAssert = new SoftAssert();
    HomePage homePage = new HomePage(driver);
    List<WebElement> popularTags = homePage.getPopularTags();
    popularTags.forEach(popularTag ->
    {
      waitForPreview();
      popularTag.click();

      String headerTag = homePage.getHeaderTag();
      softAssert.assertNotNull(headerTag, "Header tag " + popularTag + " is absent");
      softAssert.assertEquals(headerTag, popularTag.getText(), "Incorrect header tag.");
    });
    softAssert.assertAll();
  }


  private void waitForPreview()
  {
    new WebDriverWait(driver, 10)
        .until(ExpectedConditions.presenceOfElementLocated(By.tagName("article-preview")));
  }


  @AfterMethod
  public void finish()
  {
    if (driver != null)
    {
      driver.quit();
    }
  }
}

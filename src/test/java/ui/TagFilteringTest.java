package ui;

import elements.ArticlePreview;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.Article;
import pages.HomePage;
import utils.BrowserUtils;

public class TagFilteringTest
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
  @Test(description = "Tag filtering test")
  void tagFilterTest()
  {
    SoftAssert softAssert = new SoftAssert();
    HomePage homePage = new HomePage(driver);
    List<WebElement> popularTags = homePage.getPopularTags();
    List<WebElement> pages = homePage.getPages();
    popularTags.forEach(popularTag ->
    {
      waitForPreview();
      popularTag.click();
      for (WebElement page : pages)
      {
        scrollDown();

        WebElement isLoading = driver.findElement(By.tagName("article-list"))
            .findElements(By.className("ng-hide")).get(0);

        page.click();
        new WebDriverWait(driver, 10)
            .until(ExpectedConditions.invisibilityOf(isLoading));
        List<ArticlePreview> articlesPreviews = homePage.getArticlePreviews();
        checkPreview(articlesPreviews, popularTag.getText(), softAssert, page.getText());
        checkArticles(articlesPreviews, popularTag.getText(), softAssert);
      }
    });
    softAssert.assertAll();
  }

  private void scrollDown()
  {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("scroll(0, 25000);");
  }

  private void waitForPreview()
  {
    new WebDriverWait(driver, 10)
        .until(ExpectedConditions.presenceOfElementLocated(By.tagName("article-preview")));
  }

  private void checkPreview(List<ArticlePreview> articlePreviews, String popularTag, SoftAssert softAssert, String pageNumber)
  {
    for (ArticlePreview articlePreview : articlePreviews)
    {
      List<String> assignedTags = articlePreview.getTags();
      softAssert.assertTrue(assignedTags.contains(popularTag),
          "\nSelected tag " + "'" + popularTag + "'"
              + " is missing for article preview on page " + pageNumber + ":\n "
              + articlePreview.toString() + "\n");
    }
  }

  private void checkArticles(List<ArticlePreview> articlePreviews, String popularTag, SoftAssert softAssert)
  {
    for (ArticlePreview articlePreview : articlePreviews)
    {
      WebElement previewLink = articlePreview.getPreviewLink();
      BrowserUtils.openInNewTab(previewLink, driver);
      String mainTab = driver.getWindowHandle();
      List<String> tabs = new ArrayList<>(driver.getWindowHandles());
      driver.switchTo().window(tabs.get(1));
      checkArticle(driver, popularTag, softAssert);

      driver.close();
      driver.switchTo().window(mainTab);
    }
  }

  private void checkArticle(WebDriver driver, String popularTag, SoftAssert softAssert)
  {
    Article article = new Article(driver);
    List<String> assignedTags = article.getTags();
    softAssert.assertTrue(assignedTags.contains(popularTag), "Article " + article.getLink()
        + " has no selected tag " + popularTag);
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

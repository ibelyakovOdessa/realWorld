package pages;

import elements.ArticlePreview;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.testng.util.Strings;

public class HomePage
{
  private WebDriver driver;

  @FindBys({
          @FindBy(className = "sidebar"),
          @FindBy(className = "tag-list"),
          @FindBy(className = "tag-default")
      })
  private List<WebElement> popularTags;

  @FindBy(tagName = "article-preview")
  private List<WebElement> articlePreviews;

  @FindBy(className = "feed-toggle")
  private WebElement globalFeed;

  @FindBy(className = "page-link")
  private List<WebElement> pages;

  public HomePage(WebDriver driver)
  {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public List<WebElement> getPopularTags()
  {
    return popularTags;
  }

  public List<ArticlePreview> getArticlePreviews()
  {
    return articlePreviews
        .stream()
        .map(ArticlePreview::new)
        .collect(Collectors.toList());
  }

  public String getHeaderTag()
  {
    WebElement element = globalFeed.findElements(By.tagName("li"))
        .stream()
        .filter(webElement -> !Strings.isNullOrEmpty(webElement.getAttribute("ng-show")))
        .filter(webElement -> webElement.getAttribute("ng-show").contains("tag"))
        .findFirst().get();
    return element.findElement(By.tagName("a")).getText();
  }

  public List<WebElement> getPages()
  {
    return pages;
  }
}

package pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

public class Article
{
  private WebDriver driver;

  @FindBys({
      @FindBy(className = "tag-list"),
      @FindBy(className = "tag-default"),
  })
  private List<WebElement> assignedTags;

  public Article(WebDriver driver)
  {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public List<String> getTags()
  {
    return assignedTags.stream()
        .map(WebElement::getText)
        .collect(Collectors.toList());
  }

  public String getLink()
  {
    return driver.getCurrentUrl();
  }

}

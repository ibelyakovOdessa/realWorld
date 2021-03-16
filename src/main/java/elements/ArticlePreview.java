package elements;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ArticlePreview
{
  private WebElement preview;
  private String author;
  private String title;
  private String description;
  private List<String> tags;
  private WebElement previewLink;

  public ArticlePreview(WebElement preview)
  {
    this.preview = preview;
    init();
  }

  private void init()
  {
    setAuthor();
    setTags();
    setDescription();
    setTitle();
    setPreviewLink();
  }

  private void setAuthor()
  {
    author = preview.findElement(By.className("author")).getText();
  }

  private void setTitle()
  {
    title = preview.findElement(By.className("preview-link"))
        .findElements(By.className("ng-binding"))
        .stream()
        .filter(webElement -> webElement.getAttribute("ng-bind").contains("title"))
        .map(WebElement::getText)
        .findFirst().orElse("");
  }

  private void setDescription()
  {
    description = preview.findElement(By.className("preview-link"))
        .findElements(By.className("ng-binding"))
        .stream()
        .filter(webElement -> webElement.getAttribute("ng-bind").contains("description"))
        .map(WebElement::getText)
        .findFirst().orElse("");
  }

  private void setPreviewLink()
  {
    previewLink = preview.findElement(By.className("preview-link"));
  }

  private void click()
  {
    previewLink.click();
  }

  private void setTags()
  {
    tags = preview.findElement(By.className("tag-list"))
        .findElements(By.className("tag-default"))
        .stream()
        .map(WebElement::getText)
        .collect(Collectors.toList());
  }

  public WebElement getPreviewLink()
  {
    return previewLink;
  }

  public String getAuthor()
  {
    return author;
  }

  public String getTitle()
  {
    return title;
  }

  public String getDescription()
  {
    return description;
  }

  public List<String> getTags()
  {
    return tags;
  }

  public String toString()
  {
    return "[Title: " + title + "\n"
        + "Description: " + description + "\n"
        + "Author: " + author + "]";
  }
}

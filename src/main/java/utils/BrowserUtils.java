package utils;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class BrowserUtils
{

  /**
   * opens link in a new tab
   * @param link link
   * @param driver webDriver
   */
  public static void openInNewTab(WebElement link, WebDriver driver)
  {
    new Actions(driver)
        .moveToElement(link)
        .keyDown(Keys.CONTROL)
        .sendKeys("t")
        .click(link)
        .keyUp(Keys.CONTROL)
        .build()
        .perform();
  }
}

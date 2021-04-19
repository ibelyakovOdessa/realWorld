package api;

import endpoints.Endpoints;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TagFilteringApiTest
{
  private static final int LIMIT = 10;

  @BeforeTest
  @DataProvider(name = "popularTags")
  public Object[][] getPopularTags()
  {
    List<String> popularTags = RestAssured.given()
        .baseUri("http://conduit.productionready.io")
        .basePath("api")
        .contentType(ContentType.JSON)
        .when()
        .get(Endpoints.TAGS)
        .then()
        .extract()
        .body().jsonPath().get("tags");

    int articlesCount = RestAssured.given()
        .baseUri("http://conduit.productionready.io")
        .basePath("api")
        .contentType(ContentType.JSON)
        .when()
        .get(Endpoints.ARTICLES)
        .then()
        .extract()
        .body().jsonPath().get("articlesCount");


    return getData(articlesCount, popularTags);
  }

  @Test(dataProvider = "popularTags")
  void TagFilteringAPItest(String tag, int offset)
  {
    RestAssured.given()
        .baseUri("http://conduit.productionready.io")
        .basePath("api")
        .contentType(ContentType.JSON)
        .queryParam("limit", LIMIT)
        .queryParam("offset", offset)
        .queryParam("tag", tag)
        .when()
        .get(Endpoints.ARTICLES)
        .then()
        .statusCode(200)
        .and()
        .body(String.format("articles.findAll {!it.tagList.contains('%s')}", tag), Matchers.empty());
  }

  private Object[][] getData(int count, List<String> tags)
  {
    List<Object[]> data = new ArrayList<>();
    for (String tag : tags)
    {
      data.add(new Object[]{tag, 0});
      data.add(new Object[]{tag, count / 2});
      data.add(new Object[]{tag, count - LIMIT});
    }
    return data.toArray(Object[][]::new);
  }
}

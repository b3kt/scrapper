package com.github.b3kt.scraper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ScraperResourceTest {

	  @Test
	    public void testEndpoint() {
	        given()
	                .when().get("/")
	                .then()
	                .statusCode(200)
	                .body(containsString("<title>Tokopedia Web Scraper</title>"));
	    }
}

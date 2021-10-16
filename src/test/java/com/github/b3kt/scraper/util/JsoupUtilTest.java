package com.github.b3kt.scraper.util;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;

import com.github.b3kt.scraper.bean.Product;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JsoupUtilTest {

  @Test
  public void searchProductTest() {
    List<Product> products = JsoupUtil.searchProduct("https://www.tokopedia.com/search?st=product", "handphone", "1");
    assertTrue(CollectionUtils.isNotEmpty(products));
  }
}

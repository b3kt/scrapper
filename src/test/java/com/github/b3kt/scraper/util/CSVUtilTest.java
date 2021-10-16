package com.github.b3kt.scraper.util;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.b3kt.scraper.bean.Product;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CSVUtilTest {
	
  private Logger logger = LoggerFactory.getLogger("CSVUtilTest");
	
  private File file = new File("/tmp/tokped.csv"); 
  private List<Product> list = new ArrayList<>();	
	
  @Test
  public void writeCSVTest() {
    try {
    	list = new ArrayList<>();
    	list.add(new Product("product11", "desc", "link", "price", "5", "merchant"));
    	list.add(new Product("product12", "desc", "link", "price", "5", "merchant"));
    	list.add(new Product("product13", "desc", "link", "price", "5", "merchant"));
    	
    	//WRITE TEST
		CSVUtil.writeCSV(file.getPath(), list);
		assertTrue(file.exists());
		
		//READ TEST
		list.clear();
		list = CSVUtil.readCSV(file.getPath());
    	logger.info("{}", list);
    	assertTrue(CollectionUtils.isNotEmpty(list));  
    	
    	Files.deleteIfExists(file.toPath());
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		assertTrue(false);
	}
	  
  }
  
}

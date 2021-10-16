package com.github.b3kt.scraper.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.b3kt.scraper.bean.Product;

public class JsoupUtil {
	
	private static Logger logger = LoggerFactory.getLogger(JsoupUtil.class);
	
	public static String escapeSpecialCharacters(String data) {
		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			data = data.replace("\"", "\"\"");
			escapedData = "\"" + data + "\"";
		}
		return escapedData;
	}

    public static List<Product> searchProduct(String url, String query, String page) {
        List<Product> results = new ArrayList<>();

        try {
        	StringBuilder urlBuilder = new StringBuilder(url);

        	if(StringUtils.isNotBlank(query)) {
        		urlBuilder.append("&q=").append(query);
        	}
        	
        	if(StringUtils.isNotBlank(page)) {
        		urlBuilder.append("&page=").append(page);
        	}
        	
        	logger.info("Using URL {} ", urlBuilder.toString());
        	Document doc = Jsoup.connect(urlBuilder.toString()).get();
            
            if(doc != null) {
            	Elements content = doc.getElementsByAttributeValue("data-testid", "divSRPContentProducts");
            		Elements links = content.get(0).getElementsByAttributeValue("data-testid", "divProductWrapper");
            		if(!links.isEmpty()) {
            			for (Element card : links) {
            				Product product = new Product();
            				
            				// GET PRODUCT NAME
            				Elements obj = card.getElementsByTag("a");
            				String productName = escapeSpecialCharacters(obj.get(1).attr("title"));
            				product.setProductName(productName);
            				
            				String desc = "";
            				String href = obj.get(1).attr("href");
            				if (!href.contains("clicks")) {
            					Document subDoc = Jsoup.connect(href).get();
            					
            					// GET DESCRIPTION
            					Elements dtls = subDoc.getElementsByAttributeValue("data-testid", "lblPDPInfoProduk");
            					desc = escapeSpecialCharacters(dtls.text());
            				}
            				product.setDescription(desc);
            				
            				// GET IMAGE LINK
            				Elements imgs = obj.get(0).getElementsByTag("img");
            				String imgLink = escapeSpecialCharacters(imgs.get(0).attr("src"));
            				product.setImageLink(imgLink);
            				
            				// GET PRICE
            				Elements price = obj.get(1).getElementsByAttributeValue("data-testid", "spnSRPProdPrice");
            				product.setPrice(price.get(0).text());
            				
            				
            				Elements rating = obj.get(1).getElementsByAttributeValue("data-productinfo", "true");
            				Elements span = rating.get(0).getElementsByTag("span");
            				String rate = "";
            				String merchantName = "";
            				
            				if (span.size() > 2) {
            					// GET RATING
            					rate = span.get(2).text();
            				}
            				
            				if (span.size() > 1) {
            					// GET MERCHANT NAME
            					merchantName = span.get(1).text();
            				}
            				
            				product.setMerchantName(merchantName);            				
            				product.setRating(rate);
            				
            				results.add(product);
            			}
            		}
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return results;
    }
}

package com.github.b3kt.scraper.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.b3kt.scraper.bean.Product;
import com.github.b3kt.scraper.util.CSVUtil;
import com.github.b3kt.scraper.util.JsoupUtil;
import com.google.gson.Gson;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("/")
public class ScraperResource {

	Logger logger = LoggerFactory.getLogger(ScraperResource.class);

	@ConfigProperty(name = "scraper.url.tokopedia")
	String scraperUrl;

	@ConfigProperty(name = "scraper.result.path")
	String scraperResultPath;

	@Inject
	Template scraper;

	@GET
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance get(@QueryParam("name") String name) {
		return scraper.data("targetUrl", scraperUrl).data("resultPath", scraperResultPath).data("data", null)
				.data("query", "handphone").data("name", name);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance triggerSearch(@FormParam("q") String query) {
		final String finalURL = scraperUrl.concat("&q=").concat(query);
		final String finalPath = scraperResultPath.replace(".csv", "-" + query + ".csv");

		List<Product> allList = new ArrayList<>();

		try {
			Integer page = 1;
			while (true) {
				List<Product> productList = JsoupUtil.searchProduct(finalURL, query, page.toString());
				allList.addAll(productList);
				Gson gson = new Gson();
				logger.info("results : {}", gson.toJson(productList));

				try {
					CSVUtil.writeCSV(finalPath, productList);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}

				long lines = CSVUtil.countLines(new File(finalPath));
				if (lines >= 100L) {
					break;
				}

				page++;
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return scraper.data("targetUrl", finalURL).data("data", allList).data("query", query).data("resultPath",
				finalPath);
	}

}

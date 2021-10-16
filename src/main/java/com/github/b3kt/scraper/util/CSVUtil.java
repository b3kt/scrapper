package com.github.b3kt.scraper.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.github.b3kt.scraper.bean.Product;

public class CSVUtil {

	private static Logger logger = LoggerFactory.getLogger("CVSUtil");

	private static CellProcessor[] getProcessors() {
		return new CellProcessor[] { new NotNull(), new NotNull(), new NotNull(), new NotNull(), new NotNull(),
				new NotNull() };
	}

	@SuppressWarnings("deprecation")
	private static boolean checkFileContainsData(String targetPath, String header) throws IOException {
		File file = new File(targetPath);

		if (!file.exists()) {
			if (file.createNewFile()) {
				// FileOutputStream oFile = new FileOutputStream(file, false);
				logger.info("file not exists, creating..");
			}
		}

		return FileUtils.readFileToString(file).contains(header);
	}

	public static long countLines(File file) throws IOException {
		long lineCount;
		try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
			lineCount = stream.count();
		}
		return lineCount;
	}

	public static void writeCSV(String targetPath, List<Product> productList) throws Exception {

		ICsvBeanWriter beanWriter = null;
		try {

			File file = new File(targetPath);

			// the header elements are used to map the bean values to each column (names
			// must match)
			final String[] header = new String[] { "productName", "description", "imageLink", "price", "rating",
					"merchantName" };
			final CellProcessor[] processors = getProcessors();

			final boolean append = checkFileContainsData(targetPath, String.join(",", header));
			beanWriter = new CsvBeanWriter(new FileWriter(targetPath, append), CsvPreference.STANDARD_PREFERENCE);

			// write the header
			if (!append) {
				beanWriter.writeHeader(header);
			}

			// write the beans
			if (CollectionUtils.isNotEmpty(productList)) {
				for (final Product product : productList) {
					if (countLines(file) <= 101) {
						beanWriter.write(product, header, processors);
					}
				}
			}

		} finally {
			if (beanWriter != null) {
				beanWriter.close();
			}
		}
	}

	public static List<Product> readCSV(String filePath) throws IOException {

		List<Product> results = new ArrayList<>();
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(filePath), CsvPreference.STANDARD_PREFERENCE);

			// the header elements are used to map the values to the bean (names must match)
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();

			Product product;
			while ((product = beanReader.read(Product.class, header, processors)) != null) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, product=%s", beanReader.getLineNumber(),
						beanReader.getRowNumber(), product));

				results.add(product);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
		return results;
	}
}

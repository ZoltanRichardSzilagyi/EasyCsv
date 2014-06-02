package hu.boot.easycsv.cellprocessor;

import hu.boot.easycsv.configuration.CsvColumnBeanFieldMapping;

import java.util.Date;

import javax.xml.bind.JAXBContext;

import org.junit.Assert;
import org.junit.Test;

public class CellProcessorFactoryTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testCellProcessorFactory() throws Exception {
		CellProcessorFactory cellProcessorFactory = new CellProcessorFactory();
		CellProcessor<Date> cellProcessor = (CellProcessor<Date>) cellProcessorFactory
				.getCellProcessor(Date.class);
		Assert.assertNotNull(cellProcessor);

		CsvColumnBeanFieldMapping mapping = new CsvColumnBeanFieldMapping();
		mapping.setFormat("yyyy-MM-dd");

		CellProcessor<Date> testCellProcessor = new DateCellProcessor();
		String dateValue = "1985-07-27";
		Date testDate = testCellProcessor.process(mapping, dateValue);
		Date date = cellProcessor.process(mapping, dateValue);
		Assert.assertEquals(date, testDate);

		CellProcessor<?> nullCellProcessor = cellProcessorFactory
				.getCellProcessor(JAXBContext.class);
		Assert.assertNull(nullCellProcessor);

		CellProcessor<?> stringCellProcessor = cellProcessorFactory
				.getCellProcessor(String.class);
		Assert.assertNotNull(stringCellProcessor);
	}
}

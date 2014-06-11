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
		final CellProcessorFactory cellProcessorFactory = new CellProcessorFactory();
		final CellProcessor<Date> cellProcessor = cellProcessorFactory
				.getCellProcessor(Date.class);
		Assert.assertNotNull(cellProcessor);

		final CellProcessor<?> nullCellProcessor = cellProcessorFactory
				.getCellProcessor(JAXBContext.class);
		Assert.assertNull(nullCellProcessor);

		final CellProcessor<?> stringCellProcessor = cellProcessorFactory
				.getCellProcessor(String.class);
		Assert.assertNotNull(stringCellProcessor);

		final CellProcessor<?> integerCellProcessor = cellProcessorFactory
				.getCellProcessor(Integer.class);
		Assert.assertNotNull(integerCellProcessor);

	}

	@Test
	@SuppressWarnings("unchecked")
	public void dateCellProcessorTest() throws Exception {
		final CellProcessorFactory cellProcessorFactory = new CellProcessorFactory();
		final CellProcessor<Date> cellProcessor = cellProcessorFactory
				.getCellProcessor(Date.class);
		Assert.assertNotNull(cellProcessor);

		final CsvColumnBeanFieldMapping mapping = new CsvColumnBeanFieldMapping();
		mapping.setFormat("yyyy-MM-dd");
		final CellProcessor<Date> testCellProcessor = new DateCellProcessor();
		final String dateValue = "1985-07-27";
		final Date testDate = testCellProcessor.process(mapping, dateValue);
		final Date date = cellProcessor.process(mapping, dateValue);
		Assert.assertEquals(date, testDate);
	}

	@Test
	public void stringCellProcessorTest() throws Exception {
		final CellProcessor<String> cellProcessor = new StringCellProcessor();
		final String value = cellProcessor.process(null, "text");
		Assert.assertEquals("text", value);
	}

	@Test
	public void integerCellProcessorTest() throws Exception {
		final CellProcessor<Integer> integerCellProcessor = new IntegerCellProcessor();
		final Integer value = integerCellProcessor.process(null, "20");
		Assert.assertEquals((Integer) 20, value);
	}

	@Test
	public void booleanCellProcessorTest() throws Exception {
		final CellProcessor<Boolean> cellProcessor = new BooleanCellProcessor();
		Boolean value = cellProcessor.process(null, "true");
		Assert.assertEquals(true, value);

		value = cellProcessor.process(null, "false");
		Assert.assertEquals(false, value);

		value = cellProcessor.process(null, "yes");
		Assert.assertEquals(true, value);
	}
}

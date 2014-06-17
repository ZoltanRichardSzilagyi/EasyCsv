package hu.boot.easycsv;

import hu.boot.easycsv.CsvParser.CsvParserBuilder;
import hu.boot.easycsv.cellprocessor.CellProcessorFactory;
import hu.boot.easycsv.configuration.CsvBeanMapping;
import hu.boot.easycsv.configuration.CsvReaderConfiguration;
import hu.boot.easycsv.configuration.ReflectionCsvBeanMapper;

import java.io.ByteArrayInputStream;

import org.apache.commons.io.input.NullInputStream;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Test;

public class CsvParserTest {

	@Test
	public void testParserBuilder() {

		final CsvParserBuilder<User> builder = new CsvParserBuilder<User>();
		builder.setData(new NullInputStream(0));
		final CsvReaderConfiguration configuration = new CsvReaderConfiguration();
		builder.setConfiguration(configuration);
		final CsvParser<User> parser = builder.build();
		Assert.assertNotNull(parser);

	}

	@Test
	public void testParser() throws EasyCsvException {
		final String data = "name,age" + "\r\n" + "john,28,"
				+ new DateTime().toString(ISODateTimeFormat.dateTime());
		final ByteArrayInputStream stream = new ByteArrayInputStream(
				data.getBytes());

		final CsvParserBuilder<User> builder = new CsvParserBuilder<User>();
		builder.setData(stream);
		final CsvReaderConfiguration configuration = new CsvReaderConfiguration();
		configuration.setBeanType(User.class);
		configuration.setCsvHeaderColumns(new String[] { "name", "age",
				"birth-date" });

		final ReflectionCsvBeanMapper beanMapper = new ReflectionCsvBeanMapper(
				User.class);
		final CsvBeanMapping mapping = beanMapper.createMapping();

		configuration.setCsvBeanMapping(mapping);
		builder.setConfiguration(configuration);
		builder.setCellProcessorFactory(new CellProcessorFactory());
		final CsvParser<User> parser = builder.build();
		Assert.assertNotNull(parser);
		final CsvReadResult<User> readResult = parser.parse();
		Assert.assertEquals(0, readResult.getErrors().size());
	}
}

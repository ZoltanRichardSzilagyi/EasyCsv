package hu.boot.easycsv.configuration;

import hu.boot.easycsv.EasyCsvException;
import hu.boot.easycsv.User;
import hu.boot.easycsv.bean.CsvColumn;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionCsvBeanMapperTest {

	private static final Logger logger = LoggerFactory
			.getLogger(ReflectionCsvBeanMapper.class);

	@Test
	public void testMapping() throws EasyCsvException {
		final ReflectionCsvBeanMapper mapper = new ReflectionCsvBeanMapper(
				User.class);
		final CsvBeanMapping mapping = mapper.createMapping();
		logger.info(mapping.getColumnsNum() + " mapping columns num");
		testColumnsAndAnnotatedFieldsAreEquals(mapping);
		testMappingProperties(mapping);

	}

	private void testMappingProperties(CsvBeanMapping mapping) {
		CsvColumnBeanFieldMapping nameColumnMapping = mapping
				.getMappingByColumnName("name");
		Assert.assertNotNull(nameColumnMapping);

		nameColumnMapping = mapping.getMappingByColumnName("credit");
		Assert.assertNull(nameColumnMapping);

		nameColumnMapping = mapping.getMappingByColumnName("birth-date");
		Assert.assertNotNull(nameColumnMapping);
		Assert.assertEquals(false, nameColumnMapping.getRequired());
	}

	private void testColumnsAndAnnotatedFieldsAreEquals(CsvBeanMapping mapping) {
		final Field[] fields = User.class.getDeclaredFields();
		Integer csvColumnsNum = 0;
		for (final Field field : fields) {
			if (field.getAnnotation(CsvColumn.class) != null) {
				csvColumnsNum++;
			}
		}
		logger.info(csvColumnsNum + " native");
		Assert.assertEquals(csvColumnsNum, mapping.getColumnsNum());
	}

}

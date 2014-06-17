package hu.boot.easycsv.configuration;

import hu.boot.easycsv.EasyCsvException;
import hu.boot.easycsv.User;
import hu.boot.easycsv.bean.CsvColumn;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReflectionCsvBeanMapperTest {

	private ReflectionCsvBeanMapper mapper;
	private CsvBeanMapping mapping;

	@Before
	public void testMapping() throws EasyCsvException {
		mapper = new ReflectionCsvBeanMapper(User.class);
		mapping = mapper.createMapping();
	}

	@Test
	public void testMappingProperties() {
		CsvColumnBeanFieldMapping nameColumnMapping = mapping
				.getMappingByColumnName("name");
		Assert.assertNotNull(nameColumnMapping);

		nameColumnMapping = mapping.getMappingByColumnName("credit");
		Assert.assertNull(nameColumnMapping);

		nameColumnMapping = mapping.getMappingByColumnName("birth-date");
		Assert.assertNotNull(nameColumnMapping);
		Assert.assertEquals(false, nameColumnMapping.getRequired());

		nameColumnMapping = mapping.getColumnField(0);
		Assert.assertNotNull(nameColumnMapping);
		Assert.assertEquals("name", nameColumnMapping.getName());

		nameColumnMapping = mapping.getColumnField(-100);
		Assert.assertNull(nameColumnMapping);

		nameColumnMapping = mapping.getColumnField(100);
		Assert.assertNull(nameColumnMapping);

	}

	@Test
	public void testColumnsAndAnnotatedFieldsAreEquals() {
		final Field[] fields = User.class.getDeclaredFields();
		Integer csvColumnsNum = 0;
		for (final Field field : fields) {
			if (field.getAnnotation(CsvColumn.class) != null) {
				csvColumnsNum++;
			}
		}
		Assert.assertEquals(csvColumnsNum, mapping.getColumnsNum());
	}

}

package hu.boot.easycsv.configuration;

import hu.boot.easycsv.EasyCsvException;
import hu.boot.easycsv.User;
import hu.boot.easycsv.bean.CsvColumn;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

public class ReflectionCsvBeanMapperTest {

	@Test
	public void testMapping() throws EasyCsvException {
		ReflectionCsvBeanMapper mapper = new ReflectionCsvBeanMapper(User.class);
		CsvBeanMapping mapping = mapper.createMapping();
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
		Field[] fields = User.class.getDeclaredFields();
		Integer csvColumnsNum = 0;
		for (Field field : fields) {
			if (field.getAnnotation(CsvColumn.class) != null) {
				csvColumnsNum++;
			}
		}
		Assert.assertEquals(csvColumnsNum, mapping.getColumnsNum());
	}

}

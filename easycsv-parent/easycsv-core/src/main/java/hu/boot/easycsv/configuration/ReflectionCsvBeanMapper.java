package hu.boot.easycsv.configuration;

import hu.boot.easycsv.EasyCsvException;
import hu.boot.easycsv.bean.CsvColumn;
import hu.boot.easycsv.bean.CsvTransient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionCsvBeanMapper implements CsvBeanMapper {

	private Class<?> beanType;

	public ReflectionCsvBeanMapper(Class<?> beanType) {
		this.beanType = beanType;
	}

	@Override
	public CsvBeanMapping createMapping() throws EasyCsvException {
		final List<CsvColumnBeanFieldMapping> mappings = new ArrayList<CsvColumnBeanFieldMapping>();
		final Field[] beanFields = beanType.getDeclaredFields();
		for (final Field field : beanFields) {
			processField(mappings, field);
		}
		return new CsvBeanMapping(mappings);
	}

	private void processField(List<CsvColumnBeanFieldMapping> mappings,
			Field field) {
		if (field.getAnnotation(CsvTransient.class) != null) {
			return;
		}
		if (field.isSynthetic()) {
			return;
		}
		final CsvColumnBeanFieldMapping mapping = new CsvColumnBeanFieldMapping();
		mapping.setField(field);
		createMapping(field, mapping);
		mappings.add(mapping);
	}

	private void createMapping(Field field, CsvColumnBeanFieldMapping mapping) {
		final CsvColumn columnMeta = field.getAnnotation(CsvColumn.class);
		if (columnMeta != null) {
			processAnnotationConfiguredField(mapping, columnMeta);
		} else {
			processNotConfiguredField(mapping);
		}
	}

	private void processAnnotationConfiguredField(
			CsvColumnBeanFieldMapping mapping, CsvColumn columnMapping) {
		mapping.setName(columnMapping.name());
		mapping.setColumnName(columnMapping.name());
		mapping.setFormat(columnMapping.format());
		mapping.setRequired(columnMapping.required());
	}

	private void processNotConfiguredField(CsvColumnBeanFieldMapping mapping) {
		final Field field = mapping.getField();
		mapping.setColumnName(field.getName());
		mapping.setName(mapping.getColumnName());
	}

	public void setBeanType(Class<?> beanType) {
		this.beanType = beanType;
	}

}

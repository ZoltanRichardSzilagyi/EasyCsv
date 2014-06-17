package hu.boot.easycsv.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvBeanMapping {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CsvBeanMapping.class);

	private final List<CsvColumnBeanFieldMapping> csvColumnFieldMapping;

	private final Integer columnsNum;

	private final Map<String, CsvColumnBeanFieldMapping> columnNameBasedMapping;

	public CsvBeanMapping(List<CsvColumnBeanFieldMapping> csvColumnFieldMapping) {
		this.csvColumnFieldMapping = csvColumnFieldMapping;
		columnsNum = csvColumnFieldMapping.size();

		columnNameBasedMapping = new HashMap<String, CsvColumnBeanFieldMapping>();
		for (final CsvColumnBeanFieldMapping mapping : csvColumnFieldMapping) {
			columnNameBasedMapping.put(mapping.getColumnName(), mapping);
		}
	}

	public CsvColumnBeanFieldMapping getColumnField(int index) {
		try {
			return csvColumnFieldMapping.get(index);
		} catch (final IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}

	}

	public Integer getColumnsNum() {
		return columnsNum;
	}

	public CsvColumnBeanFieldMapping getMappingByColumnName(String columnName) {
		return columnNameBasedMapping.get(columnName);
	}

	public List<CsvColumnBeanFieldMapping> getCsvColumnFieldMapping() {
		return csvColumnFieldMapping;
	}

}

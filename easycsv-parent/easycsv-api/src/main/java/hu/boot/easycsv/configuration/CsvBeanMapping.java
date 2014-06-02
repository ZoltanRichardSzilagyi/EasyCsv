package hu.boot.easycsv.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvBeanMapping {

	private List<CsvColumnBeanFieldMapping> csvColumnFieldMapping;

	private final Integer columnsNum;

	private final Map<String, CsvColumnBeanFieldMapping> columnNameBasedMapping;

	public CsvBeanMapping(List<CsvColumnBeanFieldMapping> csvColumnFieldMapping) {
		this.csvColumnFieldMapping = csvColumnFieldMapping;
		columnsNum = csvColumnFieldMapping.size();

		columnNameBasedMapping = new HashMap<String, CsvColumnBeanFieldMapping>();
		for (CsvColumnBeanFieldMapping mapping : csvColumnFieldMapping) {
			columnNameBasedMapping.put(mapping.getColumnName(), mapping);
		}
	}

	public CsvColumnBeanFieldMapping getColumnField(int index) {
		return csvColumnFieldMapping.get(index);
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

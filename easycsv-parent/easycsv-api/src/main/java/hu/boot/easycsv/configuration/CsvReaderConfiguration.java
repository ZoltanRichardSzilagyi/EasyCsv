package hu.boot.easycsv.configuration;

import hu.boot.easycsv.processor.RowProcessor;

import java.util.ArrayList;
import java.util.List;

public class CsvReaderConfiguration extends AbstractCsvMappingConfiguration {

	private Boolean containsHeader = true;

	private String columnSeparator = ",";

	private List<RowProcessor> rowProcessors = new ArrayList<RowProcessor>();

	public CsvReaderConfiguration() {
	}

	public CsvReaderConfiguration(String[] csvHeaderColumns) {
		super(csvHeaderColumns);
	}

	public String getColumnSeparator() {
		return columnSeparator;
	}

	public void setColumnSeparator(String columnSeparator) {
		this.columnSeparator = columnSeparator;
	}

	public Boolean getContainsHeader() {
		return containsHeader;
	}

	public void setContainsHeader(Boolean containsHeader) {
		this.containsHeader = containsHeader;
	}

	public List<RowProcessor> getRowProcessors() {
		return rowProcessors;
	}

	public void addRowProcessor(RowProcessor rowProcessor) {
		rowProcessors.add(rowProcessor);
	}

}

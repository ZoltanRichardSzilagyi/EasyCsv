package hu.boot.easycsv.configuration;

import hu.boot.easycsv.processor.RowProcessor;

import java.util.ArrayList;
import java.util.List;

public class CsvReaderConfiguration extends AbstractCsvConfiguration {

	private Boolean containsHeader = true;

	private List<RowProcessor> rowProcessors = new ArrayList<RowProcessor>();

	public CsvReaderConfiguration() {
	}

	public CsvReaderConfiguration(String[] csvHeaderColumns) {
		super(csvHeaderColumns);
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

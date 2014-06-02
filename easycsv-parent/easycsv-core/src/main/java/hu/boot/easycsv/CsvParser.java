package hu.boot.easycsv;

import hu.boot.easycsv.cellprocessor.CellProcessor;
import hu.boot.easycsv.cellprocessor.CellProcessorFactory;
import hu.boot.easycsv.configuration.CsvBeanMapping;
import hu.boot.easycsv.configuration.CsvColumnBeanFieldMapping;
import hu.boot.easycsv.configuration.CsvReaderConfiguration;
import hu.boot.easycsv.error.ErrorMessages;
import hu.boot.easycsv.processor.RowProcessor;
import hu.boot.easycsv.stream.StreamReader;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

public class CsvParser<B> {

	private final CsvReaderConfiguration configuration;

	private final CsvReadResult<B> result = new CsvReadResult<B>();

	private final CsvBeanMapping mapping;

	private final String[] csvHeaderColumns;

	private final CellProcessorFactory cellProcessorFactory = new CellProcessorFactory();

	private final StreamReader csvStreamReader;

	private Integer lineNumber = 0;

	public CsvParser(InputStream data, CsvReaderConfiguration configuration) {
		this.configuration = configuration;
		csvStreamReader = new CsvStreamReader(data);
		mapping = configuration.getCsvBeanMapping();
		csvHeaderColumns = configuration.getCsvHeaderColumns();
	}

	public CsvReadResult<B> parse() throws EasyCsvException {
		try {
			if (checkStreamIsEmpty()) {
				return result;
			}
			parseRows();
		} catch (Exception e) {
			throw new EasyCsvException("Unable to parse source data.", e);
		} finally {
			closeInputs();
		}
		return result;
	}

	private Boolean checkStreamIsEmpty() throws IOException {
		if (csvStreamReader.isEmpty()) {
			result.addError(ErrorMessages.EMPTY_CSV);
			closeInputs();
			return true;
		}
		return false;
	}

	private void parseRows() throws Exception {
		String row = null;
		readHeaderIfNecessary();
		while ((row = csvStreamReader.readNextRow()) != null) {
			lineNumber++;
			runRowProcessors(row);
			processRow(row);

		}
	}

	private void readHeaderIfNecessary() throws IOException {
		if (configuration.getContainsHeader()) {
			csvStreamReader.readNextRow();
		}
	}

	private void runRowProcessors(String row) {
		if (configuration.getRowProcessors().size() == 0) {
			return;
		}
		for (RowProcessor rowProcessor : configuration.getRowProcessors()) {
			row = rowProcessor.process(row);
		}
	}

	private void processRow(String row) throws Exception {
		String[] cells = splitRow(row);
		int index = 0;
		if (!isRowValid(cells)) {
			return;
		}
		B bean = createBeanInstance();
		for (String cellValue : cells) {
			String columnName = csvHeaderColumns[index];
			CsvColumnBeanFieldMapping fieldMapping = mapping
					.getMappingByColumnName(columnName);
			String fieldName = fieldMapping.getField().getName();
			Object processedValue = convertRawValue(fieldMapping, cellValue);
			PropertyUtils.setProperty(bean, fieldName, processedValue);
			index++;
		}
		result.addBean(bean);
	}

	private Boolean isRowValid(String[] cells) {
		if (cells.length != mapping.getColumnsNum()) {
			result.addError(ErrorMessages.INVALID_CELLS_NUM, lineNumber,
					cells.length, mapping.getColumnsNum());
			return false;
		}
		return true;
	}

	private Object convertRawValue(CsvColumnBeanFieldMapping fieldMapping,
			String value) throws Exception {
		Class<?> fieldType = fieldMapping.getField().getType();
		CellProcessor<?> cellProcessor = cellProcessorFactory
				.getCellProcessor(fieldType);
		return cellProcessor.process(fieldMapping, value);
	}

	@SuppressWarnings("unchecked")
	private B createBeanInstance() throws Exception {
		B bean = (B) configuration.getBeanType().newInstance();
		return bean;
	}

	private String[] splitRow(String row) {
		String[] lineValues = StringUtils.split(row,
				configuration.getColumnSeparator());
		return lineValues;
	}

	private void closeInputs() {
		csvStreamReader.close();
	}

}

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

final class CsvParser<B> {

	private final CsvReaderConfiguration configuration;

	private final CsvReadResult<B> result = new CsvReadResult<B>();

	private final CsvBeanMapping mapping;

	private final String[] csvHeaderColumns;

	private final CellProcessorFactory cellProcessorFactory;

	private final StreamReader csvStreamReader;

	private Integer lineNumber = 0;

	public CsvParser(CsvParserBuilder<B> builder) {
		configuration = builder.configuration;
		cellProcessorFactory = builder.cellProcessorFactory;
		csvStreamReader = new CsvStreamReader(builder.data,
				configuration.getCharset());
		csvHeaderColumns = configuration.getCsvHeaderColumns();
		mapping = configuration.getCsvBeanMapping();

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
		if (isRowValid(cells)) {
			processRow(cells);
		}
	}

	private void processRow(String[] cells) throws Exception {
		Integer index = 0;
		B bean = createBeanInstance();
		for (String cellValue : cells) {
			cellValue = parseCellValue(cellValue);
			bean = processRow(bean, index, cellValue);
			index++;
		}
		result.addBean(bean);
	}

	private String parseCellValue(String cellValue) {
		String trimmedCell = StringUtils.trimToEmpty(cellValue);
		if (isCellValueQuoted(trimmedCell)) {
			trimmedCell = StringUtils.substringAfter(trimmedCell,
					configuration.getQuoteChar());
			trimmedCell = StringUtils.substringBeforeLast(trimmedCell,
					configuration.getQuoteChar());
			return trimmedCell;
		}
		return cellValue;

	}

	private boolean isCellValueQuoted(String cellValue) {
		if (StringUtils.startsWith(cellValue, configuration.getQuoteChar())
				&& StringUtils
						.endsWith(cellValue, configuration.getQuoteChar())) {
			return true;
		}
		return false;
	}

	private B processRow(B bean, Integer index, String cellValue)
			throws Exception {
		String columnName = csvHeaderColumns[index];
		CsvColumnBeanFieldMapping fieldMapping = mapping
				.getMappingByColumnName(columnName);
		String fieldName = fieldMapping.getField().getName();
		Object processedValue = convertRawValue(fieldMapping, cellValue);
		PropertyUtils.setProperty(bean, fieldName, processedValue);
		return bean;
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
				configuration.getDelimiterChar());
		return lineValues;
	}

	private void closeInputs() {
		csvStreamReader.close();
	}

	public static class CsvParserBuilder<B> {
		private InputStream data;

		private CellProcessorFactory cellProcessorFactory;

		private CsvReaderConfiguration configuration;

		public CsvParserBuilder<B> setData(InputStream data) {
			this.data = data;
			return this;
		}

		public CsvParserBuilder<B> setCellProcessorFactory(
				CellProcessorFactory cellProcessorFactory) {
			this.cellProcessorFactory = cellProcessorFactory;
			return this;
		}

		public CsvParserBuilder<B> setConfiguration(
				CsvReaderConfiguration configuration) {
			this.configuration = configuration;
			return this;
		}

		public CsvParser<B> build() {
			return new CsvParser<B>(this);
		}
	}

}

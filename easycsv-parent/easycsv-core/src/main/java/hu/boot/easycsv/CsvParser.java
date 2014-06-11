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
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class CsvParser<B> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CsvParser.class);

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
		} catch (final Exception e) {
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

	private void parseRows() {

		readHeaderIfNecessary();

		while (true) {
			String row = null;
			try {
				row = csvStreamReader.readNextRow();
			} catch (final IOException e) {
				LOGGER.error(e.getMessage(), e);
				result.addError("Unable to read next line!");
			}
			if (row == null) {
				break;
			}
			lineNumber++;
			final String preprocessedRow = runRowProcessors(row);
			processRow(preprocessedRow);

		}
	}

	private void readHeaderIfNecessary() {
		if (configuration.getContainsHeader()) {
			try {
				csvStreamReader.readNextRow();
			} catch (final IOException e) {
				LOGGER.error(e.getMessage(), e);
				result.addError("Unable to read header!");
			}
		}
	}

	private String runRowProcessors(String row) {
		if (CollectionUtils.isEmpty(configuration.getRowProcessors())) {
			return row;
		}
		String processedRow = row;
		for (final RowProcessor rowProcessor : configuration.getRowProcessors()) {
			processedRow = rowProcessor.process(processedRow);
		}
		return processedRow;
	}

	private void processRow(String row) {
		final String[] cells = splitRow(row);
		if (isRowValid(cells)) {
			processRow(cells);
		}
	}

	private void processRow(String[] cells) {
		Integer index = 0;
		B bean = null;
		try {
			bean = createBeanInstance();
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.addError("Error during bean instantiation");
			return;
		}

		for (String cellValue : cells) {
			cellValue = parseCellValue(cellValue);
			try {
				bean = processRow(bean, index, cellValue);
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
				result.addError("Unable to process row: {}, error: ",
						lineNumber, e.getMessage());
			}
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
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		final String columnName = csvHeaderColumns[index];
		final CsvColumnBeanFieldMapping fieldMapping = mapping
				.getMappingByColumnName(columnName);
		final String fieldName = fieldMapping.getField().getName();
		final Object processedValue = convertRawValue(fieldMapping, cellValue);
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
			String value) {
		final Class<?> fieldType = fieldMapping.getField().getType();
		final CellProcessor<?> cellProcessor = cellProcessorFactory
				.getCellProcessor(fieldType);
		try {
			return cellProcessor.process(fieldMapping, value);
		} catch (final Exception e) {
			result.addError("Unable to convert row to object, line: {}",
					lineNumber);
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private B createBeanInstance() throws InstantiationException,
			IllegalAccessException {
		return (B) configuration.getBeanType().newInstance();
	}

	private String[] splitRow(String row) {
		return StringUtils.split(row, configuration.getDelimiterChar());
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

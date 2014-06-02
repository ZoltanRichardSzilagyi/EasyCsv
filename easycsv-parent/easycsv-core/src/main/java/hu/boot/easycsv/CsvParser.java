package hu.boot.easycsv;

import hu.boot.easycsv.cellprocessor.CellProcessor;
import hu.boot.easycsv.cellprocessor.CellProcessorFactory;
import hu.boot.easycsv.configuration.CsvBeanMapping;
import hu.boot.easycsv.configuration.CsvColumnBeanFieldMapping;
import hu.boot.easycsv.configuration.CsvReaderConfiguration;
import hu.boot.easycsv.error.ErrorMessages;
import hu.boot.easycsv.processor.LineProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class CsvParser<B> {
	// TODO customreader-> readLine, isEmpty, close...
	private final InputStream data;
	private final CsvReaderConfiguration configuration;
	private final InputStreamReader reader;
	private final BufferedReader bufferedReader;

	private final CsvReadResult<B> result = new CsvReadResult<B>();

	private final CsvBeanMapping mapping;

	private final String[] csvHeaderColumns;

	private final CellProcessorFactory cellProcessorFactory = new CellProcessorFactory();

	private Integer lineNumber = 0;

	public CsvParser(InputStream data, CsvReaderConfiguration configuration) {
		this.data = data;
		this.configuration = configuration;
		reader = new InputStreamReader(data);
		bufferedReader = new BufferedReader(reader);
		mapping = configuration.getCsvBeanMapping();
		csvHeaderColumns = configuration.getCsvHeaderColumns();
	}

	public CsvReadResult<B> parse() throws EasyCsvException {
		try {
			if (checkStreamIsEmpty()) {
				return result;
			}
			parseLines();
		} catch (Exception e) {
			throw new EasyCsvException("Unable to parse source data.", e);
		} finally {
			closeInputs();
		}
		return result;
	}

	private Boolean checkStreamIsEmpty() throws IOException {
		if (data.available() == 0) {
			result.addError(ErrorMessages.EMPTY_CSV);
			closeInputs();
			return true;
		}
		return false;
	}

	private void parseLines() throws Exception {
		String line = null;
		readHeaderIfNecessary();
		while ((line = bufferedReader.readLine()) != null) {
			lineNumber++;
			runLineProcessors(line);
			processRow(line);

		}
	}

	private void readHeaderIfNecessary() throws IOException {
		if (configuration.getContainsHeader()) {
			bufferedReader.readLine();
		}
	}

	private void runLineProcessors(String line) {
		if (configuration.getLineProcessors().size() == 0) {
			return;
		}
		for (LineProcessor lineProcessor : configuration.getLineProcessors()) {
			line = lineProcessor.processLine(line);
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
		IOUtils.closeQuietly(bufferedReader);
		IOUtils.closeQuietly(reader);
		IOUtils.closeQuietly(data);
	}

}

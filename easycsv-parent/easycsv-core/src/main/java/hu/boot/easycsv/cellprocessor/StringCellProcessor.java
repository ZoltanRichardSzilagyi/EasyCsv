package hu.boot.easycsv.cellprocessor;

import hu.boot.easycsv.configuration.CsvColumnBeanFieldMapping;

public class StringCellProcessor implements CellProcessor<String> {

	@Override
	public String process(CsvColumnBeanFieldMapping mapping, String value)
			throws Exception {
		return value;
	}

}

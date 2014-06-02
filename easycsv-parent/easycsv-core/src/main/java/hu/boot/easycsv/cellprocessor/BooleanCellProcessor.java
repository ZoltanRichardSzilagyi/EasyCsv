package hu.boot.easycsv.cellprocessor;

import hu.boot.easycsv.configuration.CsvColumnBeanFieldMapping;

import org.apache.commons.lang3.BooleanUtils;

public class BooleanCellProcessor implements CellProcessor<Boolean> {

	@Override
	public Boolean process(CsvColumnBeanFieldMapping mapping, String value)
			throws Exception {
		return BooleanUtils.toBoolean(value);
	}

}

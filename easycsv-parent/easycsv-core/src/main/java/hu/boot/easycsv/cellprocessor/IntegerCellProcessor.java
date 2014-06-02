package hu.boot.easycsv.cellprocessor;

import hu.boot.easycsv.configuration.CsvColumnBeanFieldMapping;

import org.apache.commons.lang3.math.NumberUtils;

public class IntegerCellProcessor implements CellProcessor<Integer> {

	@Override
	public Integer process(CsvColumnBeanFieldMapping mapping, String value)
			throws Exception {
		return NumberUtils.toInt(value);
	}

}

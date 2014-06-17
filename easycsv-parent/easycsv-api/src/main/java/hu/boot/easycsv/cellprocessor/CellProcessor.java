package hu.boot.easycsv.cellprocessor;

import hu.boot.easycsv.configuration.CsvColumnBeanFieldMapping;

public interface CellProcessor<T> {

	public T process(CsvColumnBeanFieldMapping mapping, String value);

}

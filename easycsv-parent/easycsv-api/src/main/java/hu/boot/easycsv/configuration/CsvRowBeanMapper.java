package hu.boot.easycsv.configuration;

import hu.boot.easycsv.CsvRow;

public interface CsvRowBeanMapper<B> {

	public B mapRow(CsvRow row);
}

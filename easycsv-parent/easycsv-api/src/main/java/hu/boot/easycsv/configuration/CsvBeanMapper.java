package hu.boot.easycsv.configuration;

import hu.boot.easycsv.EasyCsvException;

public interface CsvBeanMapper {

	public CsvBeanMapping createMapping() throws EasyCsvException;

}

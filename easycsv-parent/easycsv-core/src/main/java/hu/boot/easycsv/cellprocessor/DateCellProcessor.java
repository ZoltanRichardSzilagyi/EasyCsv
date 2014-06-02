package hu.boot.easycsv.cellprocessor;

import hu.boot.easycsv.configuration.CsvColumnBeanFieldMapping;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DateCellProcessor implements CellProcessor<Date> {

	@Override
	public Date process(CsvColumnBeanFieldMapping mapping, String value)
			throws Exception {
		DateTimeFormatter dateTimeFormatter = getDateFormatter(mapping);
		return dateTimeFormatter.parseDateTime(value).toDate();
	}

	private DateTimeFormatter getDateFormatter(CsvColumnBeanFieldMapping mapping) {
		return StringUtils.isNotBlank(mapping.getFormat()) ? DateTimeFormat
				.forPattern(mapping.getFormat()) : ISODateTimeFormat.dateTime();

	}

}

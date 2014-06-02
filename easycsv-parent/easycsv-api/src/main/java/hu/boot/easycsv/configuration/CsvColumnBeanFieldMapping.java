package hu.boot.easycsv.configuration;

import java.lang.reflect.Field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

// TODO validation rules
@XmlRootElement(name = "mapping")
@XmlAccessorType(XmlAccessType.FIELD)
public class CsvColumnBeanFieldMapping {

	@XmlAttribute(required = true)
	private String name;

	@XmlAttribute(required = true)
	private String columnName;

	@XmlTransient
	private Field field;

	@XmlAttribute
	private Boolean required = true;

	@XmlAttribute
	private String format = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

}

package hu.boot.easycsv.bean;

import java.lang.reflect.Field;

public class PropertyMetaData {

	private Field field;

	private String format;

	private Boolean required;

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

}

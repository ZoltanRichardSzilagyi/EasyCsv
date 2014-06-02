package hu.boot.easycsv;

import hu.boot.easycsv.error.Error;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CsvReadResult<B> {

	private List<B> beans = new ArrayList<B>();

	private final List<Error> errors = new ArrayList<Error>();

	public CsvReadResult() {
	}

	public CsvReadResult(List<B> beans) {
		this.beans = beans;
	}

	public List<B> getBeans() {
		return beans;
	}

	public void setBeans(List<B> beans) {
		this.beans = beans;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void addError(Error error) {
		errors.add(error);
	}

	public void addError(String message) {
		Error error = new Error(message);
		errors.add(error);
	}

	public void addError(String message, Object... args) {
		String replacedMessage = message;
		for (Object arg : args) {
			replacedMessage = StringUtils.replaceOnce(replacedMessage, "{}",
					arg.toString());
		}
		Error error = new Error(replacedMessage);
		errors.add(error);
	}

	public void addErrors(List<Error> errors) {
		errors.addAll(errors);
	}

	public void addBean(B bean) {
		beans.add(bean);
	}

	public void addBeans(List<B> beans) {
		beans.addAll(beans);
	}

}

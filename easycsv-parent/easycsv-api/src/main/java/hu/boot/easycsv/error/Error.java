package hu.boot.easycsv.error;

public class Error {

	private final Integer lineNumber;

	private final String message;

	public Error(String message) {
		this.message = message;
		lineNumber = -1;
	}

	public Error(Integer lineNumber, String message) {
		this.lineNumber = lineNumber;
		this.message = message;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public String getMessage() {
		return message;
	}

}

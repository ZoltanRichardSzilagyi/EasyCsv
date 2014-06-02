package hu.boot.easycsv.error;

public class Error {

	private Integer lineNumber;

	private final String message;

	public Error(String message) {
		this.message = message;
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

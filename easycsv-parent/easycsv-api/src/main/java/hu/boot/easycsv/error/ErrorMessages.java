package hu.boot.easycsv.error;

public final class ErrorMessages {

	public static final String EMPTY_CSV = "Empty input data";

	public static final String FILE_NULL = "File parameter must not be null";

	public static final String FILE_MISSING = "File not found: {}";

	public static final String INVALID_CELLS_NUM = "line {}. contains {} columns, {} expected";

	private ErrorMessages() {
	}

}

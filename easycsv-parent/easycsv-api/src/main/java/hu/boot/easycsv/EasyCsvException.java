package hu.boot.easycsv;

public class EasyCsvException extends Exception {

	private static final long serialVersionUID = 8765607026313493248L;
	
	public EasyCsvException(String message) {
		super(message);
	}
	
	public EasyCsvException(String message, Throwable throwable){
		super(message, throwable);
	}
	
	

}

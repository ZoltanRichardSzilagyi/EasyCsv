package hu.boot.easycsv.cellprocessor;

@SuppressWarnings("rawtypes")
public class CellProcessorHolder {

	private final Class<? extends CellProcessor> cellprocessorType;

	private CellProcessor<?> cellProcessorInstance;

	public CellProcessorHolder(Class<? extends CellProcessor> cellprocessorType2) {
		this.cellprocessorType = cellprocessorType2;
	}

	public Class getCellprocessorType() {
		return cellprocessorType;
	}

	public CellProcessor getCellProcessorInstance() {
		return cellProcessorInstance;
	}

	public void setCellProcessorInstance(CellProcessor<?> cellProcessorInstance) {
		this.cellProcessorInstance = cellProcessorInstance;
	}

}

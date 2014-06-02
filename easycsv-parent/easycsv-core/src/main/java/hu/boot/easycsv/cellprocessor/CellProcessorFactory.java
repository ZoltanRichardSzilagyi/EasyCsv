package hu.boot.easycsv.cellprocessor;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

@SuppressWarnings("rawtypes")
public class CellProcessorFactory {

	private Map<Class<?>, CellProcessorHolder> cellprocessors = new HashMap<Class<?>, CellProcessorHolder>();

	public CellProcessorFactory() {
		initCellProcessors();
	}

	private void initCellProcessors() {
		Reflections reflections = new Reflections(
				"hu.boot.easycsv.cellprocessor");
		Set<Class<? extends CellProcessor>> cellprocessorClassess = reflections
				.getSubTypesOf(CellProcessor.class);
		for (Class<? extends CellProcessor> cellprocessorType : cellprocessorClassess) {
			ParameterizedType type = (ParameterizedType) cellprocessorType
					.getGenericInterfaces()[0];
			Class<?> cellProcessorGenericType = (Class<?>) type
					.getActualTypeArguments()[0];
			CellProcessorHolder cellProcessorHolder = new CellProcessorHolder(
					cellprocessorType);
			cellprocessors.put(cellProcessorGenericType, cellProcessorHolder);
		}
	}

	public CellProcessor<?> getCellProcessor(Class<?> type) {
		CellProcessorHolder cellProcessorHolder = cellprocessors.get(type);
		if (cellProcessorHolder == null) {
			return null;
		}
		CellProcessor<?> cellProcessor = null;
		cellProcessor = cellProcessorHolder.getCellProcessorInstance();
		if (cellProcessor == null) {
			try {
				cellProcessor = cellProcessorHolder.getCellprocessorType()
						.newInstance();
				cellProcessorHolder.setCellProcessorInstance(cellProcessor);
			} catch (Exception e) {
				return null;
			}
		}
		return cellProcessor;

	}

}

package hu.boot.easycsv.cellprocessor;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class CellProcessorFactory {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CellProcessorFactory.class);

	private final Map<Class<?>, CellProcessorHolder> cellprocessors = new HashMap<Class<?>, CellProcessorHolder>();

	public CellProcessorFactory() {
		initCellProcessors();
	}

	private void initCellProcessors() {
		final Reflections reflections = new Reflections(
				"hu.boot.easycsv.cellprocessor");
		final Set<Class<? extends CellProcessor>> cellprocessorClassess = reflections
				.getSubTypesOf(CellProcessor.class);
		for (final Class<? extends CellProcessor> cellprocessorType : cellprocessorClassess) {
			final ParameterizedType type = (ParameterizedType) cellprocessorType
					.getGenericInterfaces()[0];
			final Class<?> cellProcessorGenericType = (Class<?>) type
					.getActualTypeArguments()[0];
			final CellProcessorHolder cellProcessorHolder = new CellProcessorHolder(
					cellprocessorType);
			cellprocessors.put(cellProcessorGenericType, cellProcessorHolder);
		}
	}

	public CellProcessor getCellProcessor(Class<?> type) {
		final CellProcessorHolder cellProcessorHolder = cellprocessors
				.get(type);
		if (cellProcessorHolder == null) {
			return null;
		}
		CellProcessor cellProcessor = null;
		cellProcessor = cellProcessorHolder.getCellProcessorInstance();
		if (cellProcessor == null) {
			try {
				cellProcessor = (CellProcessor) cellProcessorHolder
						.getCellprocessorType().newInstance();
				cellProcessorHolder.setCellProcessorInstance(cellProcessor);
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
				return null;
			}
		}
		return cellProcessor;

	}

}

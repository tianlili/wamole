package com.baidu.wamole.xml;

import static java.util.logging.Level.WARNING;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * {@link Converter} implementation for XStream.
 */
public final class XmlConverter extends AbstractCollectionConverter {
	public XmlConverter(Mapper mapper) {
		super(mapper);
	}

	public boolean canConvert(Class type) {
		return type == CopyOnWriteList.class;
	}

	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		for (Object o : (CopyOnWriteList) source)
			writeItem(o, context, writer);
	}

	@SuppressWarnings("unchecked")
	public CopyOnWriteList unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		// read the items from xml into a list
		List items = new ArrayList();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			try {
				Object item = readItem(reader, context, items);
				items.add(item);
			} catch (CannotResolveClassException e) {
				LOGGER.log(WARNING, "Failed to resolve class", e);
				// RobustReflectionConverter.addErrorInContext(context, e);
			} catch (LinkageError e) {
				LOGGER.log(WARNING, "Failed to resolve class", e);
				// RobustReflectionConverter.addErrorInContext(context, e);
			}
			reader.moveUp();
		}

		return new CopyOnWriteList(items, true);
	}
	private static final Logger LOGGER = Logger.getLogger(CopyOnWriteList.class
			.getName());
}

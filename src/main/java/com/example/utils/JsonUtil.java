package com.example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
	private static final ObjectMapper mapper = newMapper();

	private static ObjectMapper newMapper() {
		ObjectMapper mapper = new ObjectMapper();
		/*
		SimpleModule dateModule = new SimpleModule("DateTimeFormatModule", new Version(1, 0, 0, null, "", ""))
			.addDeserializer(
				Date.class,
				new DateDeserializer(new DateDeserializer(),
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
						"yyyy-MM-dd HH:mm:ss"))
			.addSerializer(
				Date.class,
				new DateSerializer(false, new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss")));
		mapper.registerModule(dateModule);
		*/
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		return mapper;
	}

	public static <T> List<T> parseList(String jsonString, Class<T> type) throws Exception {
		JavaType javaType = mapper.getTypeFactory().constructParametrizedType(
				ArrayList.class, List.class, type);
		return mapper.readValue(jsonString, javaType);
	}
}

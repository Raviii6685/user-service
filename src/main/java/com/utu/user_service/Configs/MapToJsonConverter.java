package com.utu.user_service.Configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utu.user_service.Models.FileMetaDataDoc;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.Map;

@Converter
public class MapToJsonConverter implements AttributeConverter<Map<String, List<FileMetaDataDoc>>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, List<FileMetaDataDoc>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    @Override
    public Map<String, List<FileMetaDataDoc>> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData,
                    new TypeReference<Map<String, List<FileMetaDataDoc>>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to map", e);
        }
    }
}
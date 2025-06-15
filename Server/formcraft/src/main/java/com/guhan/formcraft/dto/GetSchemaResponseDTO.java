package com.guhan.formcraft.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class GetSchemaResponseDTO {
	
	private String schemaId;
	private JsonNode jsonSchema;
	
	public GetSchemaResponseDTO() {
	}
	public GetSchemaResponseDTO(String schemaId, JsonNode jsonSchema) {
		super();
		this.schemaId = schemaId;
		this.jsonSchema = jsonSchema;
	}
	public String getSchemaId() {
		return schemaId;
	}
	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}
	public JsonNode getJsonSchema() {
		return jsonSchema;
	}
	public void setJsonSchema(JsonNode jsonSchema) {
		this.jsonSchema = jsonSchema;
	}
	
	
	
	

}

package com.guhan.formcraft.dto;
import com.fasterxml.jackson.databind.JsonNode;

public class AddDataRequestDTO {
	
	private String schemaId;
	private JsonNode formData;
	
	public AddDataRequestDTO() {
		
	}

	public AddDataRequestDTO(String schemaId, JsonNode formData) {
		super();
		this.schemaId = schemaId;
		this.formData = formData;
	}

	public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	public JsonNode getFormData() {
		return formData;
	}

	public void setFormData(JsonNode formData) {
		this.formData = formData;
	}
	
	

	
}

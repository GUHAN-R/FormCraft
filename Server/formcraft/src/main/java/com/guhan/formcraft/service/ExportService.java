package com.guhan.formcraft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.guhan.formcraft.models.Form;
import com.guhan.formcraft.models.Schema;

@Service
public class ExportService {
	
	@Autowired
	private DataPersistanceService dataPersistanceService;
	
	@Autowired
	private ObjectMapper objectMapper;

	public JsonNode Export(String schemaId) throws Exception{
		Schema schema =  dataPersistanceService.getSchema(schemaId);
		List<Form> forms = dataPersistanceService.getFormsBySchemaId(schemaId);
		
		ObjectNode data = objectMapper.createObjectNode();
		
		data.set("schema", schema.getJsonSchema());
		ArrayNode formsArray = objectMapper.createArrayNode();
		for (Form form : forms) {
			if (form.getFormData() != null) {
				JsonNode formDataNode = form.getFormData();
				formsArray.add(formDataNode);
			}
		}
		data.set("forms", formsArray);
	    return data;
	}
}

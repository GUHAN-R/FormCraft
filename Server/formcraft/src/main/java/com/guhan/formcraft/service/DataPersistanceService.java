package com.guhan.formcraft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.guhan.formcraft.dto.SchemaSummaryResponseDTO;
import com.guhan.formcraft.exceptions.DataNotFoundException;
import com.guhan.formcraft.exceptions.DataPersistanceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.guhan.formcraft.models.Form;
import com.guhan.formcraft.models.Schema;
import com.guhan.formcraft.repo.FormRepo;
import com.guhan.formcraft.repo.SchemaRepo;
import com.guhan.formcraft.repo.UserRepo;
import com.guhan.formcraft.utilities.UUIDGenerator;

@Service
public class DataPersistanceService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private SchemaRepo schemaRepo;
	
	@Autowired
	private FormRepo formRepo;
	
	private Schema prepareSchemaObject(JsonNode JSONSchema) throws Exception {
		Schema schemaObj = new Schema();
		schemaObj.setSchemaId(UUIDGenerator.getUUID());
		schemaObj.setUserId("1");
		schemaObj.setSchemaDescription(JSONSchema.get("narration").asText());
		schemaObj.setSchemaTitle(JSONSchema.get("title").asText());
		schemaObj.setJsonSchema(JSONSchema);
		return schemaObj;
	}
	
	private Form prepareFormObject(JsonNode formData, Schema schema) throws Exception {
		Form form = new Form();
		form.setFormId(UUIDGenerator.getUUID());
		form.setSchema(schema);
		form.setFormData(formData);
		return form;
	}
//	public void persistData(JsonNode JSONSchema, JsonNode formData) {
//		try {
//			Schema schema = prepareSchemaObject(JSONSchema);
//			Form form = prepareFormObject(formData,schema);
//			schemaRepo.save(schema);
//			formRepo.save(form);
//		} catch (Exception e) {
//			throw new DataPersistanceException("Failed to persist data",e);
//		}
//		
//		
//	}
	
	public void saveFormData(JsonNode formData, Schema schema) {
		try {
			Form form = prepareFormObject(formData, schema);
			formRepo.save(form);
		}catch (Exception e) {
			throw new DataPersistanceException("Failed to save Form data",e);
		}
	}
	
	public Schema saveSchema(JsonNode jsonSchema) {
		try {
			Schema schema = prepareSchemaObject(jsonSchema);
			schemaRepo.save(schema);
			return schema;
		}catch (Exception e) {
			throw new DataPersistanceException("Failed to save schema data",e);
		}
	}
	
	public List<SchemaSummaryResponseDTO> getSchemasByUserId(String userId){
		try {
			List<Schema> schemas = schemaRepo.findByUserId(userId);
			return schemas.stream().map(schema -> new SchemaSummaryResponseDTO(
					schema.getSchemaId(),schema.getSchemaDescription(),schema.getSchemaTitle())).collect(Collectors.toList());
		}catch (Exception e) {
			throw new DataPersistanceException("Failed to retrieve schemas",e);
		}
				
	}
	
	public Schema getSchema(String schemaId) {
		try {
			
			return schemaRepo.findBySchemaId(schemaId).orElseThrow(() -> new DataNotFoundException("schema Record not found for the schema id"+schemaId));
		}catch (Exception e) {
			throw new DataPersistanceException("Failed to fetch Json schema",e);
		}
		
	}
	public List<Form> getFormsBySchemaId(String schemaId) {
		try {
			List<Form> forms = formRepo.findBySchema_SchemaId(schemaId);
			return forms;
		}catch (Exception e) {
			throw new DataPersistanceException("Failed to fetch form data",e);
		}
		
	}
	

}

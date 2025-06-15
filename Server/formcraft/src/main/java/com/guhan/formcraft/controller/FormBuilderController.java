package com.guhan.formcraft.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.guhan.formcraft.dto.SchemaSummaryResponseDTO;
import com.guhan.formcraft.dto.AddDataRequestDTO;
import com.guhan.formcraft.dto.GetSchemaResponseDTO;
import com.guhan.formcraft.dto.ValidationResponseDTO;
import com.guhan.formcraft.models.Schema;
import com.guhan.formcraft.service.DataPersistanceService;
import com.guhan.formcraft.service.ExportService;

import java.util.*;
//import com.guhan.formcraft.service.FormPersistanceService;
import com.guhan.formcraft.service.ValidationService;

@RestController
@RequestMapping("/api/formbuilder")
public class FormBuilderController {
	
	private final ValidationService validationService;
	private final DataPersistanceService dataPersistanceService;
	private final ExportService exportService;
	private final ObjectMapper objectMapper;
	
	public FormBuilderController(ValidationService validationService,DataPersistanceService dataPersistanceService,ExportService exportService,ObjectMapper objectMapper) {
		this.validationService = validationService;
		this.dataPersistanceService = dataPersistanceService;
		this.exportService = exportService;
		this.objectMapper = objectMapper;
	}
	
	@PostMapping("/saveSchema")
	public ResponseEntity<String> saveSchema(@RequestBody JsonNode request){
		
		try {
			Schema schema = dataPersistanceService.saveSchema(request.get("schema"));
			return ResponseEntity.ok(schema.getSchemaId());
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		
	}
	
	@PostMapping("/addFormData")
	public ResponseEntity<ValidationResponseDTO> addFormData(@RequestBody AddDataRequestDTO addDataRequestDTO){
		try {
			Schema schema = dataPersistanceService.getSchema(addDataRequestDTO.getSchemaId());
			ValidationResponseDTO response = validationService.validate(schema.getJsonSchema(), addDataRequestDTO.getFormData());
			if(response.isSuccess()) {
				dataPersistanceService.saveFormData(addDataRequestDTO.getFormData(),schema);
				return ResponseEntity.ok(response);
				
			}else {
				return ResponseEntity.badRequest().body(response);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ValidationResponseDTO(false,"Internal server error", null));
			
		}
	}
//	@PostMapping("/saveSchema")
//	public ResponseEntity<ValidationResponseDTO> saveSchema(@RequestBody JsonNode request){
//		try {
//			dataPersistanceService.saveSchema(request);
//		}
//		ValidationResponseDTO response = validationService.validate(request.getSchema(), request.getFormData());
//		if(response.isSuccess()) {
//			try {
//				dataPersistanceService.persistData(request.getSchema(),request.getFormData());
//				return ResponseEntity.ok(response);
//			}catch (Exception exception) {
//				exception.printStackTrace();
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ValidationResponseDTO(false,"Internal server error", null));
//			}
//			
//		}else {
//			return ResponseEntity.badRequest().body(response);
//		}
//		
//	}
	
	@GetMapping("/getSchemas")
	public  ResponseEntity<List<SchemaSummaryResponseDTO>> getSchemas(@RequestParam("userId") String userId) {
		try {
			List<SchemaSummaryResponseDTO> schemaHistory= dataPersistanceService.getSchemasByUserId(userId);
			return ResponseEntity.ok(schemaHistory);
		}catch (Exception exception) {
			exception.printStackTrace();
			SchemaSummaryResponseDTO errorDTO = new SchemaSummaryResponseDTO("error", "An error occurred while fetching schema history.",null);
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                         .body(Collections.singletonList(errorDTO));
		}
	}
	
	@GetMapping("/getSchema")
	public ResponseEntity<GetSchemaResponseDTO> getSchema(@RequestParam("schemaId") String schemaId) {
		try {
			Schema schema = dataPersistanceService.getSchema(schemaId);
			return ResponseEntity.ok(new GetSchemaResponseDTO(schemaId,schema.getJsonSchema()));
		}catch (Exception e) {
			e.printStackTrace();
			ObjectNode errorNode = objectMapper.createObjectNode();
	        errorNode.put("status", "error");
	        errorNode.put("message", "An error occurred while Exporting");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GetSchemaResponseDTO("error",errorNode));
		}
	}
	
	@GetMapping("/export")
	public  ResponseEntity<JsonNode> export(@RequestParam("schemaId") String schemaId) {
		try {	
			JsonNode file =  exportService.Export(schemaId);
			return ResponseEntity.ok(file);
		}catch (Exception exception) {
			exception.printStackTrace();
	        ObjectNode errorNode = objectMapper.createObjectNode();
	        errorNode.put("status", "error");
	        errorNode.put("message", "An error occurred while Exporting");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorNode);
		}
	}

}

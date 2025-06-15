package com.guhan.formcraft.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.guhan.formcraft.dto.ValidationResponseDTO;
import com.guhan.formcraft.models.ValidationError;

@Service
public class ValidationService {
	
	public ValidationResponseDTO validate(JsonNode schema, JsonNode formData ) {
		List<ValidationError> errors = new ArrayList<>();
		
		JsonNode properties = schema.get("properties");
		if(properties==null || !properties.isObject()) {
			System.out.print("Error at request");
			return new ValidationResponseDTO(false, "Schema has no Properties", null);
		}
		
		Iterator<String> fields = properties.fieldNames();
		while(fields.hasNext()) {
			String fieldKey = fields.next();
			JsonNode fieldSchema = properties.get(fieldKey);
			JsonNode formValue = formData.get(fieldKey);
			boolean isRequired = fieldSchema.has("required") && fieldSchema.get("required").asBoolean();
			
			if(isRequired && (formValue==null || formValue.asText().isEmpty())) {
				errors.add(new ValidationError(fieldKey, "This is a Required Field"));
				continue;
			}
			
			if(formValue==null)continue;
			
			String fieldType = fieldSchema.has("type")?fieldSchema.get("type").asText():"string"; 
			
			if(fieldType.equals("string")) {
				if(!formValue.isTextual()) {
					errors.add(new ValidationError(fieldKey, "The expected Value is String"));
					continue;
				}
				
				String value = formValue.asText();
				if(fieldSchema.has("minLength")) {
					if(value.length()<fieldSchema.get("minLength").asInt()) {
						errors.add(new ValidationError(fieldKey, "Value Length should be minimum of "+fieldSchema.get("minLength").asInt()));
						continue;
					}
				}
				if(fieldSchema.has("maxLength")) {
					if(value.length()>fieldSchema.get("maxLength").asInt()) {
						errors.add(new ValidationError(fieldKey, "Value Length should not exceed maximum length "+fieldSchema.get("maxLength").asInt()));
						continue;
					}
				}
				if (fieldSchema.has("pattern")) {
                    String pattern = fieldSchema.get("pattern").asText();
                    if (!value.matches(pattern)) {
                        errors.add(new ValidationError(fieldKey, "Value does not match the expected pattern. "+pattern));
                    }
                }
			}
			if(fieldType.equals("integer") || fieldType.equals("number")) {
				
				if(!formValue.isNumber()) {
					errors.add(new ValidationError(fieldKey, "Value should be a Number"));
					continue;
				}
				
				double num = formValue.asDouble();
				if(fieldSchema.has("minValue")) {
					double minVal = fieldSchema.get("minValue").asDouble();
					if(num<minVal) {
						errors.add(new ValidationError(fieldKey, "Value  must be higher than the minimum value : "+minVal));
					}
				}
				if(fieldSchema.has("maxValue")) {
					double maxVal = fieldSchema.get("maxValue").asDouble();
					if(num>maxVal) {
						errors.add(new ValidationError(fieldKey, "Value must not exceed the maximum value : "+maxVal));
					}
				}
			}
			if(fieldType.equals("boolean")) {
				if(!formValue.isBoolean()) {
					errors.add(new ValidationError(fieldKey, "Value should be a true or false"));
					continue;
				}
				
			}
			 if (fieldSchema.has("enum")) {
				 ArrayNode enumValues = (ArrayNode) fieldSchema.get("enum");
	             boolean match = false;
	             for (JsonNode enumVal : enumValues) {
	            	 if (enumVal.asText().equals(formValue.asText())) {
	            		 match = true;
	                     break;
	                     }
	             }
	             if (!match) {
	            	 errors.add(new ValidationError(fieldKey,"Value must be one of values in : " + enumValues.toString()));
	             }
	        }
			
		}
		if (errors.isEmpty()) {
            return new ValidationResponseDTO(true, "Validation successful", null);
        } else {
        	System.out.print("Error at request");
            return new ValidationResponseDTO(false, "Validation failed", errors);
        }
	}

}

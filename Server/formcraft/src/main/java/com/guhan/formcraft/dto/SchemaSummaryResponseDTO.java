package com.guhan.formcraft.dto;

public class SchemaSummaryResponseDTO {
    private String schemaId;
    private String description;
    private String title;

    public SchemaSummaryResponseDTO() {}

    public SchemaSummaryResponseDTO(String schemaId, String description, String title) {
        this.schemaId = schemaId;
        this.description = description;
        this.title = title;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
    
}





package com.guhan.formcraft.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;



@Entity
@Table(name = "schemas")
public class Schema {
	
	public Schema() {
		
	}

    @Id
    @Column(name = "schema_id", length = 6)
    private String schemaId;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(name="schema_description",length = 255)
    private String schemaDescription;

    @Column(name = "schema_title",length = 50)
    private String schemaTitle;
    
    @Column(name = "json_schema", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode jsonSchema;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
    
	public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String user) {
		this.userId = user;
	}

	public String getSchemaDescription() {
		return schemaDescription;
	}

	public void setSchemaDescription(String schemaDescription) {
		this.schemaDescription = schemaDescription;
	}

	public String getSchemaTitle() {
		return schemaTitle;
	}

	public void setSchemaTitle(String schemaTitle) {
		this.schemaTitle = schemaTitle;
	}

	public JsonNode getJsonSchema() {
		return jsonSchema;
	}

	public void setJsonSchema(JsonNode jsonSchema) {
		this.jsonSchema = jsonSchema;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


	
    
}

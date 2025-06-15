package com.guhan.formcraft.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guhan.formcraft.models.Schema;
@Repository
public interface SchemaRepo extends JpaRepository<Schema, String> {
	
	List<Schema> findByUserId(String userid);
	
	Optional<Schema> findBySchemaId(String schemaId);

}


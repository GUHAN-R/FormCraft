package com.guhan.formcraft.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guhan.formcraft.models.Form;

@Repository
public interface FormRepo extends JpaRepository<Form, String>{
	 List<Form> findBySchema_SchemaId(String schemaId);
}

package com.guhan.formcraft.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guhan.formcraft.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>{

}

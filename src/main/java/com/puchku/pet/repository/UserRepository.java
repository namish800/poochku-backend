package com.puchku.pet.repository;

import com.puchku.pet.model.entities.Dog;
import com.puchku.pet.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

package com.kaba.academy.repository;

import com.kaba.academy.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Specifies methods used to obtain and modify password related information
 * which is stored in the database.
 * @author Petri Kainulainen
 */
public interface PasswordRepository extends JpaRepository<Password, Long> {
}

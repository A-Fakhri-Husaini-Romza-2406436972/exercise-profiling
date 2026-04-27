package com.advpro.profiling.tutorial.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advpro.profiling.tutorial.model.Student;

/**
 * @author muhammad.khadafi
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	// Return the student with highest GPA (database-side) to avoid loading all students into memory
	Optional<Student> findTopByOrderByGpaDesc();

	// Return only student names to reduce data transferred when only names are needed
	@org.springframework.data.jpa.repository.Query("SELECT s.name FROM Student s")
	java.util.List<String> findAllNames();
}
package com.pluralsight.ticket_api.repository;

import com.pluralsight.ticket_api.model.Agent;
import com.pluralsight.ticket_api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}

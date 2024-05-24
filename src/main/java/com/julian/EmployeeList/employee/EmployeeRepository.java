package com.julian.EmployeeList.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByNameContainsIgnoreCase(String query);

    List<Employee> findByNameContainsIgnoreCaseAndAgeLessThanEqual(String query, int age);
}

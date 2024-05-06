package com.julian.EmployeeList.employee;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @GetMapping
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable UUID id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee newEmployee,
                                                   UriComponentsBuilder ucb) {
        Employee savedEmployee = employeeRepository.save(newEmployee);
        URI location = ucb.path("employees/{id}").buildAndExpand(savedEmployee.getId()).toUri();
        return ResponseEntity.created(location).body(savedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable UUID id) {
        Optional<Employee> oldEmployee = employeeRepository.findById(id);
        if (oldEmployee.isPresent()) {
            employeeRepository.delete(oldEmployee.get());
            return ResponseEntity.ok(oldEmployee.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

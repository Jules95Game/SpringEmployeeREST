package com.julian.EmployeeList.employee;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

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
            logger.trace("Could not find Employee with id:%s".formatted(id));
            logger.debug("Could not find Employee with id:%s".formatted(id));
            logger.info("Could not find Employee with id:%s".formatted(id));
            logger.warn("Could not find Employee with id:%s".formatted(id));
            logger.error("Could not find Employee with id:%s".formatted(id));
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee newEmployee, UriComponentsBuilder ucb) {
        Employee savedEmployee = employeeRepository.save(newEmployee);
        URI location = ucb.path("employees/{id}").buildAndExpand(savedEmployee.getId()).toUri();
        return ResponseEntity.created(location).body(savedEmployee);
    }
}

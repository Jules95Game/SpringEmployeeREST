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

    private ResponseEntity<Employee> notFound() {
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Employee> findAll(@RequestParam String contains,
                                  @RequestParam(name = "max-age", required = false) Integer maxAge) {
//        return employeeRepository.findAll();
        return maxAge == null ? employeeRepository.findByNameContainsIgnoreCase(contains) :
                employeeRepository.findByNameContainsIgnoreCaseAndAgeLessThanEqual(contains, maxAge);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable UUID id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            logger.trace("Could not find Employee with id:%s".formatted(id));
            logger.debug("Could not find Employee with id:%s".formatted(id));
            logger.info("Could not find Employee with id:%s".formatted(id));
            logger.warn("Could not find Employee with id:%s".formatted(id));
            logger.error("Could not find Employee with id:%s".formatted(id));
            return notFound();
        } else {
            return ResponseEntity.ok(employee.get());
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
        if (oldEmployee.isEmpty()) {
            return notFound();
        } else {
            employeeRepository.delete(oldEmployee.get());
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable UUID id,
                                                   @RequestBody Employee replacementEmployee,
                                                   UriComponentsBuilder ucb) {
        if (!employeeRepository.existsById(id)) {
            return notFound();
        } else {
            replacementEmployee.setId(id);
            Employee updatedEmployee = employeeRepository.save(replacementEmployee);
            URI location = ucb.path("employees/{id}").buildAndExpand(updatedEmployee.getId()).toUri();
            return ResponseEntity.created(location).body(updatedEmployee);
        }
    }
}

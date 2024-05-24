package com.julian.EmployeeList.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MySeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (employeeRepository.count() == 0) {
            employeeRepository.saveAll(List.of(
                    new Employee("Gerben", 26),
                    new Employee("Bram", 22),
                    new Employee("Niels", 30)
            ));
        }
    }
}

package com.oocl.company.repositories;

import com.oocl.company.entities.Company;
import com.oocl.company.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByGender(String gender);
    Page<Employee> findAll(Pageable page);
}

package com.oocl.company.repositories;

import com.oocl.company.entities.Company;
import com.oocl.company.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByGender(String gender);
    Page<Employee> findAll(Pageable page);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Employee employee set employee.name = :name where employee.id = :id")
    int changeNameById(@Param("id") Long id, @Param("name") String name);
}

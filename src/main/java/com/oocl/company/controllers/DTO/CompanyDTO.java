package com.oocl.company.controllers.DTO;

import com.oocl.company.entities.Company;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyDTO {
    private final Long id;
    private final String name;
    private final ZonedDateTime create_date;
    private final List<EmployeeDTO> employees;

    public CompanyDTO(Company company) {
        id = company.getId();
        name = company.getName();
        create_date = company.getCreate_date();
        employees = company.getEmployees().stream().map(employee -> new EmployeeDTO(employee)).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getCreate_date() {
        return create_date;
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }


}

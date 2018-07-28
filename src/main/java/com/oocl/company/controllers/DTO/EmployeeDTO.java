package com.oocl.company.controllers.DTO;

import com.oocl.company.entities.Employee;

import java.time.ZonedDateTime;

public class EmployeeDTO {
    private final Long id;
    private final String name;
    private Long companyID;

    public EmployeeDTO(Employee employee){
        id = employee.getId();
        name = employee.getName();
        if(employee.getCompany() != null)
            companyID = employee.getCompany().getId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCompanyID() {
        return companyID;
    }



}

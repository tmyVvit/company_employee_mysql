package com.oocl.company.service;

import com.oocl.company.controllers.DTO.EmployeeDTO;
import com.oocl.company.entities.Employee;
import com.oocl.company.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("employeeService")
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }


    public List<EmployeeDTO> getEmployees(Pageable page) {
        return employeeRepository.findAll(page).stream().map(EmployeeDTO::new).collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(Long employeeID) {
        Employee employee = employeeRepository.findById(employeeID).orElse(null);
        return employee==null? null: new EmployeeDTO(employee);
    }

    public boolean save(Employee employee) {
        Employee save = employeeRepository.save(employee);
        return save!=null;
    }

    public List<EmployeeDTO> getEmployeeByGender(String gender) {
        return employeeRepository.findByGender(gender).stream().map(EmployeeDTO::new).collect(Collectors.toList());
    }

    public EmployeeDTO updateEmployee(Long employeeID, Employee employee) {
        Employee emp = employeeRepository.findById(employeeID).orElse(null);
        if(emp == null){
            return null;
        }
        employee.setId(emp.getId());
        employee.setCompany(emp.getCompany());
        employeeRepository.save(employee);
        return new EmployeeDTO(employee);
    }

    public EmployeeDTO deleteEmployeeById(Long employeeID) {
        Employee employee = employeeRepository.findById(employeeID).orElse(null);
        if(employee == null){
            return null;
        }
        employeeRepository.delete(employee);
        return new EmployeeDTO(employee);
    }
}

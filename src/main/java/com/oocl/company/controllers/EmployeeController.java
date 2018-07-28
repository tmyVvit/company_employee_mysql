package com.oocl.company.controllers;

import com.oocl.company.controllers.DTO.EmployeeDTO;
import com.oocl.company.entities.Company;
import com.oocl.company.entities.Employee;
import com.oocl.company.exceptions.BadRequestException;
import com.oocl.company.exceptions.ResourceNotFoundException;
import com.oocl.company.repositories.CompanyRepository;
import com.oocl.company.repositories.EmployeeRepository;
import org.aspectj.lang.annotation.DeclareError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    public EmployeeController(EmployeeRepository _employeeRepository){
        employeeRepository = _employeeRepository;
    }

    @Transactional
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeDTO> getAllEmployees(){
        List<EmployeeDTO> employees = new ArrayList<>();
//        List<Employee> employees = employeeRepository.findAll();
        employeeRepository.findAll().stream().forEach(employee -> employees.add(new EmployeeDTO(employee)));
        return employees;
    }

    @Transactional
    @GetMapping(path = "/{employeeID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDTO getEmployeeById(@PathVariable Long employeeID){
        Employee employee=  employeeRepository.findById(employeeID).orElseThrow(()->new ResourceNotFoundException("company not found"));
        return new EmployeeDTO(employee);
    }

    @Transactional
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDTO saveEmployee(@RequestBody Employee employee){
        return new EmployeeDTO(employeeRepository.save(employee));
    }

    @Transactional
    @PutMapping(path = "/{employeeID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateEmployeeById(@PathVariable Long employeeID, @RequestBody Employee employee){
        Employee emp =  employeeRepository.findById(employeeID).orElseThrow(()-> new BadRequestException("bad request"));
        employee.setId(employeeID);
        employee.setCompany(emp.getCompany());
        employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Transactional
    @DeleteMapping(path = "/{employeeID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDTO deleteEmployeeById(@PathVariable Long employeeID){
        Employee employee = employeeRepository.findById(employeeID).orElseThrow(()->new ResourceNotFoundException("employee not found"));
        EmployeeDTO employeeDTO = new EmployeeDTO(employee);
        Company company = employee.getCompany();
        if(company!= null){
            company.deleteEmployee(employee);
        }
        employeeRepository.delete(employee);
        return employeeDTO;
    }
}

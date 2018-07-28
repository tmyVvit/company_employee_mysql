package com.oocl.company.controllers;

import com.oocl.company.controllers.DTO.EmployeeDTO;
import com.oocl.company.entities.Employee;
import com.oocl.company.exceptions.BadRequestException;
import com.oocl.company.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeDTO> getAllEmployees(Pageable page){
        List<EmployeeDTO> employees = employeeService.getEmployees(page);
        if(employees.size() == 0){
            throw new BadRequestException("page not exists");
        }
        return employees;
    }

    @GetMapping(path = "/{employeeID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDTO getEmployeeById(@PathVariable Long employeeID){
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(employeeID);
        if(employeeDTO == null){
            throw new BadRequestException("employee not exists");
        }
        return employeeDTO;
    }

    @GetMapping(path = "/male", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeDTO> getMaleEmployees(){
        List<EmployeeDTO> employees = employeeService.getEmployeeByGender("male");
        if(employees.size() == 0){
            throw new BadRequestException("male employee not exists");
        }
        return employees;
    }


    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveEmployee(@RequestBody Employee employee){
        if(employeeService.save(employee)){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping(path = "/{employeeID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateEmployeeById(@PathVariable Long employeeID, @RequestBody Employee employee){
        if(employeeService.updateEmployee(employeeID, employee)){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new BadRequestException("employee not exists");
    }

    @DeleteMapping(path = "/{employeeID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDTO deleteEmployeeById(@PathVariable Long employeeID){
        EmployeeDTO employeeDTO = employeeService.deleteEmployeeById(employeeID);
        if(employeeDTO == null){
            throw new BadRequestException("employee not exists");
        }
        return employeeDTO;
    }
}

package com.oocl.company.controllers;


import com.oocl.company.controllers.DTO.CompanyDTO;
import com.oocl.company.entities.Company;
import com.oocl.company.entities.Employee;
import com.oocl.company.exceptions.BadRequestException;
import com.oocl.company.exceptions.ResourceNotFoundException;
import com.oocl.company.repositories.CompanyRepository;
import com.oocl.company.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    public CompanyController(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    @Transactional
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO saveCompany(@RequestBody Company company){
        company.getEmployees().stream().forEach(employee -> employee.setCompany(company));
        companyRepository.save(company);
        return new CompanyDTO(company);
    }

    @Transactional
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CompanyDTO> getAllCompany(){
        List<CompanyDTO> companies = new ArrayList<>();
        companyRepository.findAll().stream().forEach(company -> companies.add(new CompanyDTO(company)));
        return companies;
    }

    @Transactional
    @GetMapping(path = "/{companyID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO getAllCompany(@PathVariable Long companyID){
        Company company = companyRepository.findById(companyID).orElseThrow(()->new ResourceNotFoundException("company not found"));
        return new CompanyDTO(company);
    }

    @Transactional
    @PutMapping(path = "/{companyID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCompanyById(@PathVariable Long companyID, @RequestBody Company company){
        companyRepository.findById(companyID).orElseThrow(()-> new BadRequestException("bad request"));
        company.setId(companyID);
        companyRepository.save(company);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Transactional
    @DeleteMapping(path = "/{companyID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO deleteCompanyById(@PathVariable Long companyID){
        Company company = companyRepository.findById(companyID).orElseThrow(()->new ResourceNotFoundException("company not found"));
        CompanyDTO companyDTO = new CompanyDTO(company);
//        company.getEmployees().stream().forEach(employee -> employeeRepository.delete(employee));
        companyRepository.delete(company);
        return companyDTO;
    }

    @Transactional
    @PatchMapping(path = "/{companyID}/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO addTheEmployeeToTheCompany(@PathVariable Long companyID, @RequestBody Employee employee){
        Employee emp = employeeRepository.findById(employee.getId()).orElseThrow(()->new ResourceNotFoundException("Employee not found"));
        Company company = companyRepository.findById(companyID).orElseThrow(()->new ResourceNotFoundException("company not found"));
        emp.setCompany(company);
        company.addEmployee(emp);
        return new CompanyDTO(company);
    }
}

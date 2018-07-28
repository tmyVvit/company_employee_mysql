package com.oocl.company.controllers;


import com.oocl.company.controllers.DTO.CompanyDTO;
import com.oocl.company.controllers.DTO.EmployeeDTO;
import com.oocl.company.entities.Company;
import com.oocl.company.entities.Employee;
import com.oocl.company.exceptions.BadRequestException;
import com.oocl.company.exceptions.ResourceNotFoundException;
import com.oocl.company.repositories.CompanyRepository;
import com.oocl.company.repositories.EmployeeRepository;
import com.oocl.company.service.CompanyService;
import com.oocl.company.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

    private CompanyService companyService;
    private EmployeeService employeeService;

    @Autowired
    public CompanyController(CompanyService companyService, EmployeeService employeeService){
        this.companyService = companyService;
        this.employeeService = employeeService;
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveCompany(@RequestBody Company company){
        if(companyService.save(company)){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CompanyDTO> getAllCompany(Pageable page){

            List<CompanyDTO> companyDTOS = companyService.getAllCompany(page);
            if(companyDTOS.size() == 0){
                throw new BadRequestException("page not exist");
            }
            return companyDTOS;
    }

    @GetMapping(path = "/{companyID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO getAllCompany(@PathVariable Long companyID){
        CompanyDTO companyDTO = companyService.getById(companyID);
        if(companyDTO == null){
            throw new ResourceNotFoundException("page not exist");
        }
        return companyDTO;
    }

    @GetMapping(path = "/{companyID}/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeDTO> getEmployeesByCompany(@PathVariable Long companyID){
        List<EmployeeDTO> employees = companyService.getEmployeesByCompany(companyID);
        if(employees == null){
            throw new BadRequestException("company not exist");
        }
        return employees;
    }

    @PutMapping(path = "/{companyID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO updateCompanyById(@PathVariable Long companyID, @RequestBody Company company){
        CompanyDTO companyDTO = companyService.updateCompany(companyID, company);
        if(companyDTO == null){
            throw new BadRequestException("bad request, company not exist");
        }
        return companyDTO;
    }

    @DeleteMapping(path = "/{companyID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Company deleteCompanyById(@PathVariable Long companyID){
        Company company = companyService.deleteCompanyById(companyID);
        if(company == null){
            throw new BadRequestException("company not exist");
        }
        return company;
    }

    @PatchMapping(path = "/{companyID}/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public Company addTheEmployeeToTheCompany(@PathVariable Long companyID, @RequestBody Employee employee){
        Company company = companyService.addEmployeeToCompany(companyID, employee.getId());
        if(company == null){
            throw new BadRequestException("company or employee not exist");
        }
        return company;
    }
}

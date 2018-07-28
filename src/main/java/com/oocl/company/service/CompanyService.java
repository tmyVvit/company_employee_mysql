package com.oocl.company.service;

import com.oocl.company.controllers.DTO.CompanyDTO;
import com.oocl.company.controllers.DTO.EmployeeDTO;
import com.oocl.company.entities.Company;
import com.oocl.company.entities.Employee;
import com.oocl.company.exceptions.ResourceNotFoundException;
import com.oocl.company.repositories.CompanyRepository;
import com.oocl.company.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("companyService")
public class CompanyService {
    private CompanyRepository companyRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, EmployeeRepository employeeRepository){
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDTO> getEmployeesByCompany(Long companyID) {
        Company company = companyRepository.findById(companyID).orElse(null);
        if(company == null){
            return null;
        }
        return company.getEmployees().stream().map(EmployeeDTO::new).collect(Collectors.toList());
    }


    public boolean save(Company company) {
        company.getEmployees().forEach(employee -> employee.setCompany(company));
        Company save = companyRepository.save(company);
        return save != null;
    }

    public List<CompanyDTO> getAllCompany(Pageable page) {
//        List<CompanyDTO> companies = new ArrayList<>();
//        companyRepository.findAll(page).stream().forEach(company -> companies.add(new CompanyDTO(company)));
//        return companies;
        return companyRepository.findAll(page).stream().map(CompanyDTO::new).collect(Collectors.toList());
    }

    public CompanyDTO getById(Long companyID) {
        Company company = companyRepository.findById(companyID).orElse(null);
        if(company == null)
            return null;
        return new CompanyDTO(company);
    }

    public CompanyDTO updateCompany(Long companyID, Company company) {
        Company cm = companyRepository.findById(companyID).orElse(null);
        if(cm==null){
            return null;
        }
        cm.setName(company.getName());
        companyRepository.save(cm);
        return new CompanyDTO(cm);
    }

    public Company deleteCompanyById(Long companyID) {
        Company company = companyRepository.findById(companyID).orElse(null);
        companyRepository.delete(company);
        return company;
    }

    public Company addEmployeeToCompany(Long companyID, Long employeeID) {
        Company company = companyRepository.findById(companyID).orElse(null);
        Employee employee = employeeRepository.findById(employeeID).orElse(null);
        if(company == null || employee == null){
            return null;
        }
        employee.setCompany(company);
        company.addEmployee(employee);
//        employeeRepository.save(employee);
        companyRepository.save(company);

        return company;
    }
}

package com.oocl.company.repositories;

import com.oocl.company.controllers.DTO.CompanyDTO;
import com.oocl.company.entities.Company;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TestEntityManager  entityManager;

    @After
    public void tearDown() throws Exception{
        entityManager.clear();
    }

    @Test
    public void findAll(){
    // given
        entityManager.persist(new Company("oocl1"));
        entityManager.persist(new Company("oocl2"));
        // when
        List<Company> companies = companyRepository.findAll(PageRequest.of(0, 1)).getContent();
    // then
        assertThat(companies.size(), is(1));
        assertThat(companies.get(0).getName(), is("oocl1"));
    }

    @Test
    public void findById(){
        // given
        entityManager.persist(new Company("oocl1"));
        Long id = Long.valueOf(entityManager.persistAndGetId(new Company("oocl2")).toString());
        // when
        Company company = companyRepository.findById(id).orElse(null);
        // then
        assertThat(company.getName(), is("oocl2"));
    }

    @Test
    public void changeNameById(){
    // given
        Long id = Long.valueOf(entityManager.persistAndGetId(new Company("oocl")).toString());
    // when
        companyRepository.changeNameById(id, "change");
        Company company = companyRepository.findById(id).orElse(null);
    // then
        assertThat(company.getName(), is("change"));
    }
}

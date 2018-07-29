package com.oocl.company.repositories;

import com.oocl.company.entities.Employee;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @After
    public void tearDown() throws Exception{
        entityManager.clear();
    }

    @Test
    public void findByGender(){
    // given
        entityManager.persist(new Employee( "name1", "male"));
        entityManager.persist(new Employee( "name2", "female"));

        // when
        List<Employee> maleEmployees = employeeRepository.findByGender("male");
        List<Employee> femaleEmployees = employeeRepository.findByGender("female");
    // then
        assertThat(maleEmployees.size(), is(1));
        assertThat(maleEmployees.get(0).getName(), is("name1"));
        assertThat(femaleEmployees.size(), is(1));
        assertThat(femaleEmployees.get(0).getName(), is("name2"));
    }

    @Test
    public void findAll(){
    // given
        entityManager.persist(new Employee( "name1", "male"));
        entityManager.persist(new Employee( "name2", "female"));
    // when
        List<Employee> employees = employeeRepository.findAll(PageRequest.of(0,2)).getContent();
    // then
        assertThat(employees.size(), is(2));
        assertThat(employees.get(0).getName(), is("name1"));
        assertThat(employees.get(1).getName(), is("name2"));
    }

    @Test
    public void findById(){
        // given
        entityManager.persist(new Employee( "name1", "male"));
        entityManager.persist(new Employee( "name2", "female"));
        Long id = Long.valueOf(entityManager.persistAndGetId(new Employee( "name1", "male")).toString());
        // when
        Employee employee = employeeRepository.findById(id).orElse(null);
        // then
        assertThat(employee.getName(), is("name1"));
        assertThat(employee.getGender(), is("male"));
    }
}

package com.oocl.company.repositories;

import com.oocl.company.entities.Employee;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager entityManager;

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
        assertThat(maleEmployees.get(0).getId(), is(1L));
        assertThat(maleEmployees.get(0).getName(), is("name1"));
        assertThat(femaleEmployees.size(), is(1));
        assertThat(femaleEmployees.get(0).getName(), is("name2"));
        assertThat(femaleEmployees.get(0).getId(), is(2L));

    }
}

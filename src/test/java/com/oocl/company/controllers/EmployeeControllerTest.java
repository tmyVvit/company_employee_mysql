package com.oocl.company.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oocl.company.controllers.DTO.EmployeeDTO;
import com.oocl.company.entities.Employee;
import com.oocl.company.exceptions.BadRequestException;
import com.oocl.company.service.EmployeeService;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
@EnableSpringDataWebSupport
public class EmployeeControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;


    @Test
    public void return_employeeDTO_when_getEmployeeById() throws Exception {
        //given
        EmployeeDTO employeeDTO = new EmployeeDTO(new Employee(1L, "test-name", "male"));


        given(employeeService.getEmployeeById(1L)).willReturn(employeeDTO);
        given(employeeService.getEmployeeById(2L)).willThrow(new BadRequestException());

        //whenL
        //then
        mockMvc.perform(get("/api/v1/employees/1").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("name", containsString("name")))
                .andExpect(jsonPath("gender", is("male")));

        mockMvc.perform(get("/api/v1/employees/2").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void return_employeeDTO_list_when_getEmployees() throws Exception {
        //given
        EmployeeDTO employeeDTO = new EmployeeDTO(new Employee(1L, "test-name", "male"));

        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        employeeDTOS.add(employeeDTO);
        given(employeeService.getEmployees(any())).willReturn(employeeDTOS);

        //when
        //then
        mockMvc.perform(get("/api/v1/employees?page=1&size=1").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", containsString("name")))
                .andExpect(jsonPath("$[0].gender", is("male")));
    }

    @Test
    public void return_male_employeeDTO_list_when_getMaleEmployees() throws Exception {
        //given
        EmployeeDTO employeeDTO = new EmployeeDTO(new Employee(1L, "test-name", "male"));

        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        employeeDTOS.add(employeeDTO);
        given(employeeService.getEmployeeByGender("male")).willReturn(employeeDTOS);

        //when
        //then
        mockMvc.perform(get("/api/v1/employees/male").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", containsString("name")))
                .andExpect(jsonPath("$[0].gender", is("male")));
    }

    @Test
    public void return_created_status_when_post_employee() throws Exception{
    // given
        Employee employee = new Employee(1L, "test", "male");
        given(employeeService.save(any(Employee.class))).willReturn(true);
    // when
    // then
        mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON_VALUE).
                        content(mapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void should_retturn_no_content_status_when_update_employee() throws Exception{
    // given
        Employee employee = new Employee(1L, "name", "male");
        given(employeeService.updateEmployee(anyLong(), any(Employee.class))).willReturn(true, false);
    // when
    // then
        mockMvc.perform(put("/api/v1/employees/1").contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(employee)))
                .andExpect(status().isNoContent())
                .andDo(print());
        mockMvc.perform(put("/api/v1/employees/2").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void should_return_employeeDTO_when_delete_employee_by_id() throws Exception{
    // given
        Employee employee = new Employee(1L, "name", "male");
        EmployeeDTO employeeDTO = new EmployeeDTO(employee);
        given(employeeService.deleteEmployeeById(anyLong())).willReturn(employeeDTO);
    // when
    // then
        mockMvc.perform(delete("/api/v1/employees/1").contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(employee)))
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("name", is("name")))
                .andExpect(jsonPath("gender", is("male")));
    }
}
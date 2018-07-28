package com.oocl.company.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oocl.company.entities.Company;
import com.oocl.company.service.CompanyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    @Test
    public void should_return_created_status_when_save_company_success() throws Exception{
    // given
        Company company = new Company(1L, "oocl");
        given(companyService.save(any(Company.class))).willReturn(true);
    // when
    // then
        mockMvc.perform(post("/api/v1/companies").contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .content(mapper.writeValueAsString(company)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

}
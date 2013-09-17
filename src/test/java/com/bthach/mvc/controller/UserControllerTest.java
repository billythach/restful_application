package com.bthach.mvc.controller;

import com.bthach.persistence.entity.User;
import com.bthach.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class UserControllerTest {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        UserService userService = (UserService) ctx.getBean("userService");
        userService.save(new User("Carl", "Azoury", "carl.azoury@zenika.com"));
    }

    @Test
    public void list() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"email\":\"carl.azoury@zenika.com\",\"firstName\":\"Carl\",\"id\":1,\"lastName\":\"Azoury\",\"userType\":\"DEFAULT\"}]"));
    }

    @Test
    public void addUserByRequestParamsWithParamsMapping() throws Exception {
        mockMvc.perform(post("/user/add/m2")
                .param("lastName", "Thach")
                .param("firstName","Billy")
                .param("email", "billy.thach@zenika.com"))
                .andExpect(status().isCreated());
    }

    @Test
    public void addUserByRequestParamsWithEnumParamMapping() throws Exception {
        mockMvc.perform(post("/user/add/m3")
                .param("lastName", "Thach")
                .param("firstName","Billy")
                .param("email", "billy.thach@zenika.com")
                .param("userType", "ADMIN"))
                .andExpect(status().isCreated());
    }

    @Test
    public void addUserByParamsWithObjectMapping() throws Exception {
        mockMvc.perform(post("/user/add/m4")
                .param("lastName", "Thach")
                .param("firstName","Billy")
                .param("email", "billy.thach@zenika.com"))
                .andExpect(status().isCreated());
    }

    @Test
    public void addUserByMultipartFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("userFile", "{\"lastName\":\"THACH\",\"firstName\":\"Billy\",\"email\":\"billy.thach@zenika.com\",\"userType\":\"ADMIN\"}".getBytes());

        mockMvc.perform(fileUpload("/user/add/m5")
                .file(file)
                .param("status","IMPORT"))
                .andExpect(status().isCreated());
    }
}

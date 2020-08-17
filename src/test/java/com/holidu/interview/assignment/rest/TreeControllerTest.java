package com.holidu.interview.assignment.rest;

import com.holidu.interview.assignment.exceptions.TreeServiceException;
import com.holidu.interview.assignment.service.TreeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TreeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TreeService treeService;

    @InjectMocks
    private TreeController controller;


    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    public void test_countTreesInCircle_returnsTreeCountInCircle() throws Exception {
        Map<String, Integer> treeCount = new HashMap<>();
        treeCount.put("oak", 4);
        treeCount.put("pine", 3);
        when(treeService.countTrees(any())).thenReturn(treeCount);

        mockMvc.perform(get("/trees")
                .param("x", "1")
                .param("y", "3")
                .param("radius", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(readResourceAsString("/com/holidu/interview/assignment/rest/getTreeCount_response_success.json")));
    }

    @Test(expected = Exception.class)
    public void test_countTreesInCircle_throwsBadRequestException() throws Exception {
        when(treeService.countTrees(any())).thenThrow(new MissingServletRequestParameterException("radius", "double"));

        mockMvc.perform(get("/trees")
                .param("x", "1")
                .param("y", "3")
                .param("radius", "10"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(readResourceAsString("/com/holidu/interview/assignment/rest/getTreeCount_response_failure.json")));
    }

    @Test(expected = Exception.class)
    public void test_countTreesInCircle_throwsException() throws Exception {
        when(treeService.countTrees(any())).thenThrow(new Exception("Exception explanation"));

        mockMvc.perform(get("/trees")
                .param("x", "1")
                .param("y", "3")
                .param("radius", "10"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().json(readResourceAsString("/com/holidu/interview/assignment/rest/getTreeCount_response_failure.json")));
    }


    private InputStream readResource(String path) {
        InputStream is = this.getClass().getResourceAsStream(path);
        assertNotNull("Resource not found in " + path, is);
        return is;
    }

    private String readResourceAsString(String path) {
        InputStream is = readResource(path);
        try {
            return StreamUtils.copyToString(is, Charset.forName("UTF8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
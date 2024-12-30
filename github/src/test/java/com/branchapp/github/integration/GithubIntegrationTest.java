package com.branchapp.github.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GithubIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnUserDataFromRealGithubApi() throws Exception {
        mockMvc.perform(get("/github/user/octocat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("octocat"))
                .andExpect(jsonPath("$.display_name").value("The Octocat"))
                .andExpect(jsonPath("$.avatar").value("https://avatars.githubusercontent.com/u/583231?v=4"))
                .andExpect(jsonPath("$.geo_location").value("San Francisco"))
                .andExpect(jsonPath("$.repos").isArray())
                .andExpect(jsonPath("$.repos[0].name").value("boysenberry-repo-1"))
                .andExpect(jsonPath("$.repos[0].url").value("https://github.com/octocat/boysenberry-repo-1"))
                .andExpect(jsonPath("$.repos[1].name").exists())
                .andExpect(jsonPath("$.created_at").value("2011-01-25 18:44:36"));
    }

    @Test
    void shouldHandleUserNotFoundFromGithubApi() throws Exception {
        mockMvc.perform(get("/github/user/nonexistent-user"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.repos").doesNotExist())
                .andExpect(jsonPath("$.user_name").doesNotExist());
    }

}

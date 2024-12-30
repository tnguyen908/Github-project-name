package com.branchapp.github.unit.controller;

import com.branchapp.github.controller.GithubController;
import com.branchapp.github.exception.GithubServiceException;
import com.branchapp.github.model.GithubUser;
import com.branchapp.github.model.Repo;
import com.branchapp.github.service.GithubService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GithubController.class)
class GithubControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean
    private GithubService githubService;

    @Test
    void shouldReturnUserDetailsForValidUsername() throws Exception {
        // Mock service response
        List<Repo> repos = new java.util.ArrayList<>();
        repos.add(new Repo("repo1", "https://github.com/octocat/repo1"));
        GithubUser user = new GithubUser(
                "octocat",
                "The Octocat",
                "https://avatar.url",
                "San Francisco",
                null,
                "https://github.com/octocat",
                "2011-01-25 18:44:36",
                repos
        );
        when(githubService.getUserDetails("octocat")).thenReturn(user);

        mockMvc.perform(get("/github/user/octocat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("octocat"))
                .andExpect(jsonPath("$.display_name").value("The Octocat"))
                .andExpect(jsonPath("$.repos[0].name").value("repo1"));
    }

    @Test
    void shouldReturnNotFoundForInvalidUsername() throws Exception {
        when(githubService.getUserDetails("unknown")).thenThrow(new GithubServiceException("User not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/github/user/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    void shouldHandleInternalServerError() throws Exception {
        when(githubService.getUserDetails("octocat")).thenThrow(new GithubServiceException("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(get("/github/user/octocat"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal server error"));
    }
}





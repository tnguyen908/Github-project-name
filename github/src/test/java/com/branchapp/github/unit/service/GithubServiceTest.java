package com.branchapp.github.unit.service;

import com.branchapp.github.client.GithubClient;
import com.branchapp.github.exception.GithubServiceException;
import com.branchapp.github.model.GithubUser;
import com.branchapp.github.model.RepoResponse;
import com.branchapp.github.model.UserResponse;
import com.branchapp.github.service.GithubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubServiceTest {

    @Mock private GithubClient githubClient;
    @InjectMocks private GithubService githubService;

    @Test
    void shouldReturnGithubUserWhenValidUsername() {
        // Mock user and repo responses
        UserResponse userResponse = new UserResponse(
                "octocat",
                "The Octocat",
                "https://avatar.url",
                "San Francisco",
                null,
                "https://github.com/octocat",
                "2011-01-25T18:44:36Z"
        );
        List<RepoResponse> repoResponses = List.of(
                new RepoResponse("repo1", "https://github.com/octocat/repo1"),
                new RepoResponse("repo2", "https://github.com/octocat/repo2")
        );

        when(githubClient.getUser("octocat")).thenReturn(userResponse);
        when(githubClient.getRepos("octocat")).thenReturn(repoResponses);

        // Call service
        GithubUser user = githubService.getUserDetails("octocat");

        // Verify
        assertEquals("octocat", user.getUser_name());
        assertEquals("The Octocat", user.getDisplay_name());
        assertEquals("San Francisco", user.getGeo_location());
        assertEquals(2, user.getRepos().size());
        assertEquals("repo1", user.getRepos().get(0).getName());
    }

    @Test
    void shouldThrowNotFoundWhenUserDoesNotExist() {
        // Simulate FeignException for 404
        when(githubClient.getUser("unknown"))
                .thenThrow(new GithubServiceException("Resource not found", HttpStatus.NOT_FOUND));

        GithubServiceException exception = assertThrows(GithubServiceException.class, () -> {
            githubService.getUserDetails("unknown");
        });

        assertEquals("Resource not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void shouldThrowInternalServerErrorForOtherErrors() {
        // Simulate FeignException for 500
        when(githubClient.getUser("octocat"))
                .thenThrow(new GithubServiceException("Error fetching data", HttpStatus.INTERNAL_SERVER_ERROR));

        GithubServiceException exception = assertThrows(GithubServiceException.class, () -> {
            githubService.getUserDetails("octocat");
        });

        assertEquals("Error fetching data", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }
}

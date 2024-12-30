package com.branchapp.github.service;

import com.branchapp.github.client.GithubClient;
import com.branchapp.github.exception.GithubServiceException;
import com.branchapp.github.model.GithubUser;
import com.branchapp.github.model.RepoResponse;
import com.branchapp.github.model.UserResponse;
import com.branchapp.github.util.GithubUserConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GithubService {

    private static final Logger logger = LoggerFactory.getLogger(GithubService.class);
    private final GithubClient githubClient;

    public GithubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    /**
     * Fetches user details from GitHub API, including repositories.
     * The results are cached for subsequent requests to reduce API load.
     *
     * @param username the GitHub username to fetch details for
     * @return a transformed GithubUser object
     */
    @Cacheable(value = "githubUsers", key = "#username", unless = "#result == null")
    public GithubUser getUserDetails(String username) {
        logger.info("Fetching details for username: {}", username);

        try {
            UserResponse userResponse = githubClient.getUser(username);
            List<RepoResponse> repoResponses = githubClient.getRepos(username);

            GithubUser githubUser = GithubUserConverter.convert(userResponse, repoResponses);
            logger.info("Successfully processed data for username: {}", username);
            return githubUser;

        } catch (GithubServiceException e) {
            if (e.getStatus() == HttpStatus.NOT_FOUND) {
                logger.warn("User not found: {}", username);
                throw e;
            }
            logger.error("Service exception for username: {}", username, e);
            throw new GithubServiceException("Error fetching data", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Unexpected error for username: {}", username, e);
            throw new GithubServiceException("Error fetching data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

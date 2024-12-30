package com.branchapp.github.controller;

import com.branchapp.github.model.GithubUser;
import com.branchapp.github.service.GithubService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/github")
@Validated
public class GithubController {

    private static final Logger logger = LoggerFactory.getLogger(GithubController.class);
    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    // Max of 20 character for username for now. Can consider different
    // use cases later and update the max or min char count
    @GetMapping("/user/{username}")
    public ResponseEntity<GithubUser> getUserDetails(
            @PathVariable("username") @NotBlank @Size(max = 20) String username) {
        logger.info("Received request to fetch details for username: {}", username);

        try {
            GithubUser user = githubService.getUserDetails(username);
            logger.info("Successfully fetched details for username: {}", username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error while fetching details for username: {}", username, e);
            throw e;
        }
    }
}

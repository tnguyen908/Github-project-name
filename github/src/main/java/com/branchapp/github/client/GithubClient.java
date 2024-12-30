package com.branchapp.github.client;

import com.branchapp.github.config.FeignConfig;
import com.branchapp.github.model.RepoResponse;
import com.branchapp.github.model.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "githubClient", url = "https://api.github.com", configuration = FeignConfig.class)
public interface GithubClient {

    @GetMapping("/users/{username}")
    UserResponse getUser(@PathVariable String username);

    @GetMapping("/users/{username}/repos")
    List<RepoResponse> getRepos(@PathVariable String username);
}


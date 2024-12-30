package com.branchapp.github.util;

import com.branchapp.github.model.GithubUser;
import com.branchapp.github.model.Repo;
import com.branchapp.github.model.RepoResponse;
import com.branchapp.github.model.UserResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GithubUserConverter {

    public static GithubUser convert(UserResponse userResponse, List<RepoResponse> repoResponses) {
        if (userResponse == null) {
            throw new IllegalArgumentException("UserResponse cannot be null");
        }

        return new GithubUser(
                userResponse.getLogin(),
                userResponse.getName(),
                userResponse.getAvatar_url(),
                userResponse.getLocation(),
                userResponse.getEmail(),
                userResponse.getHtml_url(),
                DateTimeUtil.formatIsoToCustom(userResponse.getCreated_at()),
                convertRepos(repoResponses)
        );
    }

    private static List<Repo> convertRepos(List<RepoResponse> repoResponses) {
        if (repoResponses == null) {
            return Collections.emptyList();
        }
        return repoResponses.stream()
                .map(repo -> new Repo(repo.getName(), repo.getHtml_url()))
                .collect(Collectors.toList());
    }
}


package com.branchapp.github.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GithubServiceException extends RuntimeException {

    private final HttpStatus status;

    public GithubServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}


package com.branchapp.github.config;

import com.branchapp.github.exception.GithubServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    static class CustomErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            return switch (response.status()) {
                case 404 -> new GithubServiceException("Resource not found", HttpStatus.NOT_FOUND);
                case 500 ->
                        new GithubServiceException("Internal GitHub server error", HttpStatus.INTERNAL_SERVER_ERROR);
                default -> new RuntimeException("Generic error");
            };
        }
    }
}

package com.branchapp.github.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Repo {
    private String name;
    private String url;
}

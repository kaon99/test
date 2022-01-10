package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class GitHubRepositoryDetails {
    private String repoName;
    private String ownerLogin;
    private Boolean fork;
    private List<BranchDetails> branchDetails;

    @JsonIgnore
    public Boolean getFork() {
        return fork;
    }
}

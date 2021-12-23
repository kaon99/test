package com.model;

import lombok.Data;

import java.util.List;

@Data
public class GitHubRepositoryDetails {
    private String repoName;
    private String ownerLogin;
    private List<BranchDetails> branchDetails;

}

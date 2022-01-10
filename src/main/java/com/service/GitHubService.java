package com.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mapper.GitHubMapper;
import com.model.BranchDetails;
import com.model.GitHubRepositoryDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


@Controller

public class GitHubService {

    private final RestTemplate restTemplate;
    private final GitHubMapper repositoryMapper;
    private final String httpRepo;
    private final String httpBranches;

    public GitHubService(RestTemplate restTemplate, GitHubMapper repositoryMapper, @Value("${https.repo}") String httpRepo, @Value("${https.branches}") String httpBranches) {
        this.restTemplate = restTemplate;
        this.repositoryMapper = repositoryMapper;
        this.httpRepo = httpRepo;
        this.httpBranches = httpBranches;
    }


    public List<GitHubRepositoryDetails> gitHubRepositories(String userName) {
        List<GitHubRepositoryDetails> repositories = repositoryMapper
                .mapToGitHubRepositoryDetailsList(restTemplate.getForObject(
                        String.format(httpRepo, userName), ArrayNode.class));
        return repositories.stream()
                .filter(repo -> !repo.getFork())
                .peek(repo -> repo.setBranchDetails(getBranchDetails(repo.getOwnerLogin(), repo.getRepoName())))
                .collect(Collectors.toList());
    }

    private List<BranchDetails> getBranchDetails(String userName, String repoName) {
        return repositoryMapper.mapToBranchResult(restTemplate.getForObject(
                String.format(httpBranches, userName, repoName), ArrayNode.class));
    }
}

package com.service;

import com.constants.Constants;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mapper.GitHubMapper;
import com.model.BranchDetails;
import com.model.GitHubRepositoryDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static com.constants.Constants.*;


@Controller

public class GitHubService {

    private final RestTemplate restTemplate;
    private final GitHubMapper repositoryMapper;
    private final String httpRepo;
    private final String httpBranches;

    public GitHubService(RestTemplate restTemplate, GitHubMapper repositoryMapper,
                         @Value("${https.repo}") String httpRepo,
                         @Value("${https.branches}") String httpBranches) {
        this.restTemplate = restTemplate;
        this.repositoryMapper = repositoryMapper;
        this.httpRepo = httpRepo;
        this.httpBranches = httpBranches;
    }


    public List<GitHubRepositoryDetails> gitHubRepositories(String userName, int page) {
        List<GitHubRepositoryDetails> repositories = repositoryMapper
                .mapToGitHubRepositoryDetailsList(restTemplate.getForObject(String.format(httpRepo, userName),
                        ArrayNode.class));
        return repositories.parallelStream()
                .filter(repo -> !repo.getFork())
                .peek(repo -> repo.setBranchDetails(getBranchDetails(repo.getOwnerLogin(), repo.getRepoName(), page)))
                .collect(Collectors.toList());
    }

    private List<BranchDetails> getBranchDetails(String userName, String repoName, int page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(PER_PAGE, ONE_HUNDRED);
        headers.add(PAGE, String.valueOf(page));
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ArrayNode> exchange = restTemplate.exchange(String.format(httpBranches, userName, repoName),
                HttpMethod.GET, httpEntity, ArrayNode.class);
        return repositoryMapper.mapToBranchResult(exchange.getBody());
    }
}

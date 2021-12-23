package com.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.model.BranchDetails;
import com.model.GitHubRepositoryDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.Constant.*;


@Controller
@RequiredArgsConstructor
public class GitHubService {

    final private RestTemplate restTemplate;

    @Value("${https.repo}")
    private String httpRepo;
    @Value("${https.branches}")
    private String httpBranches;


    public List<GitHubRepositoryDetails> gitHubRepositories(String userName) {
        List<GitHubRepositoryDetails> repoResult = new ArrayList<>();
        ArrayNode repositories = restTemplate.getForObject(String.format(httpRepo, userName), ArrayNode.class);
        if (repositories != null) {
            for (JsonNode repo : repositories) {
                if (!repo.get(FORK).asBoolean()) {
                    GitHubRepositoryDetails gitHubDetails = new GitHubRepositoryDetails();
                    gitHubDetails.setRepoName(repo.get(NAME).textValue());
                    gitHubDetails.setOwnerLogin(userName);
                    gitHubDetails.setBranchDetails(getBranchDetails(userName, gitHubDetails.getRepoName()));
                    repoResult.add(gitHubDetails);
                }
            }
        }
        return repoResult;

    }

    private List<BranchDetails> getBranchDetails(String userName, String repoName) {
        List<BranchDetails> branchResult = new ArrayList<>();
        ArrayNode branches = restTemplate.getForObject(String.format(httpBranches, userName, repoName), ArrayNode.class);
        if (branches != null) {
            for (JsonNode branch : branches) {
                BranchDetails branchDetails = new BranchDetails();
                branchDetails.setBranchName(branch.get(NAME).textValue());
                branchDetails.setLastCommitSha(branch.get(COMMIT).get(SHA).textValue());
                branchResult.add(branchDetails);
            }
        }
        return branchResult;

    }
}

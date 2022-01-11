package com.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.model.BranchDetails;
import com.model.GitHubRepositoryDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.constants.Constants.*;

@Component
public class GitHubMapper {

    public List<GitHubRepositoryDetails> mapToGitHubRepositoryDetailsList(ArrayNode arrayNode) {
        List<GitHubRepositoryDetails> result = new ArrayList<>();
        if (arrayNode != null) {
            for (JsonNode repo : arrayNode) {
                if (!repo.get(FORK).asBoolean()) {
                    GitHubRepositoryDetails gitHubDetails = new GitHubRepositoryDetails();
                    gitHubDetails.setRepoName(repo.get(NAME).textValue());
                    gitHubDetails.setFork(repo.get(FORK).asBoolean());
                    gitHubDetails.setOwnerLogin(repo.get(OWNER).get(LOGIN).textValue());
                    result.add(gitHubDetails);
                }
            }
        }
        return result;
    }

    public List<BranchDetails> mapToBranchResult(ArrayNode arrayNode) {
        List<BranchDetails> result = new ArrayList<>();
        if (arrayNode != null) {
            for (JsonNode branch : arrayNode) {
                BranchDetails branchDetails = new BranchDetails();
                branchDetails.setBranchName(branch.get(NAME).textValue());
                branchDetails.setLastCommitSha(branch.get(COMMIT).get(SHA).textValue());
                result.add(branchDetails);
            }
        }
        return result;
    }
}

package com.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mapper.GitHubMapper;
import com.model.GitHubRepositoryDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    public static final String REPOSITORY_RESPONSE = "src/test/resources/gitHubRepositoryResponse.json";
    public static final String BRANCH_RESPONSE = "src/test/resources/gitHubBranchResponse.json";
    @Mock
    private RestTemplate restTemplate;
    private final GitHubMapper repositoryMapper = new GitHubMapper();
    private final String httpRepo = "repo/test-user";
    private final String httpBranches = "branch/test-user/test1";
    @InjectMocks
    private GitHubService gitHubService;

    @BeforeEach
    void setUp() {
        gitHubService = new GitHubService(restTemplate,repositoryMapper, httpRepo, httpBranches);
    }

    @Test
    void shouldReturnOneRepository() throws IOException {
        Mockito.when(restTemplate.getForObject((httpRepo), ArrayNode.class))
                .thenReturn(getArrayNode(REPOSITORY_RESPONSE));
        Mockito.when(restTemplate.getForObject((httpBranches), ArrayNode.class))
                .thenReturn(getArrayNode(BRANCH_RESPONSE));

        List<GitHubRepositoryDetails> result = gitHubService.gitHubRepositories("test-user",1);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getRepoName()).isEqualTo("test1");
        assertThat(result.get(0).getOwnerLogin()).isEqualTo("test-user");
        assertThat(result.get(0).getBranchDetails().get(0).getBranchName()).isEqualTo("main");
        assertThat(result.get(0).getBranchDetails().get(0).getLastCommitSha()).isEqualTo("12345");
    }

    private ArrayNode getArrayNode(String path) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(Files.readAllBytes(Paths.get(path)), ArrayNode.class);
    }


}
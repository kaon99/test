package com.controller;

import com.model.BranchDetails;
import com.model.GitHubRepositoryDetails;
import com.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GitHubControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private GitHubService gitHubService;

    @Test
    void shouldReturnOneRepository() throws Exception {
        List<GitHubRepositoryDetails> list = getGitHubRepositoryList();

        Mockito.when(gitHubService.gitHubRepositories("test-user",1)).thenReturn(list);
        String expectedResult = new String(Files.readAllBytes(Paths.get("src/test/resources/oneRepository.json")));
        mvc.perform(get("/users/test-user/repositories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    void shouldFailedWithNonExistUser() throws Exception {
        Mockito.when(gitHubService.gitHubRepositories("non-exist-user",1))
                .thenThrow(HttpClientErrorException.NotFound.class);
        String expectedResult = new String(Files.readAllBytes(Paths.get("src/test/resources/notFound.json")));

        mvc.perform(get("/users/non-exist-user/repositories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedResult));
    }

    @Test
    void shouldFailedWithNotAcceptable() throws Exception {
        String expectedResult = new String(Files.readAllBytes(Paths.get("src/test/resources/notAcceptable.json")));
        mvc.perform(get("/users/test-user/repositories")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable()).andExpect(content().json(expectedResult));

    }

    private List<GitHubRepositoryDetails> getGitHubRepositoryList() {
        GitHubRepositoryDetails gitHubRepositoryDetails = new GitHubRepositoryDetails();
        gitHubRepositoryDetails.setRepoName("test-repo-name");
        gitHubRepositoryDetails.setOwnerLogin("test-user");
        BranchDetails branchDetails = new BranchDetails();
        branchDetails.setBranchName("master");
        branchDetails.setLastCommitSha("123456");
        gitHubRepositoryDetails.setBranchDetails(Collections.singletonList(branchDetails));
        return Collections.singletonList(gitHubRepositoryDetails);
    }
}

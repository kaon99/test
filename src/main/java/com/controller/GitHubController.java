package com.controller;

import com.model.GitHubRepositoryDetails;
import com.service.GitHubService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class GitHubController {
    private GitHubService gitHubService;

    @GetMapping(value = "/users/{name}/repositories")
    public List<GitHubRepositoryDetails> getGitHubRepositoryList(@RequestHeader(value = "page",defaultValue = "1")
                                                                 Integer page,
                                                                 @PathVariable @NonNull String name) {
        return gitHubService.gitHubRepositories(name, page);
    }


}

package fr.paris8univ.iut.csid.csidwebrepositorybase.repository.exposition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitRepositoryDTO {

    private String name;
    private String login;
    private int open_issues;
    private int forks;

    public GitRepositoryDTO(String name, String login,int open_issues,int forks){
        this.name=name;
        this.login=login;
        this.open_issues=open_issues;
        this.forks=forks;
    }

    public String getName() {
        return this.name;
    }

    public String getLogin() {
        return this.login;
    }

    public int getIssues() {
        return this.open_issues;
    }

    public int getForks() {
        return this.forks;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setLogin(String login) {
        this.login=login;
    }

    public void setIssues(int open_issues) {
        this.open_issues=open_issues;
    }

    public void setForks(int forks) {
        this.forks=forks;
    }
}

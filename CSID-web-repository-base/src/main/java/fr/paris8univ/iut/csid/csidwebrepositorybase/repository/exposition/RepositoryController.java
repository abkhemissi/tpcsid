package fr.paris8univ.iut.csid.csidwebrepositorybase.repository.exposition;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import fr.paris8univ.iut.csid.csidwebrepositorybase.repository.core.GitRepository;
import fr.paris8univ.iut.csid.csidwebrepositorybase.repository.core.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/repositories")
public class RepositoryController {


    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService=repositoryService;
    }

    @GetMapping
    public List<GitRepository> getRepositories(){
        return repositoryService.getRepositories();
    }

    @GetMapping("/{name}")
    public ResponseEntity<GitRepository> findOneRepository(@PathVariable String name) throws RestClientException, URISyntaxException{
        return repositoryService.findOneRepository(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<GitRepository> createRepository(@RequestBody GitRepository gitRepository) throws URISyntaxException{
        repositoryService.createRepository(gitRepository);
        return ResponseEntity.created(new URI("/repositories/"+gitRepository.getName())).build();

    }

    @PutMapping("/{name}")
    public ResponseEntity<Object> putRepository(@PathVariable String name,@RequestBody GitRepository gitRepository) {
        repositoryService.putRepository( name, gitRepository);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{name}")
    public ResponseEntity<Object> patchRepository(@PathVariable String name,@RequestBody GitRepository gitRepository) {
        repositoryService.patchRepository(name,gitRepository);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{name}")
    public void deleteRepository(@PathVariable String name) {
        repositoryService.deleteRepository(name);
    }


}


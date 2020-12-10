package fr.paris8univ.iut.csid.csidwebrepositorybase.repository.git;

import fr.paris8univ.iut.csid.csidwebrepositorybase.repository.core.GitRepository;
import fr.paris8univ.iut.csid.csidwebrepositorybase.repository.dao.GitRepositoryDao;
import fr.paris8univ.iut.csid.csidwebrepositorybase.repository.dao.GitRepositoryEntity;
import fr.paris8univ.iut.csid.csidwebrepositorybase.repository.dao.GithubRepositoryDao;
import fr.paris8univ.iut.csid.csidwebrepositorybase.repository.exposition.GitRepositoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GitRepositoryRepository {

    private final GitRepositoryDao gitRepositoryDao;
    private final GithubRepositoryDao gitHubRepositoryDao;
    private final DateFormat dateFormatEntity= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final DateFormat dateFormatNormal = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Autowired
    public GitRepositoryRepository(GitRepositoryDao gitRepositoryDao,GithubRepositoryDao gitHubRepositoryDao) {
        this.gitRepositoryDao=gitRepositoryDao;
        this.gitHubRepositoryDao=gitHubRepositoryDao;
    }

    public List<GitRepository> getRepositories(){
        List<GitRepositoryEntity> repositoryEntities = gitRepositoryDao.findAll();


        return repositoryEntities.stream()
                .map(x -> new GitRepository(x.getName(),x.getOwner(),x.getIssues(),x.getFork(),dateFormatNormal.format(x.getlastUpdate())))
                .collect(Collectors.toList());
    }

    public Optional<GitRepository> findOneRepoForPatch(String name){
       
        return Optional.of(gitRepositoryDao.findById(name)
                .map(x-> new GitRepository(x.getName(),x.getOwner(),x.getIssues(),x.getFork(),dateFormatNormal.format(x.getlastUpdate()))).get());
    }

    public Optional<GitRepository> findOneRepository(String name) throws RestClientException, URISyntaxException{
        GitRepositoryEntity actualRepository = gitRepositoryDao.findById(name).get();

        GitRepository toReturn= new GitRepository(actualRepository.getName(),actualRepository.getOwner(),actualRepository.getIssues(),actualRepository.getFork(),dateFormatNormal.format(actualRepository.getlastUpdate()));


        Date actualDate = new Date();
        String strDate = dateFormatNormal.format(actualDate);


 //       if((Instant.now().getEpochSecond()-toReturn.getLastUpdate())>300) {
            GitRepositoryDTO gitInfo = gitHubRepositoryDao.getIssuesAndForks(toReturn.getName(), toReturn.getOwner());
            toReturn.setIssues(gitInfo.getIssues());
            toReturn.setFork(gitInfo.getForks());
            //*********
            toReturn.setLastUpdate(strDate);
            patchRepository(toReturn);
 //       }

        return Optional.of(toReturn);
    }

    public void createRepository(GitRepository gitRepository){
        //remplacer string par date
        
        Date date = null;
        try {
            date = dateFormatEntity.parse(gitRepository.getLastUpdate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        gitRepositoryDao.save(new GitRepositoryEntity(gitRepository.getName(),gitRepository.getOwner(),gitRepository.getIssues(),gitRepository.getFork(),date));
    }

    public void putRepository(String name,GitRepository gitRepository) {

        Optional<GitRepositoryEntity> repository = gitRepositoryDao.findById(name);

        //Si repo n'existe pas on le créer sinon on l'update avec les nouvelles valeurs.
        if(repository.isEmpty()) {
            createRepository(gitRepository);
        }
        else {

            GitRepositoryEntity repositoryModified = repository.get();
            repositoryModified.setOwner(gitRepository.getOwner());
            repositoryModified.setIssues(gitRepository.getIssues());
            repositoryModified.setFork(gitRepository.getFork());
            gitRepositoryDao.save(repositoryModified);
        }

    }

    public void patchRepository(GitRepository gitRepo) {
        Date date = null;
        try {
            date = dateFormatEntity.parse(gitRepo.getLastUpdate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GitRepositoryEntity repositoryPatched = new GitRepositoryEntity(gitRepo.getName(),gitRepo.getOwner(),gitRepo.getIssues(),gitRepo.getFork(),date);
        gitRepositoryDao.save(repositoryPatched);
    }

    public void deleteRepository(String name) {
        gitRepositoryDao.deleteById(name);
    }

}

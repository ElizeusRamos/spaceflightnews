package passos.articles;

import com.spaceflightsnews.controller.ArticleController;
import com.spaceflightsnews.model.Article;
import com.spaceflightsnews.repository.ArticleRepositoryImpl;
import com.spaceflightsnews.service.FindArticleByIdService;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Então;
import cucumber.api.java.pt.Quando;
import org.junit.Assert;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindArticleByIdPassos {

    private ArticleController articleController;
    private ArticleRepositoryImpl repository;
    private FindArticleByIdService service;
    private Long id;
    private ResponseEntity<Article> response;


    @Dado("^parâmetro id = (\\d+)$")
    public void parâmetro_id(Long id) throws Throwable {
        this.id = id;
    }

    @Quando("^O serviço de Buscar article por id$")
    public void o_serviço_de_Buscar_article_por_id() throws Throwable {

        repository = mock(ArticleRepositoryImpl.class);
        when(repository.findArticle(0L)).thenReturn(new Article());

        service = new FindArticleByIdService(
                repository
        );

        articleController = new ArticleController(
                null,
                null,
                null,
                null,
                service
        );

        response = articleController.findArticleById(this.id);
    }

    @Então("^O status HTTP do consultar deverá ser (\\d+)$")
    public void o_status_HTTP_do_consultar_deverá_ser(int httpStatus) throws Throwable {
        Assert.assertEquals(response.getStatusCodeValue(), httpStatus);
    }
}

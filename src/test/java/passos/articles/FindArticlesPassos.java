package passos.articles;

import com.spaceflightsnews.controller.ArticleController;
import com.spaceflightsnews.model.Article;
import com.spaceflightsnews.repository.ArticleRepositoryImpl;
import com.spaceflightsnews.service.FindArticlesPageableService;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Então;
import cucumber.api.java.pt.Quando;
import org.junit.Assert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindArticlesPassos {

    private ArticleController articleController;
    private ArticleRepositoryImpl repository;
    private FindArticlesPageableService service;
    private Boolean sucesso;
    private ResponseEntity<Page<Article>> response;



    @Dado("^parâmetro sucesso = \"([^\"]*)\"$")
    public void parâmetro_sucesso(String sucesso) throws Throwable {
        this.sucesso = Boolean.parseBoolean(sucesso);
    }

    @Quando("^O serviço de Buscar article lista$")
    public void o_serviço_de_Buscar_article_lista() throws Throwable {

        Pageable pageableRequest = PageRequest.of(0,1);

        repository = mock(ArticleRepositoryImpl.class);
        Page<Article> pageable = new PageImpl<Article>(Collections.emptyList(), pageableRequest, 0);
        when(repository.findAllArticlePage(any())).thenReturn(pageable);

        service = new FindArticlesPageableService(
                repository
        );

        articleController = new ArticleController(
                null,
                service,
                null,
                null,
                null
        );

        response = articleController.findAllArticlesPage(pageableRequest);
    }

    @Então("^O status HTTP do enviar deverá ser (\\d+)$")
    public void o_status_HTTP_do_enviar_deverá_ser(int httpStatus) throws Throwable {
        Assert.assertEquals(response.getStatusCodeValue(), httpStatus);
    }
}

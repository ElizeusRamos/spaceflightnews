package com.spaceflightsnews.service;

import com.spaceflightsnews.client.SpaceFlightNewsClient;
import com.spaceflightsnews.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledPullArticlesService {
    private final SpaceFlightNewsClient client;
    private final AddAllArticlesService service;

    @Autowired
    public ScheduledPullArticlesService(SpaceFlightNewsClient client, AddAllArticlesService service) {
        this.client = client;
        this.service = service;
    }

    public void execute() {
        var response = client.findArticlesByPageable(500);
        List<Article> listaArticleResponse = response.collectList().block();
        service.addAllArticlesService(listaArticleResponse);
    }
}

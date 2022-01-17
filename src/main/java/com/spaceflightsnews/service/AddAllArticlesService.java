package com.spaceflightsnews.service;

import com.spaceflightsnews.model.Article;
import com.spaceflightsnews.repository.ArticleRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddAllArticlesService {
    private final ArticleRepositoryImpl articleRepository;

    @Autowired
    public AddAllArticlesService(ArticleRepositoryImpl articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void addAllArticlesService(List<Article> listaArticles){
        var listaNotInTable = new ArrayList<Article>();
        for (var it:listaArticles) {
            var exists = articleRepository.existsArticle(it.getId());
            if(exists == 0)
                listaNotInTable.add(it);
        }
        articleRepository.addMultipleArticles(listaNotInTable);
    }
}

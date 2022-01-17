package com.spaceflightsnews.service;

import com.spaceflightsnews.model.Article;
import com.spaceflightsnews.repository.ArticleRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddArticleService {
    private final ArticleRepositoryImpl articleRepository;

    @Autowired
    public AddArticleService(ArticleRepositoryImpl articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void execute(Article body) {
        var exists = articleRepository.existsArticle(body.getId());
        if(exists == 0)
            articleRepository.addArticle(body);
    }
}

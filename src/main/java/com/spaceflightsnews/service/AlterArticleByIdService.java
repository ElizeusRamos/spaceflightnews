package com.spaceflightsnews.service;

import com.spaceflightsnews.model.Article;
import com.spaceflightsnews.repository.ArticleRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlterArticleByIdService {
    private final ArticleRepositoryImpl articleRepository;

    @Autowired
    public AlterArticleByIdService(ArticleRepositoryImpl articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void execute(Long id, Article body) {
        var exists = articleRepository.existsArticle(id);
        if(exists != 0)
            articleRepository.alterArticleById(id, body);
    }
}

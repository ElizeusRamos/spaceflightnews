package com.spaceflightsnews.service;

import com.spaceflightsnews.model.Article;
import com.spaceflightsnews.repository.ArticleRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindArticleByIdService {
    private final ArticleRepositoryImpl articleRepository;

    @Autowired
    public FindArticleByIdService(ArticleRepositoryImpl articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article execute(Long id){
        return articleRepository.findArticle(id);
    }
}

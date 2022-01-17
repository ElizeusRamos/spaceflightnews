package com.spaceflightsnews.service;

import com.spaceflightsnews.model.Article;
import com.spaceflightsnews.repository.ArticleRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FindArticlesPageableService {
    private final ArticleRepositoryImpl articleRepository;

    @Autowired
    public FindArticlesPageableService(ArticleRepositoryImpl articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Page<Article> execute(Pageable pageable){
        return articleRepository.findAllArticlePage(pageable);
    }
}

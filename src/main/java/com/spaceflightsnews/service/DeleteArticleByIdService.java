package com.spaceflightsnews.service;

import com.spaceflightsnews.repository.ArticleRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DeleteArticleByIdService {
    private final ArticleRepositoryImpl articleRepository;

    @Autowired
    public DeleteArticleByIdService(ArticleRepositoryImpl articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional
    public void execute(Long id){
        var exists = articleRepository.existsArticle(id);
        if(exists != 0)
            articleRepository.deleteArticleById(id);
    }
}

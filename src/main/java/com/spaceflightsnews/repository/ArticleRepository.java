package com.spaceflightsnews.repository;

import com.spaceflightsnews.model.Article;
import com.spaceflightsnews.model.Event;
import com.spaceflightsnews.model.Launch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;

public interface ArticleRepository {
    public List<Article> findAllArticleObjects();
    public Page<Article> findAllArticlePage(Pageable pageable);
    public Article findArticle(Long id);
    @Transactional
    public void deleteArticleById(Long id);
    @Transactional
    public void alterArticleById(Long id, Article body);
    @Transactional
    public void addArticle(Article body);
    @Transactional
    public void addMultipleArticles(List<Article> listaArticles);
    public int existsArticle(Long id);
    @Transactional
    public void addEventsToArticleId(Long articleId, List<Event> events);
    @Transactional
    public void addLaunchesToArticleId(Long articleId, List<Launch> launches);
    @Transactional
    public void addEvent(Event event);
    @Transactional
    public void addLaunch(Launch launch);
}

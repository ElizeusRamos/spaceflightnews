package com.spaceflightsnews.repository;

import com.spaceflightsnews.model.Article;
import com.spaceflightsnews.model.Event;
import com.spaceflightsnews.model.Launch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class ArticleRepositoryImpl implements ArticleRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String QUERY_SELECT_ALL_ARTICLES = "SELECT * FROM article";
    private final String QUERY_SELECT_ARTICLE_BY_ID = "SELECT * FROM article WHERE id=?";
    private final String QUERY_SELECT_EVENTS_BY_ARTICLE =
            "SELECT article_id, events_id as id, provider FROM " +
            "article A " +
            "join article_events AE " +
            "on A.id = AE.article_id join event E " +
            "on AE.events_id = E.id WHERE AE.article_id = ?";
    private final String QUERY_SELECT_LAUNCHES_BY_ARTICLE = "\n" +
            "select l.id as id, l.provider from article a inner join article_launches al " +
            "on a.id = al.article_id inner join launch l on al.launches_id = l.id where a.id = ?";
    private final String QUERY_SELECT_COUNT_ARTICLES = "SELECT count(1) AS row_count FROM article";
    private final String QUERY_SELECT_PAGEABLE_ARTICLES = "SELECT * FROM article LIMIT ?,?";
    private final String QUERY_DELETE_LAUNCHES = "DELETE FROM article_launches WHERE article_id=?";
    private final String QUERY_DELETE_EVENTS = "DELETE FROM article_events WHERE article_id=?";
    private final String QUERY_DELETE_ARTICLE_BY_ID = "DELETE FROM article WHERE id=?";
    private final String QUERY_ALTER_ARTICLE = "UPDATE article SET title=?, url=?, image_url=?, " +
            "news_site=?, summary=?, published_at=?, updated_at=?, featured=? WHERE id=?";
    private final String QUERY_ADD_ARTICLE = "INSERT INTO article (title, url, image_url, news_site, summary, " +
            "published_at, updated_at, featured, id) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String QUERY_SELECT_EXISTS_EVENT_BY_ID = "SELECT COUNT(1) as count FROM event WHERE id = ?";
    private final String QUERY_SELECT_EXISTS_LAUNCH_BY_ID = "SELECT COUNT(1) as count FROM launch WHERE id = ?";
    private final String QUERY_ADD_EVENT = "INSERT INTO event (id, provider) values(?,?)";
    private final String QUERY_ADD_EVENT_ARTICLE = "INSERT INTO article_events (article_id, events_id) values (?,?)";
    private final String QUERY_ADD_LAUNCH_ARTICLE = "INSERT INTO article_launches (article_id, launches_id) values (?,?)";
    private final String QUERY_SELECT_EXISTS_ARTICLE_BY_ID = "SELECT COUNT(1) as count FROM article WHERE id = ?";
    private final String QUERY_ADD_LAUNCH = "INSERT INTO launch (id, provider) values (?, ?)";



    @Override
    public List<Article> findAllArticleObjects() {
        return jdbcTemplate.query(
                QUERY_SELECT_ALL_ARTICLES,
                BeanPropertyRowMapper.newInstance(Article.class));
    }

    private int getTotalCountArticles() {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                QUERY_SELECT_COUNT_ARTICLES,
                (rs, rowNum) -> rs.getInt("row_count")))
                .orElse(0);
    }

    private List<Article> getArticlesListFromPageable(Pageable pageable) {
        return jdbcTemplate.query(
                QUERY_SELECT_PAGEABLE_ARTICLES,
                BeanPropertyRowMapper.newInstance(Article.class),
                pageable.getOffset(), pageable.getPageSize()
        );
    }

    @Override
    public Page<Article> findAllArticlePage(Pageable pageable) {
        int total = getTotalCountArticles();
        List<Article> lista = getArticlesListFromPageable(pageable);
        for (var it:lista) {
            findEventsAndLaunchesByArticleId(it.getId(), it);
        }
        return new PageImpl<>(lista, pageable, total);
    }

    @Override
    public Article findArticle(Long id) {
        Article article = null;
        try{
            article = jdbcTemplate.queryForObject(
                    QUERY_SELECT_ARTICLE_BY_ID,
                    BeanPropertyRowMapper.newInstance(Article.class),
                    id);
            findEventsAndLaunchesByArticleId(id, article);
        }catch (Exception e){
            return null;
        }
        return article;
    }

    private void findEventsAndLaunchesByArticleId(Long id, Article article) {
        var events = jdbcTemplate.query(
                QUERY_SELECT_EVENTS_BY_ARTICLE, BeanPropertyRowMapper.newInstance(Event.class),
                id);

        var launches = jdbcTemplate.query(
                QUERY_SELECT_LAUNCHES_BY_ARTICLE, BeanPropertyRowMapper.newInstance(Launch.class),
                id
        );

        if(article != null) {
            article.setEvents(events);
            article.setLaunches(launches);
        }
    }

    @Override
    public void deleteArticleById(Long id) {
        jdbcTemplate.update(QUERY_DELETE_LAUNCHES, id);
        jdbcTemplate.update(QUERY_DELETE_EVENTS, id);
        jdbcTemplate.update(QUERY_DELETE_ARTICLE_BY_ID, id);
    }

    @Override
    public void alterArticleById(Long id, Article body) {
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(QUERY_ALTER_ARTICLE);
            preparedStatement.setString(1, body.getTitle());
            preparedStatement.setString(2, body.getUrl());
            preparedStatement.setString(3, body.getImageUrl());
            preparedStatement.setString(4, body.getNewsSite());
            preparedStatement.setString(5, body.getSummary());
            preparedStatement.setString(6, body
                    .getPublishedAt().toString()
                    .replace('T', ' ')
                    .replace('Z', ' ')
                    .trim());
            preparedStatement.setString(7, body
                    .getUpdatedAt().toString()
                    .replace('T', ' ')
                    .replace('Z', ' ')
                    .trim());
            preparedStatement.setBoolean(8, body.isFeatured());
            preparedStatement.setLong(9, id);
            return preparedStatement;
        });
    }

    @Override
    public void addArticle(Article body) {
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(QUERY_ADD_ARTICLE);
            preparedStatement.setString(1, body.getTitle());
            preparedStatement.setString(2, body.getUrl());
            preparedStatement.setString(3, body.getImageUrl());
            preparedStatement.setString(4, body.getNewsSite());
            preparedStatement.setString(5, body.getSummary());
            preparedStatement.setString(6, body
                    .getPublishedAt().toString()
                    .replace('T', ' ')
                    .replace('Z', ' ')
                    .trim());
            preparedStatement.setString(7, body
                    .getUpdatedAt().toString()
                    .replace('T', ' ')
                    .replace('Z', ' ')
                    .trim());
            preparedStatement.setBoolean(8, body.isFeatured());
            preparedStatement.setLong(9, body.getId());
            return preparedStatement;
        });

        addEventsAndLaunchesToArticle(body);
    }

    @Override
    public void addMultipleArticles(List<Article> listaArticles) {
        jdbcTemplate.batchUpdate(QUERY_ADD_ARTICLE,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) {
                    try {
                        preparedStatement.setString(1, listaArticles.get(i).getTitle());
                        preparedStatement.setString(2, listaArticles.get(i).getUrl());
                        preparedStatement.setString(3, listaArticles.get(i).getImageUrl());
                        preparedStatement.setString(4, listaArticles.get(i).getNewsSite());
                        preparedStatement.setString(5, listaArticles.get(i).getSummary());
                        preparedStatement.setString(6, listaArticles.get(i)
                                .getPublishedAt().toString()
                                .replace('T', ' ')
                                .replace('Z', ' ')
                                .trim());
                        preparedStatement.setString(7, listaArticles.get(i)
                                .getUpdatedAt().toString()
                                .replace('T', ' ')
                                .replace('Z', ' ')
                                .trim());
                        preparedStatement.setBoolean(8, listaArticles.get(i).isFeatured());
                        preparedStatement.setLong(9, listaArticles.get(i).getId());
                    } catch (SQLException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                @Override
                public int getBatchSize() {
                    return listaArticles.size();
                }
        });
        // insert dos events e launches
        addEventsAndLaunchesToArticles(listaArticles);
    }

    private void addEventsAndLaunchesToArticles(List<Article> listaArticles) {
        for (var ot: listaArticles) {
            addEventsAndLaunchesToArticle(ot);
        }
    }

    private void addEventsAndLaunchesToArticle(Article ot) {
        if(!ot.getEvents().isEmpty()) {
            for (var it : ot.getEvents()) {
                //verificação previa existencia do event, caso não exista cria
                addEvent(it);
            }
            addEventsToArticleId(ot.getId(), ot.getEvents());
        }
        if(!ot.getLaunches().isEmpty()) {
            for (var it : ot.getLaunches()) {
                //verificação previa existencia do launch, caso não exista cria
                addLaunch(it);
            }
            addLaunchesToArticleId(ot.getId(), ot.getLaunches());
        }
    }

    @Override
    public int existsArticle(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                QUERY_SELECT_EXISTS_ARTICLE_BY_ID,
                (rs, rowNum) -> rs.getInt("count"),
                id))
                .orElse(0);
    }

    @Override
    public void addEventsToArticleId(Long articleId, List<Event> events) {
        if(!events.isEmpty()) {
        jdbcTemplate.batchUpdate(QUERY_ADD_EVENT_ARTICLE,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) {
                    try {
                        preparedStatement.setLong(1, articleId);
                        preparedStatement.setString(2, events.get(i).getId());
                    } catch (SQLException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                @Override
                public int getBatchSize() {
                    return events.size();
                }
            });
        }
    }

    @Override
    public void addLaunchesToArticleId(Long articleId, List<Launch> launches) {
        if(!launches.isEmpty()) {
            jdbcTemplate.batchUpdate(QUERY_ADD_LAUNCH_ARTICLE,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) {
                        try {
                            preparedStatement.setLong(1, articleId);
                            preparedStatement.setString(2, launches.get(i).getId());
                        } catch (SQLException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                    @Override
                    public int getBatchSize() {
                        return launches.size();
                    }
            });
        }
    }

    @Override
    public void addEvent(Event event) {
        int inTable = Optional.ofNullable(jdbcTemplate.queryForObject(
            QUERY_SELECT_EXISTS_EVENT_BY_ID,
                (rs, rowNum) -> rs.getInt("count"),
                event.getId()
        )).orElse(0);

        if(inTable == 0)
            jdbcTemplate.update(QUERY_ADD_EVENT, event.getId(), event.getProvider());
    }

    @Override
    public void addLaunch(Launch launch) {
        int inTable = Optional.ofNullable(jdbcTemplate.queryForObject(
                QUERY_SELECT_EXISTS_LAUNCH_BY_ID,
                (rs, rowNum) -> rs.getInt("count"),
                launch.getId()
        )).orElse(0);

        if(inTable == 0)
            jdbcTemplate.update(QUERY_ADD_LAUNCH, launch.getId(), launch.getProvider());
    }

}

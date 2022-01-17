CREATE TABLE IF NOT EXISTS article(
    id INT NOT NULL PRIMARY KEY,
    title VARCHAR(255),
    url VARCHAR(255),
    image_url VARCHAR(255),
    news_site VARCHAR(255),
    summary BLOB,
    published_at DATETIME,
    updated_at DATETIME,
    featured BOOLEAN
);

CREATE TABLE IF NOT EXISTS launch (
    id varchar(255) NOT NULL PRIMARY KEY,
    provider varchar(255)
);

CREATE TABLE IF NOT EXISTS event (
    id varchar(255) NOT NULL PRIMARY KEY,
    provider varchar(255)
);

CREATE TABLE IF NOT EXISTS article_events (
    article_id INT NOT NULL,
    events_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_eventarticles_id FOREIGN KEY (article_id) REFERENCES article (id),
    CONSTRAINT fk_articleevents_id FOREIGN KEY (events_id) REFERENCES event (id)
);

CREATE TABLE IF NOT EXISTS article_launches (
    article_id INT NOT NULL,
    launches_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_launcharticles_id FOREIGN KEY (article_id) REFERENCES article (id),
    CONSTRAINT fk_articlelaunchs_id FOREIGN KEY (launches_id) REFERENCES launch (id)
);
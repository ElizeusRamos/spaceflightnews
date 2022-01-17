package com.spaceflightsnews.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Article {
    @Id
    private Long id;
    private String title;
    private String url;

    private String imageUrl;
    private String newsSite;
    private String summary;
    private OffsetDateTime publishedAt;
    private OffsetDateTime updatedAt;
    private boolean featured;

    @ManyToMany
    @JoinTable(name="article_events", joinColumns=
            {@JoinColumn(name="article_id")}, inverseJoinColumns=
            {@JoinColumn(name="events_id")})
    private List<Event> events;
    @ManyToMany
    @JoinTable(name="article_events", joinColumns=
            {@JoinColumn(name="article_id")}, inverseJoinColumns=
            {@JoinColumn(name="launches_id")})
    private List<Launch> launches;

    public Article() { }
}

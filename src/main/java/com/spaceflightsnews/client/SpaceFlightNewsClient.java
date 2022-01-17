package com.spaceflightsnews.client;

import com.spaceflightsnews.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SpaceFlightNewsClient {
    private final WebClient webClient;

    public SpaceFlightNewsClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://api.spaceflightnewsapi.net/v3/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<Article> findArticleByIdFromClient(Long id){
        var relativePath = String.format("articles/%d", id);
        log.info("BUSCANDO ARTICLE BY ID...");
        return this.webClient
                .method(HttpMethod.GET)
                .uri(relativePath)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Erro na consulta ao client::Metodo findArticleByIdFromClient")))
                .bodyToMono(Article.class);
    }

    public Flux<Article> findArticlesByPageable(int limitItemsPerPage){
        var relativePath = String.format("articles?_limit=%d", limitItemsPerPage);
        log.info("BUSCANDO ARTICLES LIST...");
        return this.webClient
                .method(HttpMethod.GET)
                .uri(relativePath)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Erro na consulta ao client::Metodo findArticlesByPageable")))
                .bodyToFlux(Article.class);
    }
}

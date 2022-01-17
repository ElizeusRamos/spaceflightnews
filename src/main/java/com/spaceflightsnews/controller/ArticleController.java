package com.spaceflightsnews.controller;

import com.spaceflightsnews.model.Article;
import com.spaceflightsnews.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/")
public class ArticleController {
	private final AddArticleService addArticleService;
	private final FindArticlesPageableService findArticlesPageableService;
	private final AlterArticleByIdService alterArticleByIdService;
	private final DeleteArticleByIdService deleteArticleByIdService;
	private final FindArticleByIdService findArticleByIdService;

	@Autowired
	public ArticleController(
			AddArticleService addArticleService,
			FindArticlesPageableService findArticlesPageableService,
			AlterArticleByIdService alterArticleByIdService,
			DeleteArticleByIdService deleteArticleByIdService,
			FindArticleByIdService findArticleByIdService) {

		this.addArticleService = addArticleService;
		this.findArticlesPageableService = findArticlesPageableService;
		this.alterArticleByIdService = alterArticleByIdService;
		this.deleteArticleByIdService = deleteArticleByIdService;
		this.findArticleByIdService = findArticleByIdService;
	}

	@GetMapping
	public ResponseEntity<String> hello(){
		return new ResponseEntity<>("Back-end Challenge 2021 - Space Flight News", HttpStatus.OK);
	}

	@GetMapping("articles/{id}")
	public ResponseEntity<Article> findArticleById(@PathVariable Long id){
		var obj = findArticleByIdService.execute(id);
		return obj != null? new ResponseEntity<>(obj, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("articles")
	public ResponseEntity<Page<Article>> findAllArticlesPage(Pageable pageable){
		var result = findArticlesPageableService.execute(pageable);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@DeleteMapping("articles/{id}")
	public ResponseEntity<Void> deleteArticleById(@PathVariable Long id){
		deleteArticleByIdService.execute(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "articles/{id}",
			method = RequestMethod.PUT)
	public ResponseEntity<Void> alterArticleById(@PathVariable Long id, @RequestBody Article body){
		alterArticleByIdService.execute(id, body);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "articles",
			method = RequestMethod.POST)
	public ResponseEntity<Void> alterArticleById(@RequestBody Article body){
		addArticleService.execute(body);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}

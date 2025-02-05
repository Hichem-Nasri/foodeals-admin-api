package net.foodeals.contentManagement.infrastructure.controllers;

import net.foodeals.contentManagement.application.Dto.response.ArticleDto;
import net.foodeals.contentManagement.application.Dto.upload.CreateArticleDto;
import net.foodeals.contentManagement.application.Dto.upload.UpdateArticleDto;
import net.foodeals.contentManagement.application.services.ArticleService;
import net.foodeals.contentManagement.domain.entities.Article;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller()
public class ArticlesController {

    private final ArticleService articleService;
    private final ModelMapper modelMapper;

    public ArticlesController(ArticleService articleService, ModelMapper modelMapper) {
        this.articleService = articleService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @GetMapping("/Articles")
    public ResponseEntity<List<ArticleDto>> getAllArticles() {
        List<Article> articles = this.articleService.getAllArticles();
        List<ArticleDto> articlesDto = articles.stream().map(article -> this.modelMapper.map(article, ArticleDto.class)).toList();
        return new ResponseEntity<List<ArticleDto>>(articlesDto, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/Article/{uuid}")
    public ResponseEntity<ArticleDto> getArticleByUuid(@PathVariable("uuid") String uuid) {
        Article article = this.articleService.getArticleByUuid(uuid);
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article Not Found");
        }

        ArticleDto articleDto = this.modelMapper.map(article, ArticleDto.class);
        return new ResponseEntity<ArticleDto>(articleDto, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/Articles")
    public ResponseEntity<ArticleDto> createAnArticle(@RequestBody CreateArticleDto createArticleDto) {
        Article article = this.articleService.createAnArticle(createArticleDto);
        ArticleDto articleDto = this.modelMapper.map(article, ArticleDto.class);
        return new ResponseEntity<ArticleDto>(articleDto, HttpStatus.OK);
    }

    @Transactional
    @PutMapping("Article/{uuid}")
    public ResponseEntity<ArticleDto> updateAnArticle(@PathVariable("uuid") String uuid, @RequestBody UpdateArticleDto updateArticleDto) {
        Article article = this.articleService.updateAnArticleByUuid(uuid, updateArticleDto);
        ArticleDto articleDto = this.modelMapper.map(article, ArticleDto.class);
        return new ResponseEntity<ArticleDto>(articleDto, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("Article/{uuid}")
    public ResponseEntity<String> deleteAnArticle(@PathVariable("uuid") String uuid) {
        this.articleService.deleteAnArticleByUuid(uuid);
        return new ResponseEntity<String>("Article has been deleted", HttpStatus.OK);
    }
}
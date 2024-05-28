package com.princz_mia.viaubv18_coffee_shop.news;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @GetMapping()
    public ResponseEntity<List<News>> getAllNews() {
        List<News> news = newsService.getAllNews();
        return ResponseEntity.ok(news);
    }

    @GetMapping("/pagination")
    public ResponseEntity<NewsPageResponse> getPageOfNews(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "8", required = false) int pageSize
    ) {
        NewsPageResponse newsPage = newsService.getPageOfNews(pageNumber, pageSize);
        return ResponseEntity.ok(newsPage);
    }

    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<NewsPageResponse> getPageOfNewsBySearchTerm(
            @PathVariable(value = "searchTerm") @NotEmpty(message = "Search Term must not be null or empty") String searchTerm,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "8", required = false) int pageSize
    ) {
        NewsPageResponse newsPage = newsService.getPageOfNewsBySearchTerm(searchTerm, pageNumber, pageSize);
        return ResponseEntity.ok(newsPage);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable(value = "id") @NotNull(message = "News Id must not be null") Long id) {
        News news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/getByName/{title}")
    public ResponseEntity<News> getNewsByTitle(@PathVariable(value = "title") @NotEmpty(message = "Title must not be null or empty") String title) {
        News news = newsService.getNewsByTitle(title);
        return ResponseEntity.ok(news);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<News> createNews(@RequestBody @Valid NewsRequest productRequest) {
        News news = newsService.createNews(productRequest);
        return ResponseEntity.ok(news);
    }

    @PostMapping(path = "/update")
    public ResponseEntity<News> updateNews(@RequestBody @Valid NewsRequest productRequest) {
        News news = newsService.updateNews(productRequest);
        return ResponseEntity.ok(news);
    }

    @PostMapping(path = "/deleteById/{id}")
    public ResponseEntity<?> removeById(@PathVariable(value = "id") @NotNull(message = "News Id must not be null") Long id) {
        newsService.removeById(id);
        return ResponseEntity.ok(true);
    }
}

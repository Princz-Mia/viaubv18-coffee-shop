package com.princz_mia.viaubv18_coffee_shop.news;

import jakarta.validation.Valid;
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
            @PathVariable(value = "searchTerm") String searchTerm,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "8", required = false) int pageSize
    ) {
        NewsPageResponse newsPage = newsService.getPageOfNewsBySearchTerm(searchTerm, pageNumber, pageSize);
        return ResponseEntity.ok(newsPage);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable(value = "id") Long id) {
        News news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<News> getNewsByTitle(@PathVariable(value = "name") String name) {
        News news = newsService.getNewsByTitle(name);
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
    public ResponseEntity<?> removeById(@PathVariable(value = "id") Long id) {
        newsService.removeById(id);
        return ResponseEntity.ok(true);
    }
}

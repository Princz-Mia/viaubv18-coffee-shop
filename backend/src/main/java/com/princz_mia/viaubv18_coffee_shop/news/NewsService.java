package com.princz_mia.viaubv18_coffee_shop.news;

import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }


    public NewsPageResponse getPageOfNews(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> pageOfNews = newsRepository.findAll(pageable);
        List<News> newsList = pageOfNews.getContent();

        return NewsPageResponse.builder()
                .content(newsList)
                .pageNumber(pageOfNews.getNumber())
                .pageSize(pageOfNews.getSize())
                .totalElements(pageOfNews.getTotalElements())
                .totalPages(pageOfNews.getTotalPages())
                .isLast(pageOfNews.isLast())
                .build();
    }

    public NewsPageResponse getPageOfNewsBySearchTerm(String searchTerm, int pageNumber, int pageSize) {
        if (searchTerm.isEmpty()) {
            return getPageOfNews(pageNumber, pageSize);
        }

        String formattedSearchTerm = searchTerm.toLowerCase();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> pageOfNews = newsRepository.findAllByTitleContainsIgnoreCase(formattedSearchTerm, pageable);
        List<News> newsList = pageOfNews.getContent();

        if (newsList.isEmpty()) {
            throw new AppException("There is no product currently available for your needs.", HttpStatus.NOT_FOUND);
        }

        return NewsPageResponse.builder()
                .content(newsList)
                .pageNumber(pageOfNews.getNumber())
                .pageSize(pageOfNews.getSize())
                .totalElements(pageOfNews.getTotalElements())
                .totalPages(pageOfNews.getTotalPages())
                .isLast(pageOfNews.isLast())
                .build();
    }

    public News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new AppException("News is not found", HttpStatus.NOT_FOUND));
    }

    public News getNewsByTitle(String title) {
        return newsRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new AppException("News is not found", HttpStatus.NOT_FOUND));
    }

    public News createNews(NewsRequest newsRequest) {
        if (newsRequest.getId() != null)
            throw new AppException("New News should not have an Id", HttpStatus.BAD_REQUEST);

        var optionalNews= newsRepository.findByTitleIgnoreCase(newsRequest.getTitle());
        if (optionalNews.isPresent())
            throw new AppException("News is already exists with matching name", HttpStatus.BAD_REQUEST);

        News news = News.builder()
                .title(newsRequest.getTitle())
                .content(newsRequest.getContent())
                .newsImage(newsRequest.getNewsImage())
                .build();

        return newsRepository.save(news);
    }

    public News updateNews(NewsRequest newsRequest) {
        if (newsRequest.getId() == null)
            throw new AppException("News Id field is missing value", HttpStatus.BAD_REQUEST);

        var news = newsRepository.findById(newsRequest.getId())
                .orElseThrow(() -> new AppException("News is not found", HttpStatus.NOT_FOUND));

        news.setTitle(newsRequest.getTitle());
        news.setContent(newsRequest.getContent());
        news.setNewsImage(newsRequest.getNewsImage());

        return newsRepository.save(news);
    }

    public void removeById(Long id) {
        if (id == null)
            throw new AppException("Invalid News Id", HttpStatus.BAD_REQUEST);

        var news = newsRepository.findById(id)
                .orElseThrow(() -> new AppException("Product is not found", HttpStatus.NOT_FOUND));

        newsRepository.delete(news);
    }
}

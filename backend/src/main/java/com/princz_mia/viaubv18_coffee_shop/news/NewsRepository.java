package com.princz_mia.viaubv18_coffee_shop.news;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Optional<News> findByTitleIgnoreCase(String title);

    Page<News> findAllByTitleContainsIgnoreCase(String formattedSearchTerm, Pageable pageable);
}
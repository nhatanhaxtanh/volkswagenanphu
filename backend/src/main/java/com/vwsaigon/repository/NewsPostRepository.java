package com.vwsaigon.repository;

import com.vwsaigon.entity.NewsPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NewsPostRepository extends JpaRepository<NewsPost, Long> {
    List<NewsPost> findByPublishedTrueOrderByCreatedAtDesc();
    Optional<NewsPost> findBySlug(String slug);
}

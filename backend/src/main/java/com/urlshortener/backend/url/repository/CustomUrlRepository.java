package com.urlshortener.backend.url.repository;

import com.urlshortener.backend.url.entity.CustomUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomUrlRepository extends JpaRepository<CustomUrl, BigInteger> {

    List<CustomUrl> findByCustomCode(String customCode);

    Optional<CustomUrl> findByShortCode(String shortCode);

    int deleteByShortCode(String shortCode);

}

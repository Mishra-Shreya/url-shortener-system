package com.urlshortener.backend.url.repository;

import com.urlshortener.backend.url.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, BigInteger> {

    Optional<Url> findByShortCode(String shortCode);

    Optional<Url> findActiveByShortCode(String shortCode, LocalDateTime expiryDate);

    List<Url> findActiveByUserId(String userId, LocalDateTime expiryDate);

    List<Url> findByUserId(String userId);

    @Modifying
    @Query(name = "Url.updateStatusByShortCode")
    int updateStatusByShortCode(String shortCode, String status);

    int deleteByShortCode(String shortCode);

    @Query("""
        SELECT u
        FROM Url u
        JOIN u.customUrls cu
        WHERE cu.customCode = :customCode
          AND u.status = 'A'
    """)
    Optional<Url> findActiveUrlByCustomCode(String customCode);

    @Query("""
        SELECT u
        FROM Url u
        JOIN u.customUrls cu
        WHERE cu.customCode = :customCode
    """)
    List<Url> findUrlByCustomCode(String customCode);
}

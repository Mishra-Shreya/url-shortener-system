package com.urlshortener.backend.url.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "URL",
        schema = "USS",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_short_status",
                        columnNames = {"short_code", "status"}
                ),
                @UniqueConstraint(
                        name = "uk_url_short_code",
                        columnNames = {"short_code"}
                )
        }
)
@NamedQueries({
        @NamedQuery(
                name = "Url.findActiveByUserId",
                query = "SELECT e FROM Url e WHERE e.userId = :userId AND e.expiryDate >= :expiryDate AND e.status = 'A'"
        ),
        @NamedQuery(
                name = "Url.findActiveByShortCode",
                query = "SELECT e FROM Url e WHERE e.shortCode = :shortCode AND e.expiryDate >= :expiryDate AND e.status = 'A'"
        ),
        @NamedQuery(
                name = "Url.updateStatusByShortCode",
                query = "UPDATE Url e SET e.status = :status WHERE e.shortCode = :shortCode"
        )
})
@Getter
@Setter
@NoArgsConstructor
public class Url {

    @Id
    private BigInteger id;

    @NonNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NonNull
    @Column(name = "original_url", columnDefinition = "TEXT", nullable = false)
    private String originalUrl;

    @NonNull
    @Column(name = "short_code", unique = true, nullable = false)
    private String shortCode;

    @NonNull
    @Column(name = "status", nullable = false)
    private String status;

    @NonNull
    @Column(name = "click_count", nullable = false)
    private Long clickCount;

    @NonNull
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NonNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "url")
    private List<CustomUrl> customUrls = new ArrayList<>();

}

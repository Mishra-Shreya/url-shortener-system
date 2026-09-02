package com.urlshortener.backend.url.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(
        name = "CUSTOM_URL",
        schema = "USS",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_custom_url_short_custom",
                        columnNames = {"short_code", "custom_code"}
                )
            }
    )
@Getter
@Setter
@NoArgsConstructor
public class CustomUrl {

    @Id
    private BigInteger id;

    @NonNull
    @Column(name = "short_code", unique = true, nullable = false)
    private String shortCode;

    @NonNull
    @Column(name = "custom_code", nullable = false)
    private String customCode;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NonNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "short_code",
            referencedColumnName = "short_code",
            insertable = false,
            updatable = false
    )
    private Url url;

}

package com.telemedicine.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false)
    @JsonBackReference
    private Consultation consultation;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "author_role", nullable = false)
    private String authorRole;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @JsonProperty("consultationId")
    public Long getConsultationId() {
        return consultation != null ? consultation.getId() : null;
    }

    @PrePersist
    protected void onCreate() {
        if(timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if(authorRole != null){
            authorRole = author.toUpperCase();
        }
    }

}

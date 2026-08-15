package com.example.likelionhackathon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_medications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserMedication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이 약을 복용하는 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 팀원이 만든 한국 약 데이터와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "korean_medication_id", nullable = false)
    private KoreanMedication koreanMedication;
}
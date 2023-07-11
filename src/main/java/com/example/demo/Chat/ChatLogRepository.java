package com.example.demo.Chat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
    // 추가적인 쿼리 메소드가 필요한 경우 정의
}

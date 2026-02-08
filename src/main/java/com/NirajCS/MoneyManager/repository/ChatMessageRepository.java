package com.NirajCS.MoneyManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByUserOrderByTimestampDesc(String user);

    List<ChatMessage> findBySessionId(String sessionId);

    List<ChatMessage> findByUserAndSessionId(String user, String sessionId);
}
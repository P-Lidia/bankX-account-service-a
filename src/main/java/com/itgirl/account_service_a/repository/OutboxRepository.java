package com.itgirl.account_service_a.repository;

import com.itgirl.account_service_a.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {}
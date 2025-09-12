package com.itgirl.account_service_a.kafka;

import com.itgirl.account_service_a.dto.ReserveRequestDTO;
import com.itgirl.account_service_a.dto.CommitRequestDTO;
import com.itgirl.account_service_a.dto.ReleaseRequestDTO;
import com.itgirl.account_service_a.service.InternalAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaAccountListener {

    private final InternalAccountService accountService;

    @Value("${topics.reserve}")
    private String reserveTopic;

    @Value("${topics.commit}")
    private String commitTopic;

    @Value("${topics.release}")
    private String releaseTopic;

    @KafkaListener(topics = "${topics.reserve}", groupId = "account-service-group")
    public void listenReserve(ReserveRequestDTO reserveRequestDTO) {
        log.info("Got Reserve message: {}", reserveRequestDTO);
        try {
            UUID accountId = reserveRequestDTO.getAccountId();
            accountService.reserveFunds(accountId, reserveRequestDTO.getAmount());
            log.info("Funds reserved for accountId={}", accountId);
        } catch (Exception e) {
            log.error("Error reserving funds: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${topics.commit}", groupId = "account-service-group")
    public void listenCommit(CommitRequestDTO commitRequestDTO) {
        log.info("Got Commit message: {}", commitRequestDTO);
        try {
            UUID accountId = commitRequestDTO.getAccountId();
            accountService.commitFunds(accountId, commitRequestDTO.getAmount(), commitRequestDTO.getTransferId());
            log.info("Funds debited for accountId={}", accountId);
        } catch (Exception e) {
            log.error("Error committing funds: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${topics.release}", groupId = "account-service-group")
    public void listenRelease(ReleaseRequestDTO releaseRequestDTO) {
        log.info("Got Release message: {}", releaseRequestDTO);
        try {
            UUID accountId = releaseRequestDTO.getAccountId();
            accountService.releaseFunds(accountId, releaseRequestDTO.getAmount(), releaseRequestDTO.getTransferId());
            log.info("Funds released for accountId={}", accountId);
        } catch (Exception e) {
            log.error("Error releasing funds: {}", e.getMessage(), e);
        }
    }
}

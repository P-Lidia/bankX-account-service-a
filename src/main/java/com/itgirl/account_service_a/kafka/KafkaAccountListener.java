package com.itgirl.account_service_a.kafka;

import com.itgirl.account_service_a.config.KafkaTopicsProperties;
import com.itgirl.account_service_a.dto.CommitRequestDTO;
import com.itgirl.account_service_a.dto.ReleaseRequestDTO;
import com.itgirl.account_service_a.dto.ReserveRequestDTO;
import com.itgirl.account_service_a.service.InternalAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaAccountListener {

    private final InternalAccountService accountService;
    private final KafkaTopicsProperties topics;

    @KafkaListener(topics = "#{__listener.topics.reserve}", groupId = "account-service-group")
    public void listenReserve(ReserveRequestDTO reserveRequestDTO) {
        log.info("Got Reserve message: {}", reserveRequestDTO);

        try {
            UUID accountId = reserveRequestDTO.getId();
            accountService.reserveFunds(accountId, reserveRequestDTO.getAmount());
            log.info("Funds reserved for accountId={}", accountId);
        } catch (Exception e) {
            log.error("An error occurred while reserving funds: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "#{__listener.topics.commit}", groupId = "account-service-group")
    public void listenCommit(CommitRequestDTO commitRequestDTO) {
        log.info("Got Commit message: {}", commitRequestDTO);

        try {
            UUID accountId = commitRequestDTO.getId();
            accountService.commitFunds(accountId, commitRequestDTO.getAmount(), commitRequestDTO.getTransferId());
            log.info("Funds debited for accountId={}", accountId);
        } catch (Exception e) {
            log.error("An error occurred while committing funds: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "#{__listener.topics.release}", groupId = "account-service-group")
    public void listenRelease(ReleaseRequestDTO releaseRequestDTO) {
        log.info("Got Release message: {}", releaseRequestDTO);

        try {
            UUID accountId = releaseRequestDTO.getId();
            accountService.releaseFunds(accountId, releaseRequestDTO.getAmount(), releaseRequestDTO.getTransferId());
            log.info("Funds returned for accountId={}", accountId);
        } catch (Exception e) {
            log.error("An error occurred while releasing funds: {}", e.getMessage(), e);
        }
    }
}
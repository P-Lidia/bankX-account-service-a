package com.itgirl.account_service_a.kafka;

import com.itgirl.account_service_a.config.KafkaTopicsProperties;
import com.itgirl.account_service_a.dto.ReserveRequestDTO;
import com.itgirl.account_service_a.dto.CommitRequestDTO;
import com.itgirl.account_service_a.dto.ReleaseRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaAccountProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties topics;

    public void sendReserve(ReserveRequestDTO reserveRequestDTO) {
        kafkaTemplate.send(
                topics.getReserve(),
                reserveRequestDTO.getTransferId().toString(),
                reserveRequestDTO
        );
    }

    public void sendCommit(CommitRequestDTO commitRequestDTO) {
        kafkaTemplate.send(
                topics.getCommit(),
                commitRequestDTO.getTransferId().toString(),
                commitRequestDTO
        );
    }

    public void sendRelease(ReleaseRequestDTO releaseRequestDTO) {
        kafkaTemplate.send(
                topics.getRelease(),
                releaseRequestDTO.getTransferId().toString(),
                releaseRequestDTO
        );
    }
}
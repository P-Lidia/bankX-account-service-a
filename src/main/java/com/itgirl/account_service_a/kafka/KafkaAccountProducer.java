package com.itgirl.account_service_a.kafka;

import com.itgirl.account_service_a.dto.ReserveRequestDTO;
import com.itgirl.account_service_a.dto.CommitRequestDTO;
import com.itgirl.account_service_a.dto.ReleaseRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component

public class KafkaAccountProducer {
    private final String reserveTopic;
    private final String commitTopic;
    private final String releaseTopic;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaAccountProducer(
            @Value("${kafka.topics.reserve}") String reserveTopic,
            @Value("${kafka.topics.commit}") String commitTopic,
            @Value("${kafka.topics.release}") String releaseTopic,
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.reserveTopic = reserveTopic;
        this.commitTopic = commitTopic;
        this.releaseTopic = releaseTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReserve(ReserveRequestDTO reserveRequestDTO) {
        kafkaTemplate.send(reserveTopic, reserveRequestDTO.getTransferId().toString(), reserveRequestDTO);
    }

    public void sendCommit(CommitRequestDTO commitRequestDTO) {
        kafkaTemplate.send(commitTopic, commitRequestDTO.getTransferId().toString(), commitRequestDTO);
    }

    public void sendRelease(ReleaseRequestDTO releaseRequestDTO) {
        kafkaTemplate.send(releaseTopic, releaseRequestDTO.getTransferId().toString(), releaseRequestDTO);
    }
}
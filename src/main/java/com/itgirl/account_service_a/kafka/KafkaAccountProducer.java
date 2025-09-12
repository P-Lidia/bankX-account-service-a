package com.itgirl.account_service_a.kafka;

import com.itgirl.account_service_a.dto.ReserveRequestDTO;
import com.itgirl.account_service_a.dto.CommitRequestDTO;
import com.itgirl.account_service_a.dto.ReleaseRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaAccountProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${topics.reserve}")
    private String reserveTopic;

    @Value("${topics.commit}")
    private String commitTopic;

    @Value("${topics.release}")
    private String releaseTopic;

    public void sendReserve(ReserveRequestDTO reserveRequestDTO) {
        UUID transferId = reserveRequestDTO.getTransferId() != null
                ? reserveRequestDTO.getTransferId()
                : UUID.randomUUID();

        reserveRequestDTO.setTransferId(transferId);

        kafkaTemplate.send(
                reserveTopic,
                transferId.toString(),
                reserveRequestDTO
        );
    }

    public void sendCommit(CommitRequestDTO commitRequestDTO) {
        kafkaTemplate.send(
                commitTopic,
                commitRequestDTO.getTransferId().toString(),
                commitRequestDTO
        );
    }

    public void sendRelease(ReleaseRequestDTO releaseRequestDTO) {
        kafkaTemplate.send(
                releaseTopic,
                releaseRequestDTO.getTransferId().toString(),
                releaseRequestDTO
        );
    }
}

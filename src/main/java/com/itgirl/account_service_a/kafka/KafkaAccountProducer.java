package com.itgirl.account_service_a.kafka;

import com.itgirl.account_service_a.config.KafkaTopicsProperties;
import com.itgirl.account_service_a.dto.ReserveRequestDTO;
import com.itgirl.account_service_a.dto.CommitRequestDTO;
import com.itgirl.account_service_a.dto.ReleaseRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaAccountProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties topics;

    public void sendReserve(ReserveRequestDTO reserveRequestDTO) {
        // Если transferId не передан — генерируем новый
        UUID transferId = reserveRequestDTO.getTransferId() != null
                ? reserveRequestDTO.getTransferId()
                : UUID.randomUUID();

        // Обновляем DTO с новым transferId
        reserveRequestDTO.setTransferId(transferId);

        kafkaTemplate.send(
                topics.getReserve(),
                transferId.toString(),   // ключ — transferId
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

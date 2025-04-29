package com.utu.user_service.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utu.user_service.Configs.GrpcClientt;
import com.utu.user_service.Models.OutBoxEvent;
import com.utu.user_service.Repositories.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutBoxRepository outboxRepository;
    private final GrpcClientt grpcClient; // custom gRPC sender

    @Scheduled(fixedDelay = 5000)
    public void processOutboxEvents() {
        List<OutBoxEvent> pendingEvents = outboxRepository.findByStatus("PENDING");

        for (OutBoxEvent event : pendingEvents) {
            try {
                Map<String, Object> payload = new ObjectMapper()
                        .readValue(event.getPayload(), new TypeReference<>() {});

                grpcClient.sendFileToNotesService(payload); // actual gRPC call

                event.setStatus("SENT");
            } catch (Exception ex) {
                event.setStatus("FAILED"); // optionally use retry counter
            }
            outboxRepository.save(event);
        }
    }
}

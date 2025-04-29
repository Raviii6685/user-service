 package  com.utu.user_service.Models;

 import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;
 import org.springframework.data.annotation.Id;
 import org.springframework.data.mongodb.core.mapping.Document;

 import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("outbox_events")
public class OutBoxEvent {
    @Id
    private String id;

    private String eventType;         // e.g., FILE_UPLOAD
    private String payload;           // JSON string of actual data
    private String status;            // PENDING, SENT, FAILED
    private LocalDateTime createdAt;
}
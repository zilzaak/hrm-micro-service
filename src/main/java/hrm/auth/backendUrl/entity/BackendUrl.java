package hrm.auth.backendUrl.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
@Entity
@Table(name="backend_url",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name","http_method"})
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BackendUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name="http_method")
    private String httpMethod;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;
}

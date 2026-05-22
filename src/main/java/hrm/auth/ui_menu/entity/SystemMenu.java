package hrm.auth.ui_menu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="system_menu")
@Builder
public class SystemMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // foreign key column
    @JsonBackReference
    private SystemMenu parent;

    @OneToMany(mappedBy = "parent",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<SystemMenu> details = new ArrayList<>();

    private String frontendUrl;

    private String backendUrlIds;

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime updated;
}

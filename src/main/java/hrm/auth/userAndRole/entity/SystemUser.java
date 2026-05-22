package hrm.auth.userAndRole.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="system_user")
@Builder
public class SystemUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column
    private String phone;
    private String email;
    @Column
    private String address;
    @Column
    private String displayName;
    private Boolean enabled;

    @OneToMany(orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},  // Don't use ALL
            mappedBy = "systemUser",
            fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<UserRoles> roleList;

}

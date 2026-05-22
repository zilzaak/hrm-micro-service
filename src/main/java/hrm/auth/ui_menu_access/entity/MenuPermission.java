package hrm.auth.ui_menu_access.entity;

import hrm.auth.ui_menu.entity.SystemMenu;
import hrm.auth.userAndRole.entity.SystemRole;
import hrm.auth.userAndRole.entity.SystemUser;
import hrm.auth.userAndRole.entity.UserRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name="permitted_menu",
uniqueConstraints = {
@UniqueConstraint(columnNames = {"menu_id","user_id","role_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuPermission {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_id")
    private SystemMenu menu;
    
    private String skipBackendUrlIds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private SystemUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id")
    private SystemRole role;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

}

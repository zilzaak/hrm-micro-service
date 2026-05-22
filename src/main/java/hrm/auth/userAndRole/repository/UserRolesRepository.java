package hrm.auth.userAndRole.repository;

import hrm.auth.userAndRole.entity.SystemRole;
import hrm.auth.userAndRole.entity.SystemUser;
import hrm.auth.userAndRole.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRolesRepository  extends JpaRepository<UserRoles,Long> {
    boolean existsByRole(SystemRole admin);
    UserRoles findByRole(SystemRole admin);
    UserRoles findBySystemUserAndRole(SystemUser systemUser,SystemRole admin);
    boolean existsBySystemUserAndRole(SystemUser systemUser,SystemRole admin);

}

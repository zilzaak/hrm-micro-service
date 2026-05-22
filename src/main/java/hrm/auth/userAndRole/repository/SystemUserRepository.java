package hrm.auth.userAndRole.repository;

import hrm.auth.userAndRole.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemUserRepository extends JpaRepository<SystemUser,Long> {

    boolean existsByUsername(String admin);

    SystemUser findByUsername(String username);
}

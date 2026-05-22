package hrm.auth.userAndRole.repository;

import hrm.auth.userAndRole.entity.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemRoleRepository extends JpaRepository<SystemRole,Long> {

    boolean existsByName(String s);

    SystemRole findByName(String admin);

    SystemRole findByNameIgnoreCase(String admin);
}

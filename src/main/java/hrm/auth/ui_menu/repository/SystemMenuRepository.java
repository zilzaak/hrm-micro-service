package hrm.auth.ui_menu.repository;

import hrm.auth.ui_menu.entity.SystemMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SystemMenuRepository extends JpaRepository<SystemMenu,Long> , JpaSpecificationExecutor {
    Optional<SystemMenu> findByName(String settings);
}
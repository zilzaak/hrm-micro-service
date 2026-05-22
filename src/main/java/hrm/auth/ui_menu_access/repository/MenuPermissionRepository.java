package hrm.auth.ui_menu_access.repository;

import hrm.auth.ui_menu_access.entity.MenuPermission;
import hrm.auth.userAndRole.entity.SystemRole;
import hrm.auth.userAndRole.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MenuPermissionRepository extends JpaRepository<MenuPermission,Long> {
   @Query("select count(x.id) from MenuPermission x " +
           " left join x.user u " +
           " left join x.role r " +
           " where  ( :userid is null or  u.id=:userid )  " +
           "  and ( :roleid is null or r.id=:roleid )  " +
           "  and x.id in :parentmenus ")
    int permissionExistByParentMenu(@Param("userid") Long userId,
                                    @Param("roleid") Long roleId,
                                    @Param("parentmenus") List<Long> parentMenus);

    @Query("select role.name as authority , user.username as username ,  " +
            " menu.backendUrlIds as backendUrlIds  from MenuPermission mp " +
            " inner join mp.menu menu " +
            " left join mp.role role  " +
            " left join mp.user user  " +
            " where menu.backendUrlIds IS NOT NULL and  user=:u or role in :roleSet " +
            " group by role.name , user.username , menu.backendUrlIds   ")
    List<Map<String, Object>> getUsersPermittedMenu(@Param("u") SystemUser u,
                                                    @Param("roleSet") Set<SystemRole> roleSet);

    @Query(" select role.name as authority , user.username as username ,  " +
            " menu.backendUrlIds as backendUrlIds  from MenuPermission mp " +
            " inner join mp.menu menu " +
            " left join mp.role role  " +
            " left join mp.user user  " +
            " where menu.backendUrlIds IS NOT NULL and  role in :roleSet " +
            " group by role.name , user.username , menu.backendUrlIds ")
    List<Map<String, Object>> getUsersPermittedMenu(@Param("roleSet") Set<SystemRole> roleSet);

    @Query(" select role.name as authority , user.username as username ,  " +
            " menu.backendUrlIds as backendUrlIds  from MenuPermission mp " +
            " inner join mp.menu menu " +
            " left join mp.role role  " +
            " left join mp.user user  " +
            " where menu.backendUrlIds IS NOT NULL and  user=:u " +
            " group by role.name , user.username , menu.backendUrlIds   ")
    List<Map<String, Object>> getUsersPermittedMenu(@Param("u") SystemUser u);



}

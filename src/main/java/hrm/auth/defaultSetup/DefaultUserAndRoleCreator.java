package hrm.auth.defaultSetup;

import hrm.auth.userAndRole.entity.SystemRole;
import hrm.auth.userAndRole.entity.SystemUser;
import hrm.auth.userAndRole.entity.UserRoles;
import hrm.auth.userAndRole.repository.SystemRoleRepository;
import hrm.auth.userAndRole.repository.SystemUserRepository;
import hrm.auth.userAndRole.repository.UserRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@RequiredArgsConstructor
public class DefaultUserAndRoleCreator {

    private final UserRolesRepository userRolesRepository;
    private final SystemUserRepository systemUserRepository;
    private final SystemRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void createDefaultUserAndRole() {
        createDefaultRoles();
        createAdminUser();
    }

    private void createDefaultRoles() {
        List<String> roleNames = List.of("ADMIN", "HR", "PAYROLL", "RECRUITMENT", "COMMON", "PERMIT_ALL");

        for (String roleName : roleNames) {
            if (!roleRepository.existsByName(roleName)) {
                roleRepository.save(SystemRole.builder().name(roleName).build());
            }
        }
    }

    private void createAdminUser() {
        if (systemUserRepository.count()<1) {
            createNewAdminUser();
        }
    }


    private void createNewAdminUser() {
        SystemUser adminUser = buildAdminUser();
        systemUserRepository.save(adminUser);
        List<UserRoles> userRoles = createAdminRoles(adminUser);
        userRolesRepository.saveAll(userRoles);
        adminUser.setRoleList(userRoles);
        systemUserRepository.save(adminUser);
    }

    private SystemUser buildAdminUser() {
        return SystemUser.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .email("orchidpust@gmail.com")
                .phone("01753181186")
                .displayName("parvez")
                .address("address")
                .enabled(true)
                .roleList(new ArrayList<>())
                .build();
    }

    private List<UserRoles> createAdminRoles(SystemUser adminUser) {
        SystemRole adminRole = getRole("ADMIN");
        return List.of(createUserRole(adminUser, adminRole));
    }

    private UserRoles createUserRole(SystemUser user, SystemRole role) {
        return UserRoles.builder()
                .role(role)
                .systemUser(user)
                .createBy("System")
                .build();
    }

    private SystemRole getRole(String roleName) {
        return roleRepository.findByName(roleName);
    }
}

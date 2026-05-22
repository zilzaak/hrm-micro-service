package hrm.auth.security.auth.service;

import hrm.auth.ui_menu_access.repository.MenuPermissionRepository;
import hrm.auth.userAndRole.entity.SystemRole;
import hrm.auth.userAndRole.entity.SystemUser;
import hrm.auth.userAndRole.repository.SystemRoleRepository;
import hrm.auth.userAndRole.repository.SystemUserRepository;
import hrm.auth.userAndRole.repository.UserRolesRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DynamicPermissionService {

    private final MenuPermissionRepository menuPermissionRepository;
    private final SystemUserRepository userRepository;
    private final UserRolesRepository roleRepository;
    private final SystemRoleRepository systemRoleRepository;

    DynamicPermissionService(MenuPermissionRepository menuPermissionRepository,
                             SystemUserRepository userRepository,
                             UserRolesRepository roleRepository,
                             SystemRoleRepository systemRoleRepository){
        this.menuPermissionRepository=menuPermissionRepository;
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.systemRoleRepository=systemRoleRepository;
    }

    public Map<String, String> getPermissions() { //all permitted backendUrlIds for users role or username

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User  springUser = null;
        SystemUser systemUser = null;
        Set<SystemRole> roleSet=new HashSet<>();
        SystemRole free = systemRoleRepository.findByName("PERMIT_ALL");
        if(auth!=null){
            springUser = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            systemUser=userRepository.findByUsername(springUser.getUsername());
            roleSet=systemUser.getRoleList().stream().map(r->r.getRole()).collect(Collectors.toSet());
        }
        roleSet.add(free);

        List<Map<String,Object>> permissions = new ArrayList<>();

        if(systemUser!=null && roleSet.size()>0){
            permissions = menuPermissionRepository.getUsersPermittedMenu(systemUser ,roleSet);
        }
        else if(systemUser==null && roleSet.size()>0){
            permissions = menuPermissionRepository.getUsersPermittedMenu(roleSet);
        }
        else if(systemUser!=null && roleSet.size()<1){
            permissions = menuPermissionRepository.getUsersPermittedMenu(systemUser);
        }

          Map<String, String> maps = new HashMap<>();

          for(Map<String,Object> obj :  permissions ){
              String roleKey = obj.get("authority")!=null?obj.get("authority").toString():null;
              String userKey = obj.get("username")!=null?obj.get("username").toString():null;
              String value= obj.get("backendUrlIds")!=null?obj.get("backendUrlIds").toString():null;
             if(maps.isEmpty()){
                 if(roleKey!=null){
                     maps.put(roleKey, value);
                 }
                 if(userKey!=null){
                     maps.put(userKey,value);
                 }
             }else{
                  if(roleKey!=null){
                      if(maps.containsKey(roleKey)){
                          maps.put(roleKey,maps.get(roleKey)+","+obj.get("backendUrlIds"));
                      }else{
                          maps.put(roleKey, (String) obj.get("backendUrlIds"));
                      }
                     }
                     if(userKey!=null){
                         if(maps.containsKey(userKey)){
                             maps.put(userKey,maps.get(userKey)+","+obj.get("backendUrlIds"));
                         }else{
                             maps.put(userKey,value);
                         }
                     }
                }
          }
        return  maps;
    }
}

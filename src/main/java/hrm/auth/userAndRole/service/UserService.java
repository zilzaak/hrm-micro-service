package hrm.auth.userAndRole.service;


import hrm.auth.common.dto.MsgResponse;
import hrm.auth.security.config.PassEncodeClss;
import hrm.auth.userAndRole.dto.SystemUserDTO;
import hrm.auth.userAndRole.dto.UserRolesDTO;
import hrm.auth.userAndRole.entity.SystemRole;
import hrm.auth.userAndRole.entity.SystemUser;
import hrm.auth.userAndRole.entity.UserRoles;
import hrm.auth.userAndRole.repository.SystemRoleRepository;
import hrm.auth.userAndRole.repository.SystemUserRepository;
import hrm.auth.userAndRole.repository.UserRolesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final SystemUserRepository systemUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final SystemRoleRepository systemRoleRepository;
    private final UserRolesRepository userRolesRepository;

    UserService(SystemUserRepository systemUserRepository,
                PasswordEncoder passwordEncoder,
                SystemRoleRepository systemRoleRepository,
                UserRolesRepository userRolesRepository){
        this.systemUserRepository=systemUserRepository;
        this.passwordEncoder=passwordEncoder;
        this.systemRoleRepository = systemRoleRepository;
        this.userRolesRepository=userRolesRepository;
    }
    public MsgResponse getList(Map<String, String> params) {
        List<SystemUser> user = systemUserRepository.findAll();
        return new MsgResponse(user,true);
    }

    public MsgResponse create(SystemUserDTO systemUserDTO) {
        SystemUser systemUser=new SystemUser();
        BeanUtils.copyProperties(systemUserDTO,systemUser);
        systemUser.setPassword(passwordEncoder.encode(systemUserDTO.getPassword()));
       try {
           systemUserRepository.save(systemUser);
           return new MsgResponse(systemUser,true);
       }catch (Exception e){
           return new MsgResponse(e.getMessage(),false);
       }
    }

    public MsgResponse update(SystemUserDTO systemUserDTO) {
        SystemUser systemUser=systemUserRepository.findById(systemUserDTO.getId()).get();
        BeanUtils.copyProperties(systemUserDTO,systemUser,"password","roleList");
        if(systemUserDTO.getPassword()!=null && !systemUserDTO.getPassword().equals(systemUser.getPassword())){
           systemUser.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        }
        userRolesRepository.deleteAll(systemUser.getRoleList());
       List<UserRoles> roles = new ArrayList<>();

       for(UserRolesDTO dto  : systemUserDTO.getRoleList()){
           roles.add(UserRoles.builder().systemUser(systemUser)
                    .role(SystemRole.builder().id(dto.getSystemRoleId())
                    .build()).build());
           }

       userRolesRepository.saveAll(roles);
       systemUser.setRoleList(roles);

        try {
            systemUserRepository.save(systemUser);
            return new MsgResponse(systemUser,true);
        }catch (Exception e){
            return new MsgResponse(e.getMessage(),false);
        }
    }

    public MsgResponse delete(Long id) {
        try{
            systemUserRepository.deleteById(id);
            return new MsgResponse("successfully deleted",true);
        } catch (Exception e) {
            return new MsgResponse(e.getMessage(),true);
        }
    }
}

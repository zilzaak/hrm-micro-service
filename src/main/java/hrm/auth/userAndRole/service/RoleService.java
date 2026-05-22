package hrm.auth.userAndRole.service;

import hrm.auth.common.dto.MsgResponse;
import hrm.auth.userAndRole.entity.SystemRole;
import hrm.auth.userAndRole.repository.SystemRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleService {

private final SystemRoleRepository systemRoleRepository;

RoleService(SystemRoleRepository systemRoleRepository){
    this.systemRoleRepository=systemRoleRepository;
}

    public MsgResponse getList(Map<String, String> params) {
    List<SystemRole> roles = systemRoleRepository.findAll();
        return new MsgResponse(roles,true);
    }

    public MsgResponse create(SystemRole systemRole) {
    try{
        systemRoleRepository.save(systemRole);
        return new MsgResponse("Successfully created",true);
    } catch (Exception e) {
        return new MsgResponse(e.getMessage(),false);
    }
    }

    public MsgResponse update(SystemRole systemRole) {
        try{
            systemRoleRepository.save(systemRole);
            return new MsgResponse("Successfully updated",true);
        } catch (Exception e) {
            return new MsgResponse(e.getMessage(),false);
        }
    }

    public MsgResponse delete(Long id) {
    try {
        systemRoleRepository.deleteById(id);
        return new MsgResponse("Successfully deleted",true);
    } catch (Exception e) {
        return new MsgResponse(e.getMessage(),false);
    }
    }
}

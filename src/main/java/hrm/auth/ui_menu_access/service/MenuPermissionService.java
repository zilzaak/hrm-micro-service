package hrm.auth.ui_menu_access.service;

import hrm.auth.common.dto.MsgResponse;
import hrm.auth.ui_menu.entity.SystemMenu;
import hrm.auth.ui_menu.service.SystemMenuService;
import hrm.auth.ui_menu_access.dto.MenuPermissionDTO;
import hrm.auth.ui_menu_access.entity.MenuPermission;
import hrm.auth.ui_menu_access.repository.MenuPermissionRepository;
import hrm.auth.userAndRole.entity.SystemRole;
import hrm.auth.userAndRole.entity.SystemUser;
import hrm.auth.userAndRole.entity.UserRoles;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuPermissionService {

    private final MenuPermissionRepository menuPermissionRepository;
    private final SystemMenuService systemMenuService;
    MenuPermissionService(MenuPermissionRepository menuPermissionRepository,
                          SystemMenuService systemMenuService){
        this.menuPermissionRepository=menuPermissionRepository;
        this.systemMenuService=systemMenuService;
    }

    Map<String, Object> validityCheck(MenuPermissionDTO dto){
        Map<String, Object> mp = new HashMap<>();
        mp.put("valid",true);
        if(dto.getMenuId()==null){
            mp.put("valid",false);
            mp.put("message","menu is required");
        }
        if(dto.getRoleId()==null && dto.getUserId()==null){
            mp.put("valid",false);
            mp.put("message","Either Role or user is required");
        }
        if(dto.getRoleId()!=null && dto.getUserId()!=null){
            mp.put("valid",false);
            mp.put("message","Either Role or user only one option need");
        }
        return  mp;

    }

    @Transactional
    public MsgResponse create(MenuPermissionDTO dto) {
        Map<String, Object> mp = this.validityCheck(dto);
        if(!(boolean) mp.get("valid")){
           return new MsgResponse(mp.get("message"),false);
        }
        MenuPermission menuPermission = new MenuPermission();
        menuPermission.setMenu(SystemMenu.builder().id(dto.getMenuId()).build());
        if(dto.getUserId()!=null){
            menuPermission.setUser(SystemUser.builder().id(dto.getUserId()).build());
        }
        if(dto.getRoleId()!=null){
            menuPermission.setRole(SystemRole.builder().id(dto.getRoleId()).build());
        }
        List<Long> parentMenus =systemMenuService.checkParentMenus(dto.getMenuId());
        if(menuPermissionRepository.permissionExistByParentMenu(dto.getUserId(),dto.getRoleId(),parentMenus)>0){
            return new MsgResponse("Redundant permission happening , already given permission with parent menu",false);
        }
        try{
            this.menuPermissionRepository.save(menuPermission);
            return new MsgResponse("Successfully created",true);

        } catch (RuntimeException e) {
            return new MsgResponse(e.getMessage(),false);
        }

    }

    public MsgResponse update(MenuPermissionDTO dto) {
        Map<String, Object> mp = this.validityCheck(dto);
        if(!(boolean) mp.get("valid")){
            return new MsgResponse(mp.get("message"),false);
        }
        MenuPermission menuPermission = menuPermissionRepository.findById(dto.getId()).orElse(null);
        if(menuPermission==null){
            return new MsgResponse("Resource not found",false);
        }
        menuPermission.setMenu(SystemMenu.builder().id(dto.getMenuId()).build());
        if(dto.getUserId()!=null){
            menuPermission.setUser(SystemUser.builder().id(dto.getUserId()).build());
            menuPermission.setRole(null);
        }
        if(dto.getRoleId()!=null){
            menuPermission.setRole(SystemRole.builder().id(dto.getRoleId()).build());
            menuPermission.setUser(null);
        }
        try{
            this.menuPermissionRepository.save(menuPermission);
            return new MsgResponse("Successfully updated",true);

        } catch (RuntimeException e) {
            return new MsgResponse(e.getMessage(),false);
        }

    }

    public MsgResponse getMenuPermission(Map<String, String> clientParams) {
        List<MenuPermission> list = menuPermissionRepository.findAll();
        return  new MsgResponse(list,true);
    }
}

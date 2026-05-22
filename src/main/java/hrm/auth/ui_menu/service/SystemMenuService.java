package hrm.auth.ui_menu.service;

import hrm.auth.common.dto.MsgResponse;
import hrm.auth.common.util.CommonUtil;
import hrm.auth.ui_menu.dto.SystemMenuDTO;
import hrm.auth.ui_menu.entity.SystemMenu;
import hrm.auth.ui_menu.repository.SystemMenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemMenuService {

    private final SystemMenuRepository systemMenuRepository;
    SystemMenuService(SystemMenuRepository systemMenuRepository){
        this.systemMenuRepository=systemMenuRepository;
    }

    Map<String,Object> checkValidation(SystemMenuDTO systemMenuDTO){

        Map<String,Object> validity = new HashMap<>();
        validity.put("valid",true);

        if(systemMenuDTO.getBackendUrlIds()!=null && !systemMenuDTO.getBackendUrlIds().isBlank()){
            List<Long> ids = CommonUtil.strListToLong(CommonUtil.
                    bulkStrToList(systemMenuDTO.getBackendUrlIds()));
            for(Long id : ids){
                if(!systemMenuRepository.existsById(id)){
                    validity.put("valid",false);
                    validity.put("message","Backend url id not found "+id);
                    return  validity;
                }
            }
        }

        if(systemMenuDTO.getParentId()!=null && !systemMenuRepository.existsById(systemMenuDTO.getParentId())){
            validity.put("valid",false);
            validity.put("message","Parent menu not found ");
            return  validity;
        }

        return validity;
    }

    public MsgResponse create(SystemMenuDTO systemMenuDTO) {
        SystemMenu systemMenu=SystemMenu.builder().name(systemMenuDTO.getName())
                .backendUrlIds(systemMenuDTO.getBackendUrlIds()).
                frontendUrl(systemMenuDTO.getFrontendUrl())
                .build();

        Map<String,Object> validity = this.checkValidation(systemMenuDTO);
        if(!(boolean)validity.get("valid")){
            return  new MsgResponse(validity.get("message"),false);
        }

        if(systemMenuDTO.getParentId()!=null){
            SystemMenu parent = systemMenuRepository.findById(systemMenuDTO.getParentId()).get();
            systemMenu.setParent(parent);
            parent.getDetails().add(systemMenu);
            systemMenuRepository.save(systemMenu);
            systemMenuRepository.save(parent);
        }else{
            systemMenuRepository.save(systemMenu);
        }
        return  new MsgResponse("Successfully created menu",true);
    }

    public MsgResponse update(SystemMenuDTO dto) {
        SystemMenu dbSystemMenu=systemMenuRepository.findById(dto.getId()).get();

        Map<String,Object> validity = this.checkValidation(dto);
        if(!(boolean)validity.get("valid")){
            return  new MsgResponse(validity.get("message"),false);
        }

        if(dto.getParentId()!=null && dbSystemMenu.getParent()!=null ){
            if(!dto.getParentId().equals(dbSystemMenu.getParent().getId())){
                SystemMenu oldParent = systemMenuRepository.findById(dbSystemMenu.getParent().getId()).get();
                SystemMenu newParent = systemMenuRepository.findById(dto.getParentId()).get();
                oldParent.getDetails().remove(dbSystemMenu);
                systemMenuRepository.save(oldParent);
                dbSystemMenu.setParent(newParent);
                dbSystemMenu.setBackendUrlIds(dto.getBackendUrlIds());
                dbSystemMenu.setFrontendUrl(dto.getFrontendUrl());
                systemMenuRepository.save(dbSystemMenu);
                newParent.getDetails().add(dbSystemMenu);
                systemMenuRepository.save(newParent);

            }
        }
        if(dto.getParentId()!=null && dbSystemMenu.getParent()==null){
            SystemMenu parent = systemMenuRepository.findById(dto.getParentId()).get();
            dbSystemMenu.setParent(parent);
            dbSystemMenu.setBackendUrlIds(dto.getBackendUrlIds());
            dbSystemMenu.setFrontendUrl(dto.getFrontendUrl());
            systemMenuRepository.save(dbSystemMenu);
            parent.getDetails().add(dbSystemMenu);
            systemMenuRepository.save(dbSystemMenu);
        }
        if(dto.getParentId()==null && dbSystemMenu.getParent()!=null){
            SystemMenu parent = systemMenuRepository.findById(dbSystemMenu.getParent().getId()).get();
            parent.getDetails().remove(dbSystemMenu);
            systemMenuRepository.save(parent);
            dbSystemMenu.setParent(null);
            dbSystemMenu.setBackendUrlIds(dto.getBackendUrlIds());
            dbSystemMenu.setFrontendUrl(dto.getFrontendUrl());
            systemMenuRepository.save(dbSystemMenu);
        }
        if(dto.getParentId()==null && dbSystemMenu.getParent()==null){
            dbSystemMenu.setBackendUrlIds(dto.getBackendUrlIds());
            dbSystemMenu.setFrontendUrl(dto.getFrontendUrl());
            systemMenuRepository.save(dbSystemMenu);
        }
        return  new MsgResponse("Successfully updated menu",true);
    }

    public List<Long> checkParentMenus(Long menuId) {
        SystemMenu menu = systemMenuRepository.findById(menuId).get();
        List<Long> list=new ArrayList<>();
        while(menu.getParent()!=null){
            list.add(menu.getParent().getId());
            menu = menu.getParent();
        }
        return  list;
    }
}

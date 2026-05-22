package hrm.auth.defaultSetup;

import hrm.auth.backendUrl.entity.BackendUrl;
import hrm.auth.backendUrl.repository.BackendUrlRepository;
import hrm.auth.common.util.CommonUtil;
import hrm.auth.ui_menu.entity.SystemMenu;
import hrm.auth.ui_menu.repository.SystemMenuRepository;
import hrm.auth.ui_menu_access.entity.MenuPermission;
import hrm.auth.ui_menu_access.repository.MenuPermissionRepository;
import hrm.auth.userAndRole.entity.SystemRole;
import hrm.auth.userAndRole.repository.SystemRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

@Service
public class DefaultSetupManager {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final BackendUrlRepository backendUrlRepository;
    private final SystemMenuRepository systemMenuRepository;
    private final MenuPermissionRepository menuPermissionRepository;
    private final SystemRoleRepository systemRoleRepository;

    public DefaultSetupManager(RequestMappingHandlerMapping mapping,
                               BackendUrlRepository backendUrlRepository,
                               SystemMenuRepository systemMenuRepository,
                               MenuPermissionRepository menuPermissionRepository,
                               SystemRoleRepository systemRoleRepository){
        this.requestMappingHandlerMapping = mapping;
        this.backendUrlRepository=backendUrlRepository;
        this.systemMenuRepository=systemMenuRepository;
        this.menuPermissionRepository=menuPermissionRepository;
        this.systemRoleRepository=systemRoleRepository;
    }

    private String getMethod(String method) {
        method = CommonUtil.removeCharFromString(method, '/');
        method = CommonUtil.removeCharFromString(method, '[');
        method = CommonUtil.removeCharFromString(method, ']');
        return method;
    }



    public void retrieveAllEndPointsAndSaveToDB() {

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

        List<String> api = new ArrayList<>();
        List<String> methods = new ArrayList<>();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            Set<String> apiInfo = info.getDirectPaths();
            if (apiInfo.size() > 0) {
                String method = "/" + info.getMethodsCondition().getMethods();
                method = getMethod(method);
                String apiUrl = new ArrayList<>(apiInfo).get(0);
                apiUrl = CommonUtil.removeAllSpace(apiUrl);
                if (!apiUrl.contains("error")) {
                    api.add(apiUrl);
                    methods.add(method);
                }
            }
        }

        String[] apiArray = api.toArray(new String[0]);
        String[] methodArray = methods.toArray(new String[0]);

        for(int i=0 ; i<apiArray.length;i++){
            BackendUrl entity = new BackendUrl();
            entity.setName(apiArray[i]);
            entity.setHttpMethod(methodArray[i]);
            try{
                backendUrlRepository.save(entity);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        //  Settings-->>1)Api 2)Menu 3)Menu Access
        //  Settings-->> Users
        this.createDefaultMenus();
        //give roles permission to default menu for admin
        this.defaultMenuPermission();



    }

    private void defaultMenuPermission(){
        //  Settings-->>1)Api 2)Menu 3)Menu Access
        //  Settings-->> Users
        //  Settings-->> Roles
        SystemMenu api = systemMenuRepository.findByName("Api").get();
        SystemMenu menu = systemMenuRepository.findByName("Menu").get();
        SystemMenu menuAccess = systemMenuRepository.findByName("Menu Access").get();
        SystemMenu users = systemMenuRepository.findByName("Users").get();
        SystemMenu roles = systemMenuRepository.findByName("Roles").get();

        SystemRole admin = this.systemRoleRepository.findByNameIgnoreCase("Admin");
        MenuPermission permOfApiMenu = MenuPermission.builder().menu(api).role(admin).build();
        MenuPermission permOfMenuMenu = MenuPermission.builder().menu(menu).role(admin).build();
        MenuPermission permOfMenuAccessMenu = MenuPermission.builder().menu(menuAccess).role(admin).build();
        MenuPermission permOfUsersMenu = MenuPermission.builder().menu(users).role(admin).build();
        MenuPermission permOfRolesMenu = MenuPermission.builder().menu(roles).role(admin).build();

        this.menuPermissionRepository.saveAll(List.of(permOfApiMenu,permOfMenuMenu,permOfMenuAccessMenu,permOfUsersMenu,permOfRolesMenu));

        SystemMenu auth = systemMenuRepository.findByName("Auth").get();
        SystemRole permitAll = this.systemRoleRepository.findByNameIgnoreCase("Permit_All");
        MenuPermission permOfOpenUrl = MenuPermission.builder().menu(auth).role(permitAll).build();
        this.menuPermissionRepository.saveAll(List.of(permOfOpenUrl));

    }

    private void createDefaultMenus() {
        //  Settings-->>1)Api 2)Menu 3)Menu Access
        //  Settings-->> Users
        //  Settings-->> Roles
        // Settings---> Auth
        SystemMenu settings = systemMenuRepository.findByName("Settings").orElse(SystemMenu.builder().name("Settings").build());

        List<SystemMenu> details = new ArrayList<>();

        //api
        SystemMenu api = systemMenuRepository.findByName("Api").orElse(SystemMenu.builder().name("Api").build());
        BackendUrl backendUrlGetApi = backendUrlRepository.findByNameAndHttpMethod("/backed-url","GET");
        BackendUrl backendUrlPostApi = backendUrlRepository.findByNameAndHttpMethod("/backed-url","POST");
        BackendUrl backendUrlPutApi = backendUrlRepository.findByNameAndHttpMethod("/backed-url","PUT");
        String backendIds = backendUrlGetApi.getId()+","+backendUrlPostApi.getId()+","+backendUrlPutApi.getId();
        api.setBackendUrlIds(backendIds);
        api.setParent(settings);
        details.add(api);

        //menu
        SystemMenu menu = systemMenuRepository.findByName("Menu").orElse(SystemMenu.builder().name("Menu").build());
        BackendUrl systemMenuGetApi = backendUrlRepository.findByNameAndHttpMethod("/system-menu","GET");
        BackendUrl systemMenuPostApi = backendUrlRepository.findByNameAndHttpMethod("/system-menu","POST");
        BackendUrl systemMenuPutApi = backendUrlRepository.findByNameAndHttpMethod("/system-menu","PUT");
        String systemMenuBackendIds =systemMenuGetApi.getId()+","+systemMenuPostApi.getId()+","+systemMenuPutApi.getId();
        menu.setBackendUrlIds(systemMenuBackendIds);
        menu.setParent(settings);
        details.add(menu);

        //menu access
        SystemMenu menuAccess =  systemMenuRepository.findByName("Menu Access").orElse(SystemMenu.builder().name("Menu Access").build());
        BackendUrl menuPermissionGetApi = backendUrlRepository.findByNameAndHttpMethod("/menu-permission","GET");
        BackendUrl menuPermissionPostApi = backendUrlRepository.findByNameAndHttpMethod("/menu-permission","POST");
        BackendUrl menuPermissionPutApi = backendUrlRepository.findByNameAndHttpMethod("/menu-permission","PUT");
        String menuPermissionBackendIds = menuPermissionGetApi.getId()+","+menuPermissionPostApi.getId()+","+menuPermissionPutApi.getId();
        menuAccess.setBackendUrlIds(menuPermissionBackendIds);
        menuAccess.setParent(settings);
        details.add(menuAccess);

        //users
        SystemMenu users = systemMenuRepository.findByName("Users").orElse(SystemMenu.builder().name("Users").build());
        BackendUrl systemRoleGetApi = backendUrlRepository.findByNameAndHttpMethod("/role","GET");
        BackendUrl systemUsersGetApi = backendUrlRepository.findByNameAndHttpMethod("/user","GET");
        BackendUrl systemUsersPutApi = backendUrlRepository.findByNameAndHttpMethod("/user","PUT");
        BackendUrl systemUsersPostApi = backendUrlRepository.findByNameAndHttpMethod("/user","POST");
        String usersMenusBackendUrlIds = systemRoleGetApi.getId()+","+systemUsersGetApi.getId()+","+ systemUsersPutApi.getId()+"," + systemUsersPostApi.getId();
        users.setBackendUrlIds(usersMenusBackendUrlIds);
        users.setParent(settings);
        details.add(users);

        //Roles
        SystemMenu roles = systemMenuRepository.findByName("Roles").orElse(SystemMenu.builder().name("Roles").build());
        BackendUrl systemRolePutApi = backendUrlRepository.findByNameAndHttpMethod("/role","PUT");
        BackendUrl systemRolePostApi = backendUrlRepository.findByNameAndHttpMethod("/role","POST");
        String roleMenusBackendUrlIds = systemRoleGetApi.getId()+","+systemRolePutApi.getId()+","+ systemRolePostApi.getId();
        roles.setBackendUrlIds(roleMenusBackendUrlIds);
        roles.setParent(settings);
        details.add(roles);

        //Auth
        SystemMenu auth = systemMenuRepository.findByName("Auth").orElse(SystemMenu.builder().name("Auth").build());
        BackendUrl authGetApi = backendUrlRepository.findByNameAndHttpMethod("/auth/getToken","POST");
        auth.setBackendUrlIds(authGetApi.getId().toString());
        auth.setParent(settings);
        details.add(auth);

        systemMenuRepository.saveAndFlush(settings);
        systemMenuRepository.saveAll(details);
    }

}
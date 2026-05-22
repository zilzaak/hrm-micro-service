package hrm.auth.ui_menu_access.controller;

import hrm.auth.common.dto.MsgResponse;
import hrm.auth.ui_menu_access.dto.MenuPermissionDTO;
import hrm.auth.ui_menu_access.service.MenuPermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/menu-permission")
public class MenuPermissionController {

    private final MenuPermissionService menuPermissionService;
    MenuPermissionController(MenuPermissionService menuPermissionService){
        this.menuPermissionService=menuPermissionService;
    }

    @GetMapping
    public ResponseEntity<?> getMenuPermission(@RequestParam Map<String,String> clientParams){
        MsgResponse response = menuPermissionService.getMenuPermission(clientParams);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MenuPermissionDTO dto){
        MsgResponse response = menuPermissionService.create(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody MenuPermissionDTO dto){
        MsgResponse response = menuPermissionService.update(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

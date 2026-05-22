package hrm.auth.ui_menu.controller;


import hrm.auth.common.dto.MsgResponse;
import hrm.auth.ui_menu.dto.SystemMenuDTO;
import hrm.auth.ui_menu.repository.SystemMenuRepository;
import hrm.auth.ui_menu.service.SystemMenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system-menu")
public class SystemMenuController {
    private final SystemMenuService systemMenuService;
    private final SystemMenuRepository systemMenuRepository;

    SystemMenuController(SystemMenuService systemMenuService, SystemMenuRepository systemMenuRepository){
        this.systemMenuService=systemMenuService;
        this.systemMenuRepository = systemMenuRepository;
    }

    @GetMapping
    public ResponseEntity<?> getList(@RequestBody SystemMenuDTO systemMenuDTO){
        MsgResponse response = systemMenuService.create(systemMenuDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SystemMenuDTO systemMenuDTO){
        MsgResponse response = systemMenuService.create(systemMenuDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<?> update(@RequestBody SystemMenuDTO systemMenuDTO){
        MsgResponse response = systemMenuService.update(systemMenuDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

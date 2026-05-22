package hrm.auth.userAndRole.controller;

import hrm.auth.common.dto.MsgResponse;
import hrm.auth.userAndRole.entity.SystemRole;
import hrm.auth.userAndRole.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;
    RoleController(RoleService roleService){
        this.roleService= roleService;
    }

    @GetMapping
    public ResponseEntity<?> getList(@RequestParam Map<String,String> params){
        MsgResponse response = roleService.getList(params);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SystemRole systemRole){
        MsgResponse response = roleService.create(systemRole);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<?> update(@RequestBody SystemRole systemRole){
        MsgResponse response = roleService.update(systemRole);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        MsgResponse response = roleService.delete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

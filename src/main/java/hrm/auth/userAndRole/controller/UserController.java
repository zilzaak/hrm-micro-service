package hrm.auth.userAndRole.controller;

import hrm.auth.common.dto.MsgResponse;
import hrm.auth.userAndRole.dto.SystemUserDTO;
import hrm.auth.userAndRole.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    UserController(UserService userService){
        this.userService= userService;
    }

    @GetMapping
    public ResponseEntity<?> getList(@RequestParam Map<String,String> params){
        MsgResponse response = userService.getList(params);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SystemUserDTO systemUserDTO){
        MsgResponse response = userService.create(systemUserDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<?> update(@RequestBody SystemUserDTO systemUserDTO){
        MsgResponse response = userService.update(systemUserDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        MsgResponse response = userService.delete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

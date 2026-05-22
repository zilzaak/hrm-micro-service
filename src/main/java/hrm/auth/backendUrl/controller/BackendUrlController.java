package hrm.auth.backendUrl.controller;
import hrm.auth.backendUrl.dto.BackendUrlDTO;
import hrm.auth.backendUrl.repository.BackendUrlRepository;
import hrm.auth.backendUrl.service.BackenUrlService;
import hrm.auth.common.dto.MsgResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/backed-url")
@Tag(name ="backend url")
public class BackendUrlController {

    private final  BackenUrlService backenUrlService;
    private final BackendUrlRepository backendUrlRepository;

    public BackendUrlController(BackenUrlService backenUrlService, BackendUrlRepository backendUrlRepository){
        this.backenUrlService=backenUrlService;
        this.backendUrlRepository = backendUrlRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BackendUrlDTO backendUrlDTO){
        MsgResponse response = backenUrlService.create(backendUrlDTO);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody BackendUrlDTO backendUrlDTO){
        MsgResponse response = backenUrlService.create(backendUrlDTO);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<?> getList(@RequestParam Map<String,String> clientParams) {
        MsgResponse response = backenUrlService.getList(clientParams);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}

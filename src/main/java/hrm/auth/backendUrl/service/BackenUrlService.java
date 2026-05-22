package hrm.auth.backendUrl.service;


import hrm.auth.backendUrl.dto.BackendUrlDTO;
import hrm.auth.backendUrl.entity.BackendUrl;
import hrm.auth.backendUrl.repository.BackendUrlRepository;
import hrm.auth.common.dto.MsgResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BackenUrlService {

    @Autowired
    private BackendUrlRepository backendUrlRepository;

    public MsgResponse create(BackendUrlDTO backendUrlDTO) {
                BackendUrl backed = BackendUrl.builder().
                name(backendUrlDTO.getName()).
                httpMethod(backendUrlDTO.getHttpMethod()).
                build();
                try{
                    backendUrlRepository.save(backed);
                    return new MsgResponse("Successfully created",true);
                }catch (Exception e){
                    return new MsgResponse(e.getMessage(),false);
                }
    }

    public MsgResponse update(BackendUrlDTO backendUrlDTO) {

        BackendUrl backed = backendUrlRepository.findById(backendUrlDTO.getId()).orElse(null);
        if(backed==null){
            return new MsgResponse("Record not found",false);
        }
        try{
            backed.setName(backendUrlDTO.getName());
            backed.setHttpMethod(backendUrlDTO.getHttpMethod());
            backendUrlRepository.save(backed);
            return new MsgResponse("Successfully updated",true);
        }catch (Exception e){
            return new MsgResponse(e.getMessage(),false);
        }
    }

    public MsgResponse getList(Map<String, String> params) {
                    Long id=null;
                    String name=null;
                    String httpMethod=null;
                    if(params.containsKey("id")){
                        id=Long.parseLong(params.get("id"));
                    }
                    if(params.containsKey("name")){
                        name=params.get("name");
                    }
                    if(params.containsKey("httpMethod")){
                        httpMethod=params.get("httpMethod");
                    }
        List<BackendUrl> list = backendUrlRepository.getList(id,name,httpMethod);
        return new MsgResponse(list,true);
    }
}

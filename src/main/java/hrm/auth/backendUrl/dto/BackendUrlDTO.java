package hrm.auth.backendUrl.dto;


import lombok.Data;

@Data
public class BackendUrlDTO {
    private Long id;
    private String name;
    private String httpMethod;
}

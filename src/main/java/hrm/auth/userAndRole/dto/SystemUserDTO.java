package hrm.auth.userAndRole.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class SystemUserDTO {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String address;
    private String displayName;
    private Boolean enabled;
    private List<UserRolesDTO> roleList;
}

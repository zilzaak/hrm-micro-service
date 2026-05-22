package hrm.auth.userAndRole.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRolesDTO {
    private Long id;
    private Long systemRoleId;
    private Long systemUserId;
}

package hrm.auth.ui_menu_access.dto;

import lombok.Data;

@Data
public class MenuPermissionDTO {
    private Long id;
    private Long menuId;
    private Long userId;
    private Long roleId;
}

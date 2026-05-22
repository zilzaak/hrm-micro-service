package hrm.auth.ui_menu.dto;

import lombok.Data;

@Data
public class SystemMenuDTO {
    private Long id;
    private String name;
    private Long parentId;
    private String frontendUrl;
    private String backendUrlIds;
}

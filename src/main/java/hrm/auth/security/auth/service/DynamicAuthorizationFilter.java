package hrm.auth.security.auth.service;

import hrm.auth.backendUrl.repository.BackendUrlRepository;
import hrm.auth.common.util.CommonUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class DynamicAuthorizationFilter extends OncePerRequestFilter {

    private final DynamicPermissionService dynamicPermissionService;
    private final BackendUrlRepository backendUrlRepository;

    DynamicAuthorizationFilter(DynamicPermissionService dynamicPermissionService,BackendUrlRepository backendUrlRepository){
        this.dynamicPermissionService=dynamicPermissionService;
        this.backendUrlRepository=backendUrlRepository;
      }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        boolean hasPermissionToAccess = false;

        if (CommonUtil.isUrlOpenToAll(request.getRequestURI())) {
                hasPermissionToAccess = true;
        }else{
            Long requestedUrlId = this.backendUrlRepository.findByNameAndHttpMethod(request.getRequestURI(),request.getMethod()).getId();
            Map<String, String> permissions = dynamicPermissionService.getPermissions();
            for(String key :  permissions.keySet()){
                if((","+permissions.get(key)+",").contains(","+requestedUrlId+",")){
                    hasPermissionToAccess=true;
                    break;
                }
            }
        }
              if(!hasPermissionToAccess){
                  response.sendError(HttpServletResponse.SC_FORBIDDEN, "No role or permission found against the requested url");
                  return;
              }

            filterChain.doFilter(request, response);

    }
}

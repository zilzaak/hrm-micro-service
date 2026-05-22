package hrm.auth.security.auth.service;

import hrm.auth.backendUrl.repository.BackendUrlRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        if (request.getRequestURI().contains("/swagger-ui") ||
                request.getRequestURI().contains("/api-docs") ||
                request.getRequestURI().contains("/v3/api-docs") ||
                request.getRequestURI().contains("/swagger-resources") ||
                request.getRequestURI().contains("/webjars") ||
                request.getRequestURI().equals("/swagger-ui.html") ||
                request.getRequestURI().contains("/configuration/ui") ||
                request.getRequestURI().contains("/configuration/security")) {
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

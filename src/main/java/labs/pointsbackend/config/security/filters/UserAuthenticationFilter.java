package labs.pointsbackend.config.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import labs.pointsbackend.config.security.authentications.UserAuthentication;
import labs.pointsbackend.config.security.authentications.UserPrincipal;
import labs.pointsbackend.model.entities.User;
import labs.pointsbackend.model.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;


@AllArgsConstructor
@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {
    public static final String X_REDIRECT = "X-Redirect";

    private static final String ROOT_PAGE = "/";
    private static final String LOGIN_PAGE = "/login";
    private static final String POINTS_PAGE = "/points";

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String sessionId = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (sessionId != null) {
            User user = userService.findUserBySessionId(sessionId);
            if (user != null) {
                UserPrincipal userPrincipal = new UserPrincipal(user);
                var userAuthentication =
                        new UserAuthentication(userPrincipal, false);

                if (isSessionIdActive(userPrincipal)) {
                    userPrincipal.eraseCredentials();
                    userAuthentication.setAuthenticated(true);

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(userAuthentication);
                    SecurityContextHolder.setContext(context);

                    if (isRequestToStartPage(request)) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setHeader(X_REDIRECT, POINTS_PAGE);
                        return;
                    }
                }

            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isRequestToStartPage(HttpServletRequest request) {
        return request.getServletPath().equals(LOGIN_PAGE)
                || request.getServletPath().equals(ROOT_PAGE);
    }

    private boolean isSessionIdActive(UserPrincipal userPrincipal) {
        return userPrincipal.getExpirationDate() != null && userPrincipal.getExpirationDate().isAfter(LocalDateTime.now());
    }

}

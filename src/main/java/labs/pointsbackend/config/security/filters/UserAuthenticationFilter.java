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
import java.util.Optional;


@AllArgsConstructor
@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {
    public static final String X_REDIRECT = "X-Redirect";

    private static final String LOGIN_PAGE_URL_FIRST = "/";
    private static final String LOGIN_PAGE_URL_SECOND = "/login";
    private static final String POINTS_PAGE = "/points";

    private static final String STYLE_CSS = "/start_page_styles/login.css";
    private static final String FAVICON_ICO = "/favicon.ico";

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

                if (isSessionIdExpired(userPrincipal)) {
                    userPrincipal.eraseCredentials();
                    userAuthentication.setAuthenticated(true);

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(userAuthentication);
                    SecurityContextHolder.setContext(context);

                    if (isRequestToLoginPage(request)) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setHeader(X_REDIRECT, POINTS_PAGE);
                        return;
                    }
                    filterChain.doFilter(request, response);
                    return;
                }

            }
        }
        if (isRequestToLoginPage(request)
                || isRequestToStaticContent(request)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean isRequestToLoginPage(HttpServletRequest request) {
        return request.getServletPath().equals(LOGIN_PAGE_URL_FIRST)
                || request.getServletPath().equals(LOGIN_PAGE_URL_SECOND);
    }

    private boolean isRequestToStaticContent(HttpServletRequest request) {
        return request.getServletPath().equals(STYLE_CSS)
                || request.getServletPath().equals(FAVICON_ICO);
    }

    private boolean isSessionIdExpired(UserPrincipal userPrincipal) {
        return Optional.of(userPrincipal.getExpirationDate())
                .map(expirationDate -> expirationDate.isAfter(LocalDateTime.now()))
                .orElse(false);
    }

}

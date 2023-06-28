package io.perfume.api.common.signIn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.perfume.api.auth.application.port.in.MakeNewTokenUseCase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SignInAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final MakeNewTokenUseCase makeNewTokenUseCase;
    private final ObjectMapper objectMapper;

    public SignInAuthenticationFilter(AuthenticationManager authenticationManager, MakeNewTokenUseCase makeNewTokenUseCase) {
        this.objectMapper = new ObjectMapper();
        this.makeNewTokenUseCase = makeNewTokenUseCase;

        super.setAuthenticationManager(authenticationManager);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/v1/login", "POST"));
    }

    /**
     * @param request  from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     *                 redirect as part of a multi-stage authentication process (such as OIDC).
     * @return Authentication Object based on username and password
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        SignInDto signInDto;
        try {
            signInDto = objectMapper.readValue(StreamUtils.copyToString(
                    request.getInputStream(), StandardCharsets.UTF_8), SignInDto.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(signInDto.getUsername(), signInDto.getPassword());
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    /**
     * @param request
     * @param response
     * @param filter
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     *                   method.
     *                   authenticated result comes from AuthenticationProvider.
     *                   deliver JWT to the LoginSuccessHandler!
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filter,
                                            Authentication authResult) throws JsonProcessingException {
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
        String accessToken = makeNewTokenUseCase.createAccessToken(principal);
        response.addHeader("Authorization", accessToken);

        String refreshToken = makeNewTokenUseCase.createRefreshToken(principal.getUser());
        Cookie cookie = new Cookie("X-Refresh-Token", refreshToken);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        SecurityContextHolder.getContext().setAuthentication(authResult);
        try {
            response.getWriter().write(
                    objectMapper.writeValueAsString(
                            (authResult.getPrincipal())));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String errorMessage = "Authentication failed: " + failed.getMessage();
        response.getWriter().write(
                objectMapper.writeValueAsString(errorMessage));
    }
}

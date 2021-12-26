package com.github.prgrms.users;

import com.github.prgrms.errors.NotFoundException;
import com.github.prgrms.errors.UnauthorizedException;
import com.github.prgrms.security.Jwt;
import com.github.prgrms.security.JwtAuthentication;
import com.github.prgrms.security.JwtAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.github.prgrms.utils.ApiUtils.ApiResult;
import static com.github.prgrms.utils.ApiUtils.success;

@RestController
@RequestMapping("api/users")
public class UserRestController {

  private final Jwt jwt;

  private final AuthenticationManager authenticationManager;

  private final UserService userService;

  public UserRestController(Jwt jwt, AuthenticationManager authenticationManager, UserService userService) {
    this.jwt = jwt;
    this.authenticationManager = authenticationManager;
    this.userService = userService;
  }

  @PostMapping(path = "login")
  public ApiResult<LoginResult> login(
    @Valid @RequestBody LoginRequest request
  ) throws UnauthorizedException {
    try {
      Authentication authentication = authenticationManager.authenticate(
        new JwtAuthenticationToken(request.getPrincipal(), request.getCredentials())
      );
      final User user = (User) authentication.getDetails();
      final String token = user.newJwt(
        jwt,
        authentication.getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .toArray(String[]::new)
      );
      return success(new LoginResult(token, user));
    } catch (AuthenticationException e) {
      throw new UnauthorizedException(e.getMessage(), e);
    }
  }

  @GetMapping(path = "me")
  public ApiResult<UserDto> me(
    // JwtAuthenticationTokenFilter ?—?„œ JWT ê°’ì„ ?†µ?•´ ?‚¬?š©?žë¥? ?¸ì¦í•œ?‹¤.
    // ?‚¬?š©?ž ?¸ì¦ì´ ? •?ƒ?œ¼ë¡? ?™„ë£Œë?‹¤ë©? @AuthenticationPrincipal ?–´?…¸?…Œ?´?…˜?„ ?‚¬?š©?•˜?—¬ ?¸ì¦ëœ ?‚¬?š©?ž ? •ë³?(JwtAuthentication)?— ? ‘ê·¼í•  ?ˆ˜ ?žˆ?‹¤.
    @AuthenticationPrincipal JwtAuthentication authentication
  ) {
    return success(
      userService.findById(authentication.id)
        .map(UserDto::new)
        .orElseThrow(() -> new NotFoundException("Could nof found user for " + authentication.id))
    );
  }

}
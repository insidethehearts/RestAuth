package me.therimuru.RestAuth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.therimuru.RestAuth.dto.requests.profile.UpdateProfileDTO;
import me.therimuru.RestAuth.security.UserIdAuthentication;
import me.therimuru.RestAuth.service.contract.adapter.ProfileService;
import me.therimuru.RestAuth.service.contract.internal.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {

    private final UserService userService;
    private final ProfileService profileService;

    @PreAuthorize("isAuthenticated()")
    @PatchMapping(value = "/edit")
    public ResponseEntity<Object> edit(@RequestBody @Valid UpdateProfileDTO updateProfileDto) {
        final UserIdAuthentication authentication = (UserIdAuthentication) SecurityContextHolder.getContext().getAuthentication();

        userService.edit(authentication.getId(), updateProfileDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> viewProfile(@PathVariable("id") Long targetId) {
        final UserIdAuthentication authentication = (UserIdAuthentication) SecurityContextHolder.getContext().getAuthentication();

        final Map<String, Object> responseBody = profileService.getProfileInfo(targetId, authentication);

        return ResponseEntity.ok(responseBody);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> viewSelfProfile() {
        final UserIdAuthentication authentication = (UserIdAuthentication) SecurityContextHolder.getContext().getAuthentication();

        final Map<String, Object> responseBody = profileService.getSelfProfileInfo(authentication.getId());
        return ResponseEntity.ok(responseBody);
    }
}
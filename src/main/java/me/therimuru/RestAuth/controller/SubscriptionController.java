package me.therimuru.RestAuth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.dto.requests.SubscribeDTO;
import me.therimuru.RestAuth.security.UserIdAuthentication;
import me.therimuru.RestAuth.service.contract.internal.SubscriptionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(@RequestBody @Valid SubscribeDTO subscribeDTO) {
        final UserIdAuthentication authentication = (UserIdAuthentication) SecurityContextHolder.getContext().getAuthentication();

        subscriptionService.subscribe(authentication.getId(), subscribeDTO.getTargetId());

        return ResponseEntity.ok(new HashMap<>());
    }

}
package me.therimuru.RestAuth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.dto.requests.subscriptions.SubscribeDTO;
import me.therimuru.RestAuth.entity.SubscriptionEntity;
import me.therimuru.RestAuth.security.UserIdAuthentication;
import me.therimuru.RestAuth.service.contract.internal.SubscriptionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/subscribe")
    public ResponseEntity<Object> subscribe(@RequestBody @Valid SubscribeDTO subscribeDTO) {
        final UserIdAuthentication authentication = (UserIdAuthentication) SecurityContextHolder.getContext().getAuthentication();

        subscriptionService.subscribe(authentication.getId(), subscribeDTO.getTargetId());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/subscribers", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> subscribers() {
        final UserIdAuthentication authentication = (UserIdAuthentication) SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(subscriptionService.getSubscribers(authentication.getId()).stream().map(SubscriptionEntity::getSubscriberId).toList());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/subscriptions", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> subscriptions() {
        final UserIdAuthentication authentication = (UserIdAuthentication) SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(subscriptionService.getSubscriptions(authentication.getId()).stream().map(SubscriptionEntity::getTargetId).toList());
    }
}
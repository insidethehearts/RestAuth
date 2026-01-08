package me.therimuru.RestAuth.service.contract.adapter;

import me.therimuru.RestAuth.security.UserIdAuthentication;

import java.util.Map;

public interface ProfileService {

    Map<String, Object> getProfileInfo(Long targetId, UserIdAuthentication authentication);

    Map<String, Object> getSelfProfileInfo(Long id);

}
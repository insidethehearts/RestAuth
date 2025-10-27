package me.therimuru.RestAuth.repository;

import me.therimuru.RestAuth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByLogin(String login);

}
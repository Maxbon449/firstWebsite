package com.maxbon.springFirst.domain.user.repository;

import com.maxbon.springFirst.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUsername(String username);
    Boolean existsByNickname(String nickname);
    Optional<UserEntity> findByUsername(String username); // Optional 쓰기

    @Transactional
    void deleteByUsername(String username);
}

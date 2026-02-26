package com.maxbon.springFirst.domain.user.service;

import com.maxbon.springFirst.domain.user.auth.PrincipalDetails;
import com.maxbon.springFirst.domain.user.dto.UserRequestDTO;
import com.maxbon.springFirst.domain.user.dto.UserResponseDTO;
import com.maxbon.springFirst.domain.user.entity.UserEntity;
import com.maxbon.springFirst.domain.user.entity.UserRoleType;
import com.maxbon.springFirst.domain.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // isAccess 권한 체크
    public boolean isAccess(String username){

        // 현재 로그인 된 유저의 username
        String sessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // 현재 로그인 된 유저의 role
        String sessionRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();

        // ADMIN 권한이 주어졌는지.
        if("ROLE_ADMIN".equals(sessionRole)){
            return true;
        }
        // 현재 로그인 한 유저가 접근하려는 정보의 유저와 동일 인물인지.
        if(sessionUsername.equals(username)){
            return true;
        }

        return false;
    }


    // CREATE 유저 생성
    @Transactional
    public void createOneUser(UserRequestDTO dto){

        if(userRepository.existsByUsername(dto.getUsername())){
           return;
        }
        if(userRepository.existsByNickname(dto.getNickname())){
            return;
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setUserRole(UserRoleType.USER);


        userRepository.save(user);
    }

    // 어드민 계정 만들기



    // READ 유저 읽기
    // 유저 한 명 읽기
    @Transactional(readOnly = true)
    public UserResponseDTO readOneUser(String username){

        UserEntity user = userRepository.findByUsername(username).orElseThrow();

        UserResponseDTO dto = new UserResponseDTO();
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setRole(user.getUserRole().toString()); // UserRoleType 형이니까 toString 으로 문자열로 만들기

        return dto;
    }

    // 유저 모두 읽기
    @Transactional(readOnly = true)
    public List<UserResponseDTO> readAllUsers(){

        List<UserEntity> list = userRepository.findAll();
        List<UserResponseDTO> dtos = new ArrayList<>();

        for(UserEntity user : list){
            UserResponseDTO dto = new UserResponseDTO();
            dto.setUsername(user.getUsername());
            dto.setNickname(user.getNickname());
            dto.setRole(user.getUserRole().toString());
        }

        return dtos;
    }

    // 유저 한 명 읽기, 로그인 용 (로그인 같은 경우 읽기지만, 시큐리티 형식으로 맞춰야 함.)
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity entity = userRepository.findByUsername(username).orElseThrow();

        return new PrincipalDetails(entity);

    //  return User.builder()
    //          .username(entity.getUsername())
    //          .password(entity.getPassword())
    //          .roles(entity.getUserRole().toString())
    //          .build();
    }

    // UPDATE 유저 업데이트
    // 유저 한 명 업데이트
    @Transactional
    public void updateOneUser(UserRequestDTO dto, String username){

        UserEntity user = userRepository.findByUsername(username).orElseThrow();

        if(dto.getNickname() != null && !dto.getNickname().isEmpty()){
            user.setNickname(dto.getNickname());
        }

        if(dto.getPassword() != null && !dto.getPassword().isEmpty()){
            user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(user);
    }


    // 유저 한 명 MANAGER 로 승급



    // DELETE 유저 삭제
    @Transactional
    public void deleteOneUser(String username){
        userRepository.deleteByUsername(username);
    }
}

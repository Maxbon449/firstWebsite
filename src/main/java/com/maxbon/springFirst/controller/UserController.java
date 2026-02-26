package com.maxbon.springFirst.controller;

import com.maxbon.springFirst.domain.user.auth.PrincipalDetails;
import com.maxbon.springFirst.domain.user.dto.UserRequestDTO;
import com.maxbon.springFirst.domain.user.dto.UserResponseDTO;
import com.maxbon.springFirst.domain.user.repository.UserRepository;
import com.maxbon.springFirst.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // 회원가입 페이지 : 페이지 응답
    @GetMapping("/join")
    public String joinPage(){

        return "join";
    }

    // 회원가입 페이지 : 수행
    @PostMapping("/join")
    public String joinPost(UserRequestDTO dto){
        userService.createOneUser(dto);

        return "redirect:/login";
    }

    // 중복 체크용 API
    @GetMapping("/api/check-username")
    @ResponseBody
    public boolean checkUsername(@RequestParam String username) {
        return userRepository.existsByUsername(username);
    }
    // 중복 체크용 API
    @GetMapping("/api/check-nickname")
    @ResponseBody
    public boolean checkNickname(@RequestParam String nickname) {
        return userRepository.existsByNickname(nickname);
    }


    // 유저 목록 + 매니저로 승급하는 기능? (어드민 페이지로)

    // 회원 수정 & 탈퇴 페이지 : 페이지 응답
    @GetMapping("/update/{username}")
    public String updatePage(@PathVariable String username, Model model){

        // 본인 또는 어드민만 접속 가능.
        if(userService.isAccess(username)){
            UserResponseDTO dto = userService.readOneUser(username);
            model.addAttribute("USER", dto);
            return "update";
        }

        return "redirect:/login";
    }

    // 회원 수정 페이지 : 수행
    @PostMapping("/update/{username}")
    public String updatePost(@PathVariable String username, UserRequestDTO dto,
                             @AuthenticationPrincipal PrincipalDetails principalDetails){

        // 본인 또는 어드민만 접속 가능.
        if(userService.isAccess(username)){
            userService.updateOneUser(dto, username);
            principalDetails.getUser().setNickname(dto.getNickname());
            principalDetails.getUser().setPassword(dto.getPassword());

            return "redirect:/";
        }

        return "redirect:/login";
    }

    // 회원 탈퇴 : 수행
    @PostMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username, HttpSession session){

        if(userService.isAccess(username)){
            userService.deleteOneUser(username);
            session.invalidate();

            return "redirect:/";
        }
        return "redirect:/login";
    }

    // 로그인 : 페이지 응답
    @GetMapping("/login")
    public String loginPage(){

        return "login";
    }
    // 로그인 : 수행 -> 이건 스프링 시큐리티가 해줄 거고, SecurityConfig 에서 POST 주소 정해줄 수 있음.
    // 로그아웃 : 수행 -> 이것도 스프링 시큐리티가 해줌.
}

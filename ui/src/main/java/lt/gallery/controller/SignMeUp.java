package lt.gallery.controller;

import lt.gallery.dao.Role;
import lt.gallery.dao.User;
import lt.gallery.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.HashSet;
import java.util.Set;

//Just to add users. No longer used
@Controller
@RequestMapping
public class SignMeUp {
//
//    private final PasswordEncoder passwordEncoder;
//    private final UserRepository userRepository;
//
//    public SignMeUp(PasswordEncoder passwordEncoder, UserRepository userRepository) {
//        this.passwordEncoder = passwordEncoder;
//        this.userRepository = userRepository;
//    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
////
//        Set<Role> roles = new HashSet<>();
//        User user=new User();
//        user.setUsername("test2");
//        user.setPassword("test");
//        roles.add(Role.ADMIN);
//        user.setRoles(roles);
//        user.setActive(true);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        userRepository.save(user);




        return "login";
    }

    @PostMapping (value = "/gallery/sign", params ="action=Save")
    public String createUser(@ModelAttribute ("user") User user)  {
        return "redirect:/login";
    }
}

package com.example.trdemoapi.service;

import com.example.trdemoapi.dto.CreateUserReq;
import com.example.trdemoapi.dto.PasswordChangeReq;
import com.example.trdemoapi.model.ERole;
import com.example.trdemoapi.model.Role;
import com.example.trdemoapi.model.User;
import com.example.trdemoapi.repository.RoleRepository;
import com.example.trdemoapi.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("userDetailsService")
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> allUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User loadCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return loadUserByEmail(authentication.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), user.isEnabled(), true, true,
                true, getAuthorities(user.getRoles()));
    }

    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User loadUserById(Long id) throws IllegalArgumentException {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    @Transactional
    public void changePassword(User user, PasswordChangeReq request) throws IllegalArgumentException {
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public User createUser(CreateUserReq request) {
        var user = new User()
                .setName(request.getName())
                .setEmail(request.getEmail())
                .setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = new ArrayList<Role>();
        for (var role : request.getRoles()) {
            roles.add(roleRepository.findByName(role.getNameWithPrefix()));
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public User updateUserRoles(User user, List<ERole> roles) {
        var newRoles = new ArrayList<Role>();
        for (var role : roles) {
            newRoles.add(roleRepository.findByName(role.getNameWithPrefix()));
        }

        user.setRoles(newRoles);
        return userRepository.save(user);
    }

    @Transactional
    public User addUserRole(User user, ERole role) {
        if (user.hasRole(role)) {
            return user;
        }

        var roles = user.getRoles();
        roles.add(roleRepository.findByName(role.getNameWithPrefix()));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
        }

        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}

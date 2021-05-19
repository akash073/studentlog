package com.ideal.studentlog.controllers;

import com.ideal.studentlog.database.models.Teacher;
import com.ideal.studentlog.helpers.dataclass.TeacherDTO;
import com.ideal.studentlog.helpers.exceptions.ServiceException;
import com.ideal.studentlog.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService service;

    @RolesAllowed({"ROLE_STUDENT"})
    @GetMapping
    public List<TeacherDTO> getAll() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication instanceof AnonymousAuthenticationToken){
            int a = 0;
            return null;
        }

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) authentication.getPrincipal();

        AccessToken token = keycloakPrincipal.getKeycloakSecurityContext().getToken();

        System.out.println(authentication);
        System.out.println(token.getEmail());

        return service.getAll();
    }

    @GetMapping(path = "/{id}")
    public TeacherDTO getById(@PathVariable("id") Integer id) throws ServiceException {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherDTO create(@RequestBody @Valid TeacherDTO dto) {
        return service.create(dto);
    }

    @PatchMapping(path = "/{id}")
    public TeacherDTO update(@PathVariable("id") Integer id, @RequestBody @Valid TeacherDTO dto) throws ServiceException {
        return service.update(id, dto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id) {
        service.delete(id);
    }
}

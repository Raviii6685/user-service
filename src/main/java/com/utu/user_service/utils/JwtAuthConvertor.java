package com.utu.user_service.utils;


import com.google.rpc.context.AttributeContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtAuthConvertor implements Converter<Jwt , AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final String principleAttribute="preferred_username";
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        Collection <GrantedAuthority> Authorities = Stream.concat(
               jwtGrantedAuthoritiesConverter.convert(source).stream(),
                       extractResourceRoles(source).stream()
       ).collect(Collectors.toSet());
       return new JwtAuthenticationToken(
               source, Authorities
               ,getPrincipleClaimName(source)
       );
    }

    private String getPrincipleClaimName(Jwt source) {
        String ClaimNames = JwtClaimNames.SUB;
        if (principleAttribute!=null){
            ClaimNames=principleAttribute;
        }
        return source.getClaim(ClaimNames);

    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt source) {
        Map<String,Object> resourceAcess;
        Map<String,Object> resource;
        Collection<String>resourceRoles;
        if (source.getClaim("resource_access")==null){
            return Set.of();
        }
        resourceAcess = source.getClaim("resource_access");
        if (resourceAcess.get("account")==null){
            return Set.of();
        }
        resource=(Map<String,Object>)resourceAcess.get("account");
        if (resource.get("roles")==null){
            return Set.of();
        }
        resourceRoles = (Collection<String>)resource.get("roles");
        return resourceRoles
                .stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role)).
                toList();

    }

    @Override
    public <U> Converter<Jwt, U> andThen(Converter<? super AbstractAuthenticationToken, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}

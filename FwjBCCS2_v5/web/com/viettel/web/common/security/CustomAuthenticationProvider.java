package com.viettel.web.common.security;

import com.viettel.fw.common.util.Const;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.passport.CustomConnector;
import com.viettel.fw.passport.GrantedAuthorityPassport;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import viettel.passport.client.ObjectToken;
import viettel.passport.client.UserToken;
import viettel.passport.util.Connector;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by LamNV5 on 4/16/2015.
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static Logger logger = Logger
            .getLogger(CustomAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("Bad Credentials");
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extenalContext = facesContext.getExternalContext();

        HttpServletRequest request = (HttpServletRequest) extenalContext.getRequest();
        HttpServletResponse httpServletResponse = (HttpServletResponse) extenalContext.getResponse();
        //thiendn1: make the buffer bufferPrincipal here
        //must not change
        CustomPrincipal principal = new CustomPrincipal();
        URLBean urlBean = (URLBean) request.getSession().getAttribute(Const.URL_BEAN);
        principal.getPrincipals().add(urlBean);

        Connector cnn = new Connector(request, httpServletResponse);
        request.setAttribute("VSA-IsPassedVSAFilter", "True");
        try {
            if (!(cnn.getAuthenticate())) {
                throw new BadCredentialsException("Bad Credentials");
            }
        } catch (IOException e) {
            logger.fatal(e);
            throw new BadCredentialsException("Bad Credentials");
        }

        UserToken userToken = (UserToken) request.getSession().getAttribute(
                CustomConnector.VSA_USER_TOKEN);

        //x_1604_1: co che phan quyen sping
        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        for (ObjectToken component : userToken.getComponentList()) {
            GrantedAuthorityPassport subComponent = new GrantedAuthorityPassport();
            BeanUtils.copyProperties(component, subComponent,
                    new String[]{"childObjects"});

            grantedAuths.add(subComponent);
        }

        //thiendn: phan quyen theo module chua xu ly
//        for (ObjectToken role : userToken.getParentMenu()) {
//            GrantedAuthorityPassport subRole = new GrantedAuthorityPassport();
//            BeanUtils.copyProperties(role, subRole,
//                    new String[]{"childObjects"});
//            grantedAuths.add(subRole); //add granted
//
//            for (ObjectToken role2 : role.getChildObjects()) {
//                GrantedAuthorityPassport subRole2 = new GrantedAuthorityPassport();
//                BeanUtils.copyProperties(role2, subRole2,
//                        new String[]{"childObjects"});
//                grantedAuths.add(subRole2); //add granted
//
//                for (ObjectToken role3 : role2.getChildObjects()) {
//                    GrantedAuthorityPassport subRole3 = new GrantedAuthorityPassport();
//                    BeanUtils.copyProperties(role3, subRole3,
//                            new String[]{"childObjects"});
//                    subRole2.getChildObjects().add(subRole3);
//                    grantedAuths.add(subRole3); //add granted
//                }
//
//                subRole.getChildObjects().add(subRole2);
//            }
//
//        }
        //x_1604_1: co che phan quyen sping
        principal.getPrincipals().add(0, userToken.getFullName());
        return new UsernamePasswordAuthenticationToken(principal, null, grantedAuths);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public static boolean hasRole(String role) {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean hasRole = false;
        for (GrantedAuthority authority : authorities) {
            //logger.info(authority.getAuthority());
            hasRole = authority.getAuthority().equals(role);
            if (hasRole) {
                break;
            }
        }

        return hasRole;
    }

    public static RequiredRoleMap getAllRoles() {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<String> roles = authorities.stream().map(x -> x.getAuthority()).collect(Collectors.toList());
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.setValues(roles);
        return requiredRoleMap;
    }

    public static RequiredRoleMap createRequiredRoleMap(String... roles) {
        RequiredRoleMap roleMap = new RequiredRoleMap();
        for (String role : roles) {
            if (hasRole(role)) roleMap.add(role);
        }

        return roleMap;
    }

    public static RequiredRoleMap createRequiredRoleMap(List<String> roles) {
        RequiredRoleMap roleMap = new RequiredRoleMap();
        roles.stream().filter(CustomAuthenticationProvider::hasRole).forEach(roleMap::add);
        return roleMap;
    }
}

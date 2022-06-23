package com.hcmus.lovelybackend.security;

import com.hcmus.lovelybackend.constant.AuthProvider;
import com.hcmus.lovelybackend.entity.common.UserPrincipal;
import com.hcmus.lovelybackend.entity.dao.UserDAO;
import com.hcmus.lovelybackend.entity.oauth2.OAuth2UserInfo;
import com.hcmus.lovelybackend.entity.oauth2.OAuth2UserInfoFactory;
import com.hcmus.lovelybackend.exception.other.OAuth2AuthenticationProcessingException;
import com.hcmus.lovelybackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        UserDAO userDAO = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        if (userDAO != null) {
//            if (!userDAO.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId())) && userDAO.getProvider() != AuthProvider.local) {
//                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
//                        userDAO.getProvider() + " account. Please use your " + userDAO.getProvider() +
//                        " account to login.");
//            }
//            if(userDAO.getProvider() != AuthProvider.local){
//                userDAO = updateExistingUser(userDAO, oAuth2UserInfo);
//            }
            userDAO = updateExistingUser(userDAO, oAuth2UserInfo);
        } else {
            userDAO = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        return UserPrincipal.create(userDAO, oAuth2User.getAttributes());
    }

    private UserDAO registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        UserDAO user = new UserDAO();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setEnabled(true);
        return userRepository.save(user);
    }

    private UserDAO updateExistingUser(UserDAO existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

}

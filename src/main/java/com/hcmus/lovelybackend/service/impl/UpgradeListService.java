package com.hcmus.lovelybackend.service.impl;

import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.common.TokenUser;
import com.hcmus.lovelybackend.entity.dao.UpgradeList;
import com.hcmus.lovelybackend.entity.dao.UserDAO;
import com.hcmus.lovelybackend.entity.response.AuPageResponse;
import com.hcmus.lovelybackend.exception.runtime.BadRequestException;
import com.hcmus.lovelybackend.exception.runtime.NotFoundException;
import com.hcmus.lovelybackend.repository.UpgradeListRepository;
import com.hcmus.lovelybackend.repository.UserRepository;
import com.hcmus.lovelybackend.service.IUpgradeListService;
import com.hcmus.lovelybackend.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UpgradeListService implements IUpgradeListService {

    @Autowired
    private UpgradeListRepository upgradeListRepository;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AuPageResponse getUpgradeList(String token, Integer page, Integer size) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Page<UpgradeList> upgradeLists = upgradeListRepository.findAll(PageRequest.of(page, size));
        return new AuPageResponse(tokenUser.getToken(), upgradeLists);
    }

    @Override
    public AuPageResponse getListSeller(String token, Integer page, Integer size) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Page<UserDAO> userDAOS = userRepository.findAllByRole(2, PageRequest.of(page, size));
        return new AuPageResponse(tokenUser.getToken(), userDAOS);
    }

    @Override
    public AuMessageCommonResponse acceptUpgradeUser(String token, Integer bidderId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UpgradeList upgradeList = upgradeListRepository.findAllByBidderId(bidderId).orElseThrow(() -> {
            throw new NotFoundException("Upgrade list not found");
        });
        UserDAO userDAO = userRepository.findById(upgradeList.getBidder().getId()).orElseThrow(() -> {
            throw new NotFoundException("User not found ");
        });
        userDAO.setRole(2);
        userRepository.save(userDAO);
        upgradeListRepository.deleteById(upgradeList.getId());
        return new AuMessageCommonResponse(tokenUser.getToken(), "Upgrade User " + userDAO.getEmail() + " success");
    }

    @Override
    public AuMessageCommonResponse downgradeUser(String token, Integer sellerId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UserDAO userDAO = userRepository.findById(sellerId).orElseThrow(() -> {
            throw new NotFoundException("User not found ");
        });
        if (!userDAO.getRole().equals(2)){
            throw new BadRequestException("User isn't seller");
        }
        userDAO.setRole(1);
        userRepository.save(userDAO);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Downgrade User " + userDAO.getEmail() + " success");
    }

    @Override
    public AuMessageCommonResponse requestUpgrade(String token) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        if(tokenUser.getUserDAO().getRole().equals(1)){
            UpgradeList upgradeList = new UpgradeList();
            upgradeList.setBidder(tokenUser.getUserDAO());
            upgradeListRepository.save(upgradeList);
        }else{
            throw new BadRequestException("You is seller.");
        }
        return new AuMessageCommonResponse(tokenUser.getToken(), "Request upgrade success");
    }
}

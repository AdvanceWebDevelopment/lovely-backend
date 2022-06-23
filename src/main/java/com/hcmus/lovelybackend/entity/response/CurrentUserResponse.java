package com.hcmus.lovelybackend.entity.response;

import com.hcmus.lovelybackend.entity.common.ResponseHeader;
import com.hcmus.lovelybackend.entity.dao.UserDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserResponse {
    private ResponseHeader responseHeader;
    private UserDAO user;
}

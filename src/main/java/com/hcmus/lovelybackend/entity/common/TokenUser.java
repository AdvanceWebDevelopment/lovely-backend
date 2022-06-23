package com.hcmus.lovelybackend.entity.common;

import com.hcmus.lovelybackend.entity.dao.UserDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenUser {
    private String token;
    private UserDAO userDAO;
}

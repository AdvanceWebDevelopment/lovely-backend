package com.hcmus.lovelybackend.entity.response;

import com.hcmus.lovelybackend.entity.common.ResponseHeader;
import com.hcmus.lovelybackend.entity.dao.Transaction;
import com.hcmus.lovelybackend.entity.dao.UserDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuEvaluationResponse {
    private ResponseHeader responseHeader;
    private UserDAO user;
    private Page<Transaction> evaluation;

    public AuEvaluationResponse(String newToken, UserDAO user, Page<Transaction> evaluation) {
        this.responseHeader = new ResponseHeader(newToken, "Bearer ");
        this.user = user;
        this.evaluation = evaluation;
    }
}

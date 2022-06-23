package com.hcmus.lovelybackend.entity.response;

import com.hcmus.lovelybackend.entity.common.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuListResponse {
    private ResponseHeader responseHeader;
    private List<?> responseBody;

    public AuListResponse(String newToken, List<?> responseBody){
        this.responseHeader = new ResponseHeader(newToken, "Bearer ");
        this.responseBody = responseBody;
    }
}

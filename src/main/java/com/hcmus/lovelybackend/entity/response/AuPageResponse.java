package com.hcmus.lovelybackend.entity.response;

import com.hcmus.lovelybackend.entity.common.ResponseHeader;
import com.hcmus.lovelybackend.entity.dao.Product;
import com.hcmus.lovelybackend.entity.dao.WatchList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuPageResponse {
    private ResponseHeader responseHeader;
    private Page<?> responseBody;

    public AuPageResponse(String newToken, Page<?> responseBody){
        this.responseHeader = new ResponseHeader(newToken, "Bearer ");
        this.responseBody = responseBody;
    }
}

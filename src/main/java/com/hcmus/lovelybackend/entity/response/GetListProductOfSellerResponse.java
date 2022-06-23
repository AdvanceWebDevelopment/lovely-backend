package com.hcmus.lovelybackend.entity.response;

import com.hcmus.lovelybackend.entity.common.ResponseHeader;
import com.hcmus.lovelybackend.entity.dao.Product;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class GetListProductOfSellerResponse {
    private ResponseHeader responseHeader;
    private Page<Product> responseBody;
    public GetListProductOfSellerResponse(String newToken, Page<Product> products){
        this.responseHeader = new ResponseHeader(newToken, "Bearer ");
        this.responseBody = products;
    }
}

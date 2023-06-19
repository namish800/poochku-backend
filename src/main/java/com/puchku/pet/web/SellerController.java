package com.puchku.pet.web;

import com.puchku.pet.model.SellerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellerController implements com.puchku.pet.api.SellerApi {
    @Override
    public ResponseEntity<SellerDto> createSellerAccount(SellerDto sellerDto) {
        return null;
    }

    @Override
    public ResponseEntity<SellerDto> requestSellerInfo(Integer sellerId) {
        return null;
    }
}

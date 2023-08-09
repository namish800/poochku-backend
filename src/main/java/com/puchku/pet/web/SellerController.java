package com.puchku.pet.web;

import com.puchku.pet.model.SellerDto;
import com.puchku.pet.api.SellerApi;
import com.puchku.pet.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellerController implements SellerApi {

    @Autowired
    private SellerService sellerService;

    @Override
    public ResponseEntity<SellerDto> createSellerAccount(SellerDto sellerDto) {

        return sellerService.createSellerAccount(sellerDto);
    }

    @Override
    public ResponseEntity<SellerDto> requestSellerInfo(Integer sellerId) {
        return sellerService.getSellerDetails(sellerId);
    }
}

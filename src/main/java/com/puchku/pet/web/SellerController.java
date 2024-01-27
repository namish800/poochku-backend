package com.puchku.pet.web;

import com.puchku.pet.model.GetRatingsForSeller200Response;
import com.puchku.pet.model.RatingDto;
import com.puchku.pet.model.RatingListDto;
import com.puchku.pet.model.RatingResponseDto;
import com.puchku.pet.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellerController implements com.puchku.pet.api.SellerApi {

    @Autowired
    private SellerService sellerService;

    @Override
    public ResponseEntity<RatingResponseDto> addRating(Long sellerId, RatingDto ratingDto) {
        return sellerService.addRating(sellerId, ratingDto);
    }

    @Override
    public ResponseEntity<RatingListDto> getRatingsForSeller(Long sellerId) {
        return sellerService.getRatings(sellerId);
    }


}

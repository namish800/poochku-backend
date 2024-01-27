package com.puchku.pet.service;

import com.puchku.pet.exceptions.BadRequestException;
import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.RatingDto;
import com.puchku.pet.model.RatingListDto;
import com.puchku.pet.model.RatingResponseDto;
import com.puchku.pet.model.entities.SellerEntity;
import com.puchku.pet.model.entities.SellerRatingEntity;
import com.puchku.pet.repository.SellerRatingRepository;
import com.puchku.pet.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SellerRatingRepository sellerRatingRepository;

    public ResponseEntity<RatingResponseDto> addRating(Long sellerId, RatingDto ratingDto) {
        if(sellerId==null || sellerId==0){
            throw new BadRequestException("Seller id is mandatory");
        }
        if(ratingDto == null){
            throw new BadRequestException("Rating Dto cannot be null");
        }

        if(ratingDto.getUserId()==null || ratingDto.getUserId()==0){
            throw new BadRequestException("Rater id is mandatory");
        }

        Optional<SellerEntity> sellerOpt = sellerRepository.findBySellerId(sellerId);
        if(sellerOpt.isEmpty()) {
            throw new NotFoundException("Seller not found");
        }
        Optional<SellerEntity> raterOpt = sellerRepository.findBySellerId(ratingDto.getUserId());
        if(raterOpt.isEmpty()) {
            throw new NotFoundException("Rater not found");
        }
        SellerEntity seller = sellerOpt.get();
        SellerEntity rater = raterOpt.get();

        SellerRatingEntity sellerRatingEntity = new SellerRatingEntity();
        sellerRatingEntity.setSeller(seller);
        sellerRatingEntity.setRater(rater);
        sellerRatingEntity.setRating(ratingDto.getRating());
        sellerRatingEntity.setComment(ratingDto.getComment());

        sellerRatingEntity = sellerRatingRepository.save(sellerRatingEntity);
        RatingResponseDto ratingResponseDto = convertSellerRatingEntityToRatingResponseDto(sellerRatingEntity);

        return new ResponseEntity<>(ratingResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<RatingListDto> getRatings(Long sellerId) {
        RatingListDto response = new RatingListDto();
        if(sellerId==null || sellerId==0){
            throw new BadRequestException("Seller id is mandatory");
        }
        Optional<SellerEntity> sellerOpt = sellerRepository.findBySellerId(sellerId);
        if(sellerOpt.isEmpty()) {
            throw new NotFoundException("Seller not found");
        }
        SellerEntity seller = sellerOpt.get();

        List<SellerRatingEntity> sellerRatingEntityList = seller.getSellerRatingEntityList();

        List<RatingResponseDto> ratingList = sellerRatingEntityList.stream()
                .map(this::convertSellerRatingEntityToRatingResponseDto)
                .toList();

        response.setRatings(ratingList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private RatingResponseDto convertSellerRatingEntityToRatingResponseDto(SellerRatingEntity sellerRating) {
        RatingResponseDto ratingDto = new RatingResponseDto();
        ratingDto.setRating(sellerRating.getRating());
        ratingDto.setComment(sellerRating.getComment());
        ratingDto.setUserId(sellerRating.getRater().getSellerId());
        ratingDto.setUserName(sellerRating.getRater().getfName());
        return ratingDto;
    }
}

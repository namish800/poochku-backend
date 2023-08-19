package com.puchku.pet.service;

import com.puchku.pet.enums.ServiceCode;
import com.puchku.pet.enums.SwipeDirection;
import com.puchku.pet.enums.SwipeStatus;
import com.puchku.pet.exceptions.BadRequestException;
import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.*;
import com.puchku.pet.model.PetSwipeDto;
import com.puchku.pet.model.SwipeRequestDto;
import com.puchku.pet.model.SwipeResponseDto;
import com.puchku.pet.model.SwipeResponseDtoMatch;
import com.puchku.pet.model.UserSwipeResponseDto;
import com.puchku.pet.model.entities.PetEntity;
import com.puchku.pet.model.entities.SwipeEntity;
import com.puchku.pet.model.projections.SwipeProjection;
import com.puchku.pet.model.projections.SwipeProjectionForTarget;
import com.puchku.pet.repository.PetRepository;
import com.puchku.pet.repository.SwipeRepository;
import com.puchku.pet.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SwipeService {
    @Autowired
    private SwipeRepository swipeRepository;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public ResponseEntity<SwipeResponseDto> recordSwipeAndMatch(SwipeRequestDto swipeRequestDto) {
        if(swipeRequestDto!=null && (swipeRequestDto.getSwiperId()==null || swipeRequestDto.getTargetId()==null)){
            throw new BadRequestException("Missing swiper or target id");
        }
        SwipeResponseDto response = new SwipeResponseDto();

        //assuming user will be there as only a logged-in user can use this functionality
        Optional<PetEntity> swiper = petRepository.findByPetIdAndService_serviceCode(swipeRequestDto.getSwiperId(), ServiceCode.MATING.getCode());
        Optional<PetEntity> target = petRepository.findByPetIdAndService_serviceCode(swipeRequestDto.getTargetId(), ServiceCode.MATING.getCode());

        if(swiper.isEmpty()){
            throw new NotFoundException("Swiper pet does not exist");
        }

        if(target.isEmpty()){
            throw new NotFoundException("Target pet does not exist");
        }

        SwipeEntity swiperSwipeEntity = new SwipeEntity(swiper.get(), target.get(), SwipeDirection.RIGHT.name(), SwipeStatus.PENDING.name());
        swiperSwipeEntity = swipeRepository.save(swiperSwipeEntity);

        Optional<SwipeEntity> targetSwipeEntityOpt = swipeRepository.findBySwiper_petIdAndTarget_petIdAndDirectionAndStatus(target.get().getPetId(), swiper.get().getPetId(), SwipeDirection.RIGHT.name(), SwipeStatus.PENDING.name());
        SwipeEntity targetSwipeEntity = null;

        //match exists if targetSwipeEntity exists
        if(targetSwipeEntityOpt.isPresent()){
            swiperSwipeEntity.setStatus(SwipeStatus.ACCEPTED.name());
            targetSwipeEntity = targetSwipeEntityOpt.get();
            targetSwipeEntity.setStatus(SwipeStatus.ACCEPTED.name());

            swiperSwipeEntity = swipeRepository.save(swiperSwipeEntity);
            swipeRepository.save(targetSwipeEntity);

            SwipeResponseDtoMatch match = new SwipeResponseDtoMatch();
            match.setMatchSwipeId(swiperSwipeEntity.getSwipeId());
            match.setTargetId(target.get().getPetId());
            match.setSwiperId(swiper.get().getPetId());

            response.setIsMatch(true);
            response.setMatch(match);
        }

        if(response.getIsMatch()!=null && response.getIsMatch()){
            response.setMessage("Matched!");
        } else {
            response.setMessage("Swipe action recorded successfully");
            response.setIsMatch(false);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<UserSwipeResponseDto> getSwipesList(Integer petId) {
        if(petId==null || petId==0){
            throw new BadRequestException("Invalid pet Id");
        }
        List<SwipeEntity> acceptedSwipes = swipeRepository.findBySwiper_petIdAndStatus(petId, SwipeStatus.ACCEPTED.name());
        List<SwipeEntity> pendingFromTarget = swipeRepository.findBySwiper_petIdAndStatus(petId, SwipeStatus.PENDING.name());
        List<SwipeEntity> pendingFromSwiper = swipeRepository.findByTarget_petIdAndStatus(petId, SwipeStatus.PENDING.name());

        UserSwipeResponseDto response = new UserSwipeResponseDto();
        response.setAccepted(acceptedSwipes.stream().map(e -> mapSwipeEntityToPetSwipes(e, true)).collect(Collectors.toList()));
        response.setPendingFromTarget(pendingFromTarget.stream().map(e -> mapSwipeEntityToPetSwipes(e, true)).collect(Collectors.toList()));
        response.setPendingFromSwiper(pendingFromSwiper.stream().map(e -> mapSwipeEntityToPetSwipes(e, false)).collect(Collectors.toList()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private PetSwipeDto mapSwipeEntityToPetSwipes(SwipeEntity swipe, boolean flag) {
        PetSwipeDto petSwipeDto = new PetSwipeDto();
        petSwipeDto.setSwipeId(swipe.getSwipeId());

        if(flag){
            petSwipeDto.setPetDetails(CommonUtils.mapPetEntitytoPetResponse(swipe.getTarget()));
        } else {
            petSwipeDto.setPetDetails(CommonUtils.mapPetEntitytoPetResponse(swipe.getSwiper()));
        }

        return petSwipeDto;
    }


}

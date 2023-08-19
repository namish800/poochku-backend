package com.puchku.pet.web;

import com.puchku.pet.model.SwipeRequestDto;
import com.puchku.pet.model.SwipeResponseDto;
import com.puchku.pet.model.UserSwipeResponseDto;
import com.puchku.pet.service.SwipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwipeController implements com.puchku.pet.api.SwipeApi {

    @Autowired
    private SwipeService swipeService;

    @Override
    public ResponseEntity<UserSwipeResponseDto> getSwipesList(Integer petId) {
        return swipeService.getSwipesList(petId);
    }

    @Override
    public ResponseEntity<SwipeResponseDto> swipePost(SwipeRequestDto swipeRequestDto) {
        return swipeService.recordSwipeAndMatch(swipeRequestDto);
    }


}

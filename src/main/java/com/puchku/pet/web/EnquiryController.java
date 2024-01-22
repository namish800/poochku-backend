package com.puchku.pet.web;

import com.puchku.pet.enums.EnquiryType;
import com.puchku.pet.service.EnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnquiryController implements com.puchku.pet.api.EnquiryApi {

    @Autowired
    private EnquiryService enquiryService;


    @Override
    public ResponseEntity<String> saveBestPriceEnquiry(String userId, String petId) {
        return enquiryService.addNewEnquiry(userId, petId, EnquiryType.GET_BEST_PRICE);
    }

    @Override
    public ResponseEntity<String> saveFindMeAPetEnquiry(String userId) {
        return enquiryService.addNewEnquiry(userId, EnquiryType.FINDPET);
    }

    @Override
    public ResponseEntity<String> saveSeeMoreEnquiry(String userId, String petId) {
        return enquiryService.addNewEnquiry(userId, petId, EnquiryType.SEE_MORE);
    }

    @Override
    public ResponseEntity<String> saveWhatsappEnquiry(String userId, String petId) {
        return enquiryService.addNewEnquiry(userId, petId, EnquiryType.WHATSAPP);
    }

}

package com.puchku.pet.util;

import com.puchku.pet.enums.EnquiryType;
import com.puchku.pet.model.Pet;
import com.puchku.pet.model.PetService;
import com.puchku.pet.model.PetStatistics;
import com.puchku.pet.model.UserDto;
import com.puchku.pet.model.entities.EnquiryEntity;
import com.puchku.pet.model.entities.PetEntity;
import com.puchku.pet.model.entities.PetServiceEntity;
import com.puchku.pet.model.entities.SellerEntity;

import java.util.List;

public class CommonUtils {
    public static String createWhatsAppUrl(String phoneNumber) {
        // Remove any non-digit characters from the phone number
        String formattedPhoneNumber = phoneNumber.replaceAll("\\D", "");

        // Create the WhatsApp URL
        String whatsappUrl = "https://api.whatsapp.com/send?phone=" + formattedPhoneNumber;

        return whatsappUrl;
    }

    public static Pet mapPetEntitytoPetResponse(PetEntity petEntity) {
        Pet petResponse = new Pet();
        petResponse.setPetId(petEntity.getPetId());
        petResponse.setAge(petEntity.getAge());
        petResponse.setPetType(petEntity.getPetType());
        petResponse.setName(petResponse.getName());
        petResponse.setBreed(petEntity.getBreed());
        petResponse.setDescription(petEntity.getDescription());
        petResponse.setStatus(petEntity.getPetStatus());
        petResponse.setFatherBreed(petEntity.getFatherBreed());
        petResponse.setMotherBreed(petEntity.getMotherBreed());
        petResponse.setLocation(petEntity.getLocation());
        petResponse.setQuality(petEntity.getQuality());
        petResponse.setGender(petEntity.getGender());
        UserDto owner = mapSellerEntitytoUserDto(petEntity.getSeller());
        petResponse.setOwner(owner);
        petResponse.setService(mapPetServiceEntityToPetService(petEntity.getService()));
        if(petEntity.getPetImageEntity()!=null) {
            petResponse.setImageUrls(petEntity.getPetImageEntity().getImageUrls());
        }

        PetStatistics petStatistics = new PetStatistics();
        List<EnquiryEntity> enquiryEntityList = petEntity.getPetEnquiries();
        int whatsAppCount = 0;
        int viewCount = 0;
        if(!enquiryEntityList.isEmpty()){
            whatsAppCount = enquiryEntityList.stream()
                    .filter(enquiry -> EnquiryType.WHATSAPP.name().equalsIgnoreCase(enquiry.getEnquiryType()))
                    .toList().size();

            viewCount = enquiryEntityList.stream()
                    .filter(enquiry -> EnquiryType.SEE_MORE.name().equalsIgnoreCase(enquiry.getEnquiryType()))
                    .toList().size();
        }
        petStatistics.setViewCount(viewCount);
        petStatistics.setWhatsappCount(whatsAppCount);
        petResponse.setStatistics(petStatistics);
        return petResponse;
    }

    public static PetService mapPetServiceEntityToPetService(PetServiceEntity petServiceEntity) {
        PetService petService = new PetService();
        petService.setServiceId(petServiceEntity.getPetServiceId());
        petService.setServiceCode(petServiceEntity.getServiceCode());
        petService.setServiceName(petServiceEntity.getServiceName());
        petService.setPrice(petServiceEntity.getPrice());
        return petService;
    }

    public static UserDto mapSellerEntitytoUserDto(SellerEntity sellerEntity) {
        UserDto userDto = new UserDto();
        userDto.setUserId(sellerEntity.getSellerId());
        userDto.setfName(sellerEntity.getfName());
        userDto.setlName(sellerEntity.getlName());
        userDto.setEmail(sellerEntity.getEmail());
        userDto.setPhoneNo(sellerEntity.getPhoneNo());
        userDto.setWhatsappUrl(CommonUtils.createWhatsAppUrl(sellerEntity.getPhoneNo()));
        return userDto;
    }

}

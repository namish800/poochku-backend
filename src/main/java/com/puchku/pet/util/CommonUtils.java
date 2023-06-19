package com.puchku.pet.util;

public class CommonUtils {
    public String createWhatsAppUrl(String phoneNumber) {
        // Remove any non-digit characters from the phone number
        String formattedPhoneNumber = phoneNumber.replaceAll("\\D", "");

        // Create the WhatsApp URL
        String whatsappUrl = "https://api.whatsapp.com/send?phone=" + formattedPhoneNumber;

        return whatsappUrl;
    }
}

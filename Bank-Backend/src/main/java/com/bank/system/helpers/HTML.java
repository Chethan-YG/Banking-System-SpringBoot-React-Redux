package com.bank.system.helpers;

public class HTML {

    public static String htmlEmailTemplate(String token, String code) {
        String url = "http://localhost:8070/api/auth/verify?token=" + token + "&code=" + code;
        String emailTemplate = "<html><body>" +
                "<h1>Welcome!</h1>" +
                "<p>Please click the link below to verify your account:</p>" +
                "<a href='" + url + "'>Verify Your Account</a>" +
                "</body></html>";
        return emailTemplate;
    }
}

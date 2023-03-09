package com.spring.project.Models;

import com.spring.project.Service.Email.AbstractEmailContext;
import org.springframework.web.util.UriComponentsBuilder;

public class ForgotPasswordEmailContext extends AbstractEmailContext {
    private String token;

    @Override
    public <T> void init(T context) {

        //we can do any common configuration setup here
        // like setting up some base URL and context
        UserEntity customer = (UserEntity) context; // we pass the customer informati
        put("firstName", customer.getFirstName());
        setTemplateLocation("account/forgotpassword");
        setSubject("Forgotten Password");
        setFrom("masghouniamal84@gmail.com");
        setTo(customer.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/password/change").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }
}

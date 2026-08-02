package com.homebase.homebase.client;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailClient {
    private final Resend resend;
    private final String alertRecipient;

    public EmailClient(
            @Value("${resend.api-key}") String apiKey,
            @Value("${resend.alert-recipient}") String alertRecipient
    ) {
        this.resend = new Resend(apiKey);
        this.alertRecipient = alertRecipient;
    }

    public void sendAlertEmail(String subject, String htmlBody) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Homebase <onboarding@resend.dev>")
                .to(alertRecipient)
                .subject(subject)
                .html(htmlBody)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);
        } catch (ResendException resendException) {
            throw new RuntimeException("Failed to send alert email via Resend",  resendException);
        }
    }
}

package com.superwallet.services;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MailJetServiceImpl {

    private final MailjetClient mailjetClient;

    public MailJetServiceImpl( MailjetClient mailjetClient) {
        this.mailjetClient = mailjetClient;
    }

    public void sendEmail(
            String toEmail,
            String toName,
            String subject,
            String textContent,
            String htmlContent) {
        try {
            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put("From", new JSONObject()
                                            .put("Email", "super-wallet@abv.bg")
                                            .put("Name", "Supper-Wallet"))
                                    .put("To", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", toEmail)
                                                    .put("Name", toName)))
                                    .put("Subject", subject)
                                    .put("TextPart", textContent)
                                    .put("HTMLPart", htmlContent)));

            MailjetResponse response = mailjetClient.post(request);
            System.out.println("Response Status: " + response.getStatus());
            System.out.println("Response Data: " + response.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

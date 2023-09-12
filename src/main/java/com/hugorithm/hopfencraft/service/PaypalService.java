package com.hugorithm.hopfencraft.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PaypalService {
    private final APIContext apiContext;
    private final static Logger LOGGER = LoggerFactory.getLogger(PaypalService.class);

    public String createPayment(String total, String currency, String method, String intent, String description, String successUrl, String cancelUrl) {
        Amount amount = new Amount(currency, total);

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        Payment payment = new Payment();
        payment.setIntent(intent);
        Payer payer = new Payer();
        payer.setPaymentMethod(method);
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(transaction));
        payment.setRedirectUrls(redirectUrls);

        try {
            Payment createdPayment = payment.create(apiContext);
            for (Links link : createdPayment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }

    public String executePayment(String paymentId, String payerId) {
        try {
            Payment payment = Payment.get(apiContext, paymentId);
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);
            Payment executedPayment = payment.execute(apiContext, paymentExecution);
            return "Payment executed successfully. Payment ID: " + executedPayment.getId();
        } catch (PayPalRESTException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return "Payment execution failed.";
        }
    }


}

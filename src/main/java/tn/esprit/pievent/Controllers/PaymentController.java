package tn.esprit.pievent.Controllers;


import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {


    @PostMapping("/create-checkout-session")
    public Map<String, Object> createCheckoutSession() throws StripeException {
        // 1 TND = 0.303 EUR (exemple)
        double TND_TO_EUR = 1.0 / 3.3;
        double montantTND = 100.0; // Montant affiché au client (en TND)
        double montantEUR = montantTND * TND_TO_EUR;
        long montantEnCentimes = (long) (montantEUR * 100); // Stripe attend les montants en centimes

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.ALIPAY)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/piEvent/payment/success")
                .setCancelUrl("http://localhost:8080/piEvent/payment/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur") // Paiement en EUR
                                                .setUnitAmount(montantEnCentimes) // 💶 Montant converti en centimes
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Réservation événement (100 TND)")
                                                                .build()
                                                )
                                                .build()
                                )
                                .setQuantity(1L)
                                .build()
                )
                .build();

        Session session = Session.create(params);
        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", session.getId());
        return ResponseEntity.ok(result).getBody();
    }


    @GetMapping("/success")
    public String getSuccess(){
        return "payment successful";
    }

    @GetMapping("/cancel")
    public String cancel(){
        return "payment canceled";
    }
}




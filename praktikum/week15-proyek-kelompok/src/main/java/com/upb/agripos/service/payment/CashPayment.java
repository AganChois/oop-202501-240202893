package com.upb.agripos.service.payment;

import com.upb.agripos.model.PaymentResult;

public class CashPayment implements PaymentStrategy {
    public PaymentResult pay(double total, double bayar) {
        if (bayar < total)
            return new PaymentResult(false, "Uang kurang");
        return new PaymentResult(true, "Kembalian: " + (bayar - total));
    }
}

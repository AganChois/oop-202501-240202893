package com.upb.agripos.service.payment;

import com.upb.agripos.model.PaymentResult;

public class EWalletPayment implements PaymentStrategy {
    public PaymentResult pay(double total, double bayar) {
        return new PaymentResult(true, "E-Wallet sukses");
    }
}


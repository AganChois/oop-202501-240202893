package com.upb.agripos.service.payment;

import com.upb.agripos.model.PaymentResult;

public interface PaymentStrategy {
    PaymentResult pay(double total, double bayar);
}


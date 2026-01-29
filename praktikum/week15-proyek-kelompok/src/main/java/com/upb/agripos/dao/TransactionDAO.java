package com.upb.agripos.dao;

import com.upb.agripos.model.Transaction;

public interface TransactionDAO {
    void save(Transaction transaction);
}

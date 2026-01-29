package com.upb.agripos.dao;

import java.util.List;

import com.upb.agripos.model.Promo;

public interface PromoDAO {
    void save(Promo promo);
    Promo findByCode(String code);
    List<Promo> findAll();
    void update(Promo promo);
    void delete(String code);
}

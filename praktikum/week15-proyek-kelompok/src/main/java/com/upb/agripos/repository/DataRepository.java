package com.upb.agripos.repository;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.ProductDAOImpl;
import com.upb.agripos.dao.PromoDAO;
import com.upb.agripos.dao.PromoDAOImpl;
import com.upb.agripos.model.Product;
import com.upb.agripos.model.Promo;
import com.upb.agripos.model.Transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Repository terpusat untuk menyimpan data Produk, Promo, dan Transaksi
 * Digunakan oleh AdminView dan KasirView agar data sinkron
 * Sekarang menggunakan PostgreSQL database
 */
public class DataRepository {
    private static DataRepository instance;
    
    private final ObservableList<Product> products;
    private final ObservableList<Promo> promos;
    private final ObservableList<Transaction> transactions;
    
    private final ProductDAO productDAO;
    private final PromoDAO promoDAO;
    
    private DataRepository() {
        // Initialize DAO
        this.productDAO = new ProductDAOImpl();
        this.promoDAO = new PromoDAOImpl();
        
        // Load data dari database
        this.products = FXCollections.observableArrayList(productDAO.findAll());
        this.promos = FXCollections.observableArrayList(promoDAO.findAll());
        this.transactions = FXCollections.observableArrayList();
    }
    
    /**
     * Mendapatkan instance singleton dari DataRepository
     */
    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }
    
    /**
     * Mendapatkan ObservableList produk (shared)
     */
    public ObservableList<Product> getProducts() {
        return products;
    }
    
    /**
     * Mendapatkan ObservableList promo (shared)
     */
    public ObservableList<Promo> getPromos() {
        return promos;
    }
    
    /**
     * Mendapatkan ObservableList transaksi (shared)
     */
    public ObservableList<Transaction> getTransactions() {
        return transactions;
    }
    
    /**
     * Menambahkan produk baru
     */
    public void addProduct(Product product) {
        productDAO.save(product);
        products.add(product);
    }
    
    /**
     * Menghapus produk
     */
    public void removeProduct(Product product) {
        productDAO.delete(product.getId());
        products.remove(product);
    }
    
    /**
     * Menambahkan promo baru
     */
    public void addPromo(Promo promo) {
        promoDAO.save(promo);
        promos.add(promo);
    }
    
    /**
     * Menghapus promo
     */
    public void removePromo(Promo promo) {
        promoDAO.delete(promo.getCode());
        promos.remove(promo);
    }
    
    /**
     * Menambahkan transaksi baru
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    
    /**
     * Update produk
     */
    public void updateProduct(int index, Product product) {
        if (index >= 0 && index < products.size()) {
            productDAO.update(product);
            products.set(index, product);
        }
    }
    
    /**
     * Update promo
     */
    public void updatePromo(int index, Promo promo) {
        if (index >= 0 && index < promos.size()) {
            promoDAO.update(promo);
            promos.set(index, promo);
        }
    }
    
    /**
     * Reload data dari database
     */
    public void reloadProducts() {
        products.clear();
        products.addAll(productDAO.findAll());
    }
    
    /**
     * Reload promos dari database
     */
    public void reloadPromos() {
        promos.clear();
        promos.addAll(promoDAO.findAll());
    }
}

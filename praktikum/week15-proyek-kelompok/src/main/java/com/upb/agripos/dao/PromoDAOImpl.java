package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.upb.agripos.model.Promo;

public class PromoDAOImpl implements PromoDAO {

    @Override
    public void save(Promo promo) {
        String sql = "INSERT INTO promos (code, name, discount, start_date, end_date, active) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, promo.getCode());
            stmt.setString(2, promo.getName());
            stmt.setDouble(3, promo.getDiscount());
            stmt.setDate(4, java.sql.Date.valueOf(promo.getStartDate()));
            stmt.setDate(5, java.sql.Date.valueOf(promo.getEndDate()));
            stmt.setBoolean(6, promo.isActive());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Promo findByCode(String code) {
        String sql = "SELECT * FROM promos WHERE code = ?";
        
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Promo(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("discount"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate()
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public List<Promo> findAll() {
        List<Promo> promos = new ArrayList<>();
        String sql = "SELECT * FROM promos ORDER BY code";
        
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Promo promo = new Promo(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("discount"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate()
                );
                promos.add(promo);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return promos;
    }

    @Override
    public void update(Promo promo) {
        String sql = "UPDATE promos SET name = ?, discount = ?, start_date = ?, end_date = ?, active = ? WHERE code = ?";
        
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, promo.getName());
            stmt.setDouble(2, promo.getDiscount());
            stmt.setDate(3, java.sql.Date.valueOf(promo.getStartDate()));
            stmt.setDate(4, java.sql.Date.valueOf(promo.getEndDate()));
            stmt.setBoolean(5, promo.isActive());
            stmt.setString(6, promo.getCode());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String code) {
        String sql = "DELETE FROM promos WHERE code = ?";
        
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, code);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.example.demo1.dao;

import com.example.demo1.db.Database;
import com.example.demo1.model.Charity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CharityDao {

    public List<Charity> searchByInterests(String interests) throws SQLException {
        String[] tokens = tokenize(interests);
        if (tokens.length == 0) return new ArrayList<>();

        StringBuilder where = new StringBuilder();
        List<String> params = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            if (i > 0) where.append(" OR ");
            where.append("(category LIKE ? OR description LIKE ?)");
            String like = "%" + tokens[i] + "%";
            params.add(like);
            params.add(like);
        }

        String sql = "SELECT id, name, category, description FROM charities WHERE " + where + " ORDER BY name";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Charity> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
                return out;
            }
        }
    }

    public Charity findByName(String name) throws SQLException {
        String sql = "SELECT id, name, category, description FROM charities WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        }
    }

    public List<Charity> findAll() throws SQLException {
        String sql = "SELECT id, name, category, description FROM charities";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Charity> out = new ArrayList<>();
            while (rs.next()) out.add(mapRow(rs));
            return out;
        }
    }

    private Charity mapRow(ResultSet rs) throws SQLException {
        return new Charity(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("description")
        );
    }

    private String[] tokenize(String s) {
        if (s == null) return new String[0];
        String[] raw = s.split(",");
        List<String> tokens = new ArrayList<>();
        for (String r : raw) {
            String t = r.trim();
            if (!t.isEmpty()) tokens.add(t);
        }
        if (tokens.isEmpty()) {
            for (String w : s.split("\\s+")) {
                String t = w.trim();
                if (!t.isEmpty()) tokens.add(t);
            }
        }
        return tokens.toArray(new String[0]);
    }
}



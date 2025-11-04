package com.example.demo1.util;

import com.example.demo1.db.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CsvImporter {

    // Minimal CSV importer for columns: category,description,name
    public static void importCharities(String csvPath) throws Exception {
        try (Connection conn = Database.getConnection()) {
            ensureTable(conn);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath, StandardCharsets.UTF_8));
             Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);
            String line;
            boolean first = true;
            String sql = "INSERT IGNORE INTO charities(category, description, name) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                while ((line = br.readLine()) != null) {
                    if (first) { first = false; continue; } // skip header
                    String[] cols = parseCsvLine(line);
                    if (cols.length < 3) continue;
                    String category = cols[0].trim();
                    String description = cols[1].trim();
                    String name = cols[2].trim();
                    if (name.isEmpty() || description.isEmpty()) continue;
                    ps.setString(1, category);
                    ps.setString(2, description);
                    ps.setString(3, name);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
        }
    }

    private static void ensureTable(Connection conn) throws SQLException {
        String ddl = "CREATE TABLE IF NOT EXISTS charities (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "category VARCHAR(255) NOT NULL," +
                "description TEXT NOT NULL," +
                "name VARCHAR(512) NOT NULL UNIQUE," +
                "FULLTEXT KEY ft_cat_desc (category, description)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        try (PreparedStatement ps = conn.prepareStatement(ddl)) { ps.execute(); }
    }

    // Basic CSV parser supporting quotes; not fully RFC-compliant but adequate for typical data
    private static String[] parseCsvLine(String line) {
        java.util.List<String> out = new java.util.ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++; // skip escaped quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(ch);
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: CsvImporter <path-to-CharityData_NonProfit_cleaned_FINAL.csv>");
            return;
        }
        importCharities(args[0]);
        System.out.println("CSV import complete.");
    }
}



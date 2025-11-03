package com.example.demo1.service;

import com.example.demo1.dao.CharityDao;
import com.example.demo1.model.Charity;

import java.sql.SQLException;
import java.util.*;

public class CharityRecommenderService {

    private final CharityDao charityDao;

    public CharityRecommenderService() {
        this.charityDao = new CharityDao();
    }

    public List<Charity> getCharitiesByInterests(String interests) throws SQLException {
        return charityDao.searchByInterests(interests);
    }

    public List<ScoredCharity> getSimilarCharities(String charityName, int topK) throws SQLException {
        Charity target = charityDao.findByName(charityName);
        if (target == null) return Collections.emptyList();

        List<Charity> all = charityDao.findAll();
        if (all.isEmpty()) return Collections.emptyList();

        Map<Integer, double[]> vectors = buildTfidfVectors(all);
        double[] queryVec = vectors.get(target.getId());
        if (queryVec == null) return Collections.emptyList();

        PriorityQueue<ScoredCharity> pq = new PriorityQueue<>(Comparator.comparingDouble(ScoredCharity::score));
        for (Charity c : all) {
            if (c.getId() == target.getId()) continue;
            double[] v = vectors.get(c.getId());
            if (v == null) continue;
            double sim = cosine(queryVec, v);
            pq.offer(new ScoredCharity(c, sim));
            if (pq.size() > topK) pq.poll();
        }

        List<ScoredCharity> out = new ArrayList<>(pq);
        out.sort((a, b) -> Double.compare(b.score, a.score));
        return out;
    }

    public static class ScoredCharity {
        private final Charity charity;
        private final double score;
        public ScoredCharity(Charity charity, double score) { this.charity = charity; this.score = score; }
        public Charity charity() { return charity; }
        public double score() { return score; }
    }

    private Map<Integer, double[]> buildTfidfVectors(List<Charity> charities) {
        List<String[]> documents = new ArrayList<>();
        Map<Integer, Integer> idToIndex = new HashMap<>();
        for (int i = 0; i < charities.size(); i++) {
            Charity c = charities.get(i);
            idToIndex.put(c.getId(), i);
            String text = (c.getCategory() + " " + c.getDescription()).toLowerCase(Locale.ROOT);
            String[] tokens = tokenize(text);
            documents.add(tokens);
        }

        // Build vocabulary and DF
        Map<String, Integer> termIndex = new HashMap<>();
        Map<String, Integer> docFreq = new HashMap<>();
        for (String[] doc : documents) {
            Set<String> seen = new HashSet<>();
            for (String t : doc) {
                if (t.isEmpty()) continue;
                termIndex.computeIfAbsent(t, k -> termIndex.size());
                if (seen.add(t)) docFreq.merge(t, 1, Integer::sum);
            }
        }

        int vocab = termIndex.size();
        int nDocs = documents.size();
        double[] idf = new double[vocab];
        for (Map.Entry<String, Integer> e : termIndex.entrySet()) {
            int df = docFreq.getOrDefault(e.getKey(), 1);
            idf[e.getValue()] = Math.log((nDocs + 1.0) / (df + 1.0)) + 1.0; // smoothed IDF
        }

        Map<Integer, double[]> vectors = new HashMap<>();
        for (int i = 0; i < documents.size(); i++) {
            String[] doc = documents.get(i);
            Map<Integer, Integer> tfCounts = new HashMap<>();
            for (String t : doc) {
                Integer idx = termIndex.get(t);
                if (idx != null) tfCounts.merge(idx, 1, Integer::sum);
            }
            double[] vec = new double[vocab];
            double norm = 0.0;
            for (Map.Entry<Integer, Integer> e : tfCounts.entrySet()) {
                int idx = e.getKey();
                double tf = e.getValue();
                double val = (1.0 + Math.log(tf)) * idf[idx];
                vec[idx] = val;
                norm += val * val;
            }
            norm = Math.sqrt(norm);
            if (norm > 0) {
                for (int j = 0; j < vec.length; j++) vec[j] /= norm;
            }
            vectors.put(charities.get(i).getId(), vec);
        }

        return vectors;
    }

    private double cosine(double[] a, double[] b) {
        double dot = 0.0, na = 0.0, nb = 0.0;
        int len = Math.min(a.length, b.length);
        for (int i = 0; i < len; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        if (na == 0 || nb == 0) return 0.0;
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    private String[] tokenize(String text) {
        return text.replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim()
                .split(" ");
    }
}



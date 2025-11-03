package com.altruithm.backend.service;

import com.altruithm.backend.Entity.CharityBasic;
import com.altruithm.backend.repository.CharityBasicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    @Autowired
    private CharityBasicRepository charityBasicRepository;

    public List<CharityBasic> getSimilarCharities(String charityName) {
        Optional<CharityBasic> referenceOpt = charityBasicRepository.findByNameIgnoreCase(charityName);
        if (referenceOpt.isEmpty()) return new ArrayList<>();
        CharityBasic reference = referenceOpt.get();
        return charityBasicRepository.findByCategory(reference.getCategory())
                .stream()
                .filter(c -> c.getScore() != null && c.getScore() >= 75)
                .filter(c -> !c.getName().equalsIgnoreCase(charityName))
                .limit(5)
                .toList();
    }

    public List<CharityBasic> getCharitiesByInterests(String interests) {
        List<CharityBasic> results = new ArrayList<>();
        for (String category : interests.split(",")) {
            results.addAll(
                    charityBasicRepository.findByCategory(category.trim())
                            .stream()
                            .filter(c -> c.getScore() != null && c.getScore() >= 75)
                            .limit(3)
                            .toList()
            );
        }
        // Remove duplicates and limit total to 5
        return results.stream().distinct().limit(5).toList();
    }
}
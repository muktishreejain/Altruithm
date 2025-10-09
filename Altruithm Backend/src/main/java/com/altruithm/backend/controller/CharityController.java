package com.altruithm.backend.controller;

import com.altruithm.backend.model.Charity;
import com.altruithm.backend.repository.CharityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/charities")
@CrossOrigin(origins = "http://localhost:3000")
public class CharityController {

    @Autowired
    private CharityRepository charityRepository;

    @GetMapping
    public List<Charity> getAllCharities() {
        return charityRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Charity> getCharityById(@PathVariable Long id) {
        Optional<Charity> charity = charityRepository.findById(id);
        if (charity.isPresent()) {
            return ResponseEntity.ok(charity.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Charity createCharity(@RequestBody Charity charity) {
        return charityRepository.save(charity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Charity> updateCharity(@PathVariable Long id, @RequestBody Charity charityDetails) {
        Optional<Charity> optionalCharity = charityRepository.findById(id);
        if (optionalCharity.isPresent()) {
            Charity charity = optionalCharity.get();
            charity.setName(charityDetails.getName());
            charity.setDescription(charityDetails.getDescription());
            charity.setWebsite(charityDetails.getWebsite());
            charity.setContact(charityDetails.getContact());
            return ResponseEntity.ok(charityRepository.save(charity));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharity(@PathVariable Long id) {
        if (charityRepository.existsById(id)) {
            charityRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
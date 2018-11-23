package com.seiranyan.jsonpostgres.controllers;

import com.seiranyan.jsonpostgres.repositories.AreaRepository;
import com.seiranyan.jsonpostgres.repositories.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpecializationController {
    public static SpecializationRepository specializationRepository;

    @Autowired
    public SpecializationController(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

}

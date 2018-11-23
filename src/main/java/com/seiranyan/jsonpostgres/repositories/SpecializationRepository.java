package com.seiranyan.jsonpostgres.repositories;

import com.seiranyan.jsonpostgres.entities.Area;
import com.seiranyan.jsonpostgres.entities.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
}

package com.seiranyan.jsonpostgres.repositories;

import com.seiranyan.jsonpostgres.entities.Area;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long> {
}

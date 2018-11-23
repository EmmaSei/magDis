package com.seiranyan.jsonpostgres.repositories;

import com.seiranyan.jsonpostgres.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}

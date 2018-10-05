package com.seiranyan.jsonpostgres.repositories;

import com.seiranyan.jsonpostgres.entities.Vacancy;
import org.springframework.data.repository.CrudRepository;

public interface VacancyRepository extends CrudRepository <Vacancy, Long>{
}

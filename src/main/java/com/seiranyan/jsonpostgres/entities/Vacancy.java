package com.seiranyan.jsonpostgres.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Data
@Entity
@Table(schema = "json", name = "vacancy")
public class Vacancy{

    @Id
    private Long id;
    private String name;
    private String created_at;
    private Float salary_to;
    private Float salary_from;
    private String city;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_area", nullable = true)
    private Area area;

    private String experience;

    @ManyToMany
    @JoinTable(schema = "json", name = "vacancies_to_specs",
            joinColumns = {@JoinColumn(name = "id_vac")},
            inverseJoinColumns = @JoinColumn(name = "id_spec"))
    private Set<Specialization> specializations;
    private String description;

}

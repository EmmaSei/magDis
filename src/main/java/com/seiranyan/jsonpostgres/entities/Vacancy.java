package com.seiranyan.jsonpostgres.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(schema = "json", name = "vacancy")
public class Vacancy {

    @Id
    private Long id;
    private String name;
    private String created_at;
    private Float salary_to;
    private Float salary_from;
    private String area;
    private Boolean premium;

}

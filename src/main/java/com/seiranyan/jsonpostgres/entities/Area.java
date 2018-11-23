package com.seiranyan.jsonpostgres.entities;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(schema = "json", name = "area")
public class Area{
    @Id
    @Column(name="id")
    private Long id;
    private String name;
    @Column(nullable = true)
    private Long parent_id;

}

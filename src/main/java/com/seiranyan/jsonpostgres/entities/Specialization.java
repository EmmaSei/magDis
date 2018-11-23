package com.seiranyan.jsonpostgres.entities;


import lombok.Data;

import javax.persistence.*;

@Table(schema = "json")
@Embeddable
@Entity
@Data
public class Specialization {
    @Id
    @Column(name = "id")
    String id;
    String name;
}

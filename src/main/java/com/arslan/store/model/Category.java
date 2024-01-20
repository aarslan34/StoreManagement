package com.arslan.store.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "Category.getAllCategory", query = "select c from Category c") //where c.id in (select p.id from Product p where p.status='true' )")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "category")
public class Category implements Serializable {

    private static final long serialVersionID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

}

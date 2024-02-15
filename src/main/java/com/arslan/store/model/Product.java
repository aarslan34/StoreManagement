package com.arslan.store.model;

import com.arslan.store.wrapper.ProductWrapper;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;


@NamedQuery(name = "Product.getAllProducts", query = "select new com.arslan.store.wrapper.ProductWrapper(p.id, p.name,p.description, p.price, p.status, p.category.id, p.category.name ) from Product p ")

@NamedQuery(name = "Product.updateProductStatus", query = "update Product p set p.status=:status where p.id=:id")

@NamedQuery(name = "Product.getProductByCategory",query = "select new com.arslan.store.wrapper.ProductWrapper(p.id, p.name) from Product p where p.category.id=:id and p.status='true'")

@NamedQuery(name = "Product.getProductById", query = "select new com.arslan.store.wrapper.ProductWrapper(p.id, p.name, p.description, p.price) from Product p where p.id=:id")

//
//@NamedQuery(name = "Product.getProductByCategory", query = "select new com.arslan.store.wrapper.ProductWrapper(p.id, p.name) from Product p where p.category.id=:id and p.status='true'")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "product")
public class Product implements Serializable {

    public static final Long serialVersionUid = 123456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    // many products can have a single category
    @JoinColumn(name = "category_fk", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "status")
    private String status;







}

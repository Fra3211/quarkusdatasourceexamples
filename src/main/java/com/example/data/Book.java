package com.example.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
@Indexed
public class Book extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Integer id;

    public String name;

    @FullTextField(analyzer = "english")
    public String title;

    @ManyToOne
    @JsonbTransient //NON HO COSì IL LOOP
    public Author author;

    public Book() {
    }


}

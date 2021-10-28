package com.epam.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor

@Entity
public  class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String path;

    private  long size;

    private  String checksum;

//    @Enumerated(EnumType.STRING)
//    private  StorageType type;

    public Resource(String path, long size, String checksum) {
        this.path = path;
        this.size = size;
        this.checksum = checksum;
//        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return size == resource.size && Objects.equals(id, resource.id) && Objects.equals(path, resource.path) && Objects.equals(checksum, resource.checksum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, path, size, checksum);
    }


    public Resource() {

    }
}

package com.spaceflightsnews.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Launch {
    @Id
    private String id;
    private String provider;

    public Launch() { }
}

package br.edu.dmos5.github_dmos5.model;

import java.io.Serializable;

/**
 * Entity to Repository
 * @author vinicius.montouro
 */
public class Repository implements Serializable {

    private String name;

    public Repository(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

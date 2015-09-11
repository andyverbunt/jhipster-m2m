package com.m2m.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the A entity.
 */
public class ADTO implements Serializable {

    private Long id;

    private String nameA;
    private Set<BDTO> bs = new HashSet<>();

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameA() {
        return nameA;
    }

    public void setNameA(String nameA) {
        this.nameA = nameA;
    }

    public Set<BDTO> getBs() {
        return bs;
    }

    public void setBs(Set<BDTO> bs) {
        this.bs = bs;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ADTO aDTO = (ADTO) o;

        if ( ! Objects.equals(id, aDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ADTO{" +
                "id=" + id +
                ", nameA='" + nameA + "'" +
                '}';
    }
}

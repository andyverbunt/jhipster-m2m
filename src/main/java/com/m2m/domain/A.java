package com.m2m.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A A.
 */
@Entity
@Table(name = "A")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class A implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "name_a")
    private String nameA;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "A_B",
               joinColumns = @JoinColumn(name="as_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="bs_id", referencedColumnName="ID"))
    private Set<B> bs = new HashSet<>();

    @ManyToOne
    private User user;

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

    public Set<B> getBs() {
        return bs;
    }

    public void setBs(Set<B> bs) {
        this.bs = bs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        A a = (A) o;

        if ( ! Objects.equals(id, a.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "A{" +
                "id=" + id +
                ", nameA='" + nameA + "'" +
                '}';
    }
}

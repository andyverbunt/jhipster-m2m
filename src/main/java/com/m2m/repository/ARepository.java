package com.m2m.repository;

import com.m2m.domain.A;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the A entity.
 */
public interface ARepository extends JpaRepository<A,Long> {

    @Query("select a from A a where a.user.login = ?#{principal.username}")
    List<A> findByUserIsCurrentUser();

    @Query("select distinct a from A a left join fetch a.bs")
    List<A> findAllWithEagerRelationships();

    @Query("select a from A a left join fetch a.bs where a.id =:id")
    A findOneWithEagerRelationships(@Param("id") Long id);

}

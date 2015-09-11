package com.m2m.repository;

import com.m2m.domain.B;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the B entity.
 */
public interface BRepository extends JpaRepository<B,Long> {

    @Query("select b from B b where b.user.login = ?#{principal.username}")
    List<B> findByUserIsCurrentUser();

}

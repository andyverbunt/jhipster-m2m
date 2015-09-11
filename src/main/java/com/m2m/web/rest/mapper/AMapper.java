package com.m2m.web.rest.mapper;

import com.m2m.domain.*;
import com.m2m.web.rest.dto.ADTO;

import org.mapstruct.*;

/**
 * Mapper for the entity A and its DTO ADTO.
 */
@Mapper(componentModel = "spring", uses = {BMapper.class, })
public interface AMapper {

    @Mapping(source = "user.id", target = "userId")
    ADTO aToADTO(A a);

    @Mapping(source = "userId", target = "user")
    A aDTOToA(ADTO aDTO);

    default B bFromId(Long id) {
        if (id == null) {
            return null;
        }
        B b = new B();
        b.setId(id);
        return b;
    }

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}

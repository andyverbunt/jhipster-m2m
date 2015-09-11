package com.m2m.web.rest.mapper;

import com.m2m.domain.*;
import com.m2m.web.rest.dto.BDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity B and its DTO BDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BMapper {

    @Mapping(source = "user.id", target = "userId")
    BDTO bToBDTO(B b);

    @Mapping(target = "as", ignore = true)
    @Mapping(source = "userId", target = "user")
    B bDTOToB(BDTO bDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}

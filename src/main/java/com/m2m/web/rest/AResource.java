package com.m2m.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.m2m.domain.A;
import com.m2m.repository.ARepository;
import com.m2m.web.rest.util.HeaderUtil;
import com.m2m.web.rest.dto.ADTO;
import com.m2m.web.rest.mapper.AMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing A.
 */
@RestController
@RequestMapping("/api")
public class AResource {

    private final Logger log = LoggerFactory.getLogger(AResource.class);

    @Inject
    private ARepository aRepository;

    @Inject
    private AMapper aMapper;

    /**
     * POST  /as -> Create a new a.
     */
    @RequestMapping(value = "/as",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ADTO> create(@RequestBody ADTO aDTO) throws URISyntaxException {
        log.debug("REST request to save A : {}", aDTO);
        if (aDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new a cannot already have an ID").body(null);
        }
        A a = aMapper.aDTOToA(aDTO);
        A result = aRepository.save(a);
        return ResponseEntity.created(new URI("/api/as/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("a", result.getId().toString()))
                .body(aMapper.aToADTO(result));
    }

    /**
     * PUT  /as -> Updates an existing a.
     */
    @RequestMapping(value = "/as",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ADTO> update(@RequestBody ADTO aDTO) throws URISyntaxException {
        log.debug("REST request to update A : {}", aDTO);
        if (aDTO.getId() == null) {
            return create(aDTO);
        }
        A a = aMapper.aDTOToA(aDTO);
        A result = aRepository.save(a);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("a", aDTO.getId().toString()))
                .body(aMapper.aToADTO(result));
    }

    /**
     * GET  /as -> get all the as.
     */
    @RequestMapping(value = "/as",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<ADTO> getAll() {
        log.debug("REST request to get all As");
        return aRepository.findAllWithEagerRelationships().stream()
            .map(a -> aMapper.aToADTO(a))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * GET  /as/:id -> get the "id" a.
     */
    @RequestMapping(value = "/as/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ADTO> get(@PathVariable Long id) {
        log.debug("REST request to get A : {}", id);
        return Optional.ofNullable(aRepository.findOneWithEagerRelationships(id))
            .map(aMapper::aToADTO)
            .map(aDTO -> new ResponseEntity<>(
                aDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /as/:id -> delete the "id" a.
     */
    @RequestMapping(value = "/as/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete A : {}", id);
        aRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("a", id.toString())).build();
    }
}

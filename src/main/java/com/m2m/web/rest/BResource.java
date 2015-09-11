package com.m2m.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.m2m.domain.B;
import com.m2m.repository.BRepository;
import com.m2m.web.rest.util.HeaderUtil;
import com.m2m.web.rest.dto.BDTO;
import com.m2m.web.rest.mapper.BMapper;
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
 * REST controller for managing B.
 */
@RestController
@RequestMapping("/api")
public class BResource {

    private final Logger log = LoggerFactory.getLogger(BResource.class);

    @Inject
    private BRepository bRepository;

    @Inject
    private BMapper bMapper;

    /**
     * POST  /bs -> Create a new b.
     */
    @RequestMapping(value = "/bs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BDTO> create(@RequestBody BDTO bDTO) throws URISyntaxException {
        log.debug("REST request to save B : {}", bDTO);
        if (bDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new b cannot already have an ID").body(null);
        }
        B b = bMapper.bDTOToB(bDTO);
        B result = bRepository.save(b);
        return ResponseEntity.created(new URI("/api/bs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("b", result.getId().toString()))
                .body(bMapper.bToBDTO(result));
    }

    /**
     * PUT  /bs -> Updates an existing b.
     */
    @RequestMapping(value = "/bs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BDTO> update(@RequestBody BDTO bDTO) throws URISyntaxException {
        log.debug("REST request to update B : {}", bDTO);
        if (bDTO.getId() == null) {
            return create(bDTO);
        }
        B b = bMapper.bDTOToB(bDTO);
        B result = bRepository.save(b);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("b", bDTO.getId().toString()))
                .body(bMapper.bToBDTO(result));
    }

    /**
     * GET  /bs -> get all the bs.
     */
    @RequestMapping(value = "/bs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<BDTO> getAll() {
        log.debug("REST request to get all Bs");
        return bRepository.findAll().stream()
            .map(b -> bMapper.bToBDTO(b))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * GET  /bs/:id -> get the "id" b.
     */
    @RequestMapping(value = "/bs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BDTO> get(@PathVariable Long id) {
        log.debug("REST request to get B : {}", id);
        return Optional.ofNullable(bRepository.findOne(id))
            .map(bMapper::bToBDTO)
            .map(bDTO -> new ResponseEntity<>(
                bDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bs/:id -> delete the "id" b.
     */
    @RequestMapping(value = "/bs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete B : {}", id);
        bRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("b", id.toString())).build();
    }
}

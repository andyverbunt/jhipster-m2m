package com.m2m.web.rest;

import com.m2m.Application;
import com.m2m.domain.B;
import com.m2m.repository.BRepository;
import com.m2m.web.rest.dto.BDTO;
import com.m2m.web.rest.mapper.BMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BResource REST controller.
 *
 * @see BResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BResourceTest {

    private static final String DEFAULT_NAME_B = "SAMPLE_TEXT";
    private static final String UPDATED_NAME_B = "UPDATED_TEXT";

    @Inject
    private BRepository bRepository;

    @Inject
    private BMapper bMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restBMockMvc;

    private B b;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BResource bResource = new BResource();
        ReflectionTestUtils.setField(bResource, "bRepository", bRepository);
        ReflectionTestUtils.setField(bResource, "bMapper", bMapper);
        this.restBMockMvc = MockMvcBuilders.standaloneSetup(bResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        b = new B();
        b.setNameB(DEFAULT_NAME_B);
    }

    @Test
    @Transactional
    public void createB() throws Exception {
        int databaseSizeBeforeCreate = bRepository.findAll().size();

        // Create the B
        BDTO bDTO = bMapper.bToBDTO(b);

        restBMockMvc.perform(post("/api/bs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bDTO)))
                .andExpect(status().isCreated());

        // Validate the B in the database
        List<B> bs = bRepository.findAll();
        assertThat(bs).hasSize(databaseSizeBeforeCreate + 1);
        B testB = bs.get(bs.size() - 1);
        assertThat(testB.getNameB()).isEqualTo(DEFAULT_NAME_B);
    }

    @Test
    @Transactional
    public void getAllBs() throws Exception {
        // Initialize the database
        bRepository.saveAndFlush(b);

        // Get all the bs
        restBMockMvc.perform(get("/api/bs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(b.getId().intValue())))
                .andExpect(jsonPath("$.[*].nameB").value(hasItem(DEFAULT_NAME_B.toString())));
    }

    @Test
    @Transactional
    public void getB() throws Exception {
        // Initialize the database
        bRepository.saveAndFlush(b);

        // Get the b
        restBMockMvc.perform(get("/api/bs/{id}", b.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(b.getId().intValue()))
            .andExpect(jsonPath("$.nameB").value(DEFAULT_NAME_B.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingB() throws Exception {
        // Get the b
        restBMockMvc.perform(get("/api/bs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateB() throws Exception {
        // Initialize the database
        bRepository.saveAndFlush(b);

		int databaseSizeBeforeUpdate = bRepository.findAll().size();

        // Update the b
        b.setNameB(UPDATED_NAME_B);
        
        BDTO bDTO = bMapper.bToBDTO(b);

        restBMockMvc.perform(put("/api/bs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bDTO)))
                .andExpect(status().isOk());

        // Validate the B in the database
        List<B> bs = bRepository.findAll();
        assertThat(bs).hasSize(databaseSizeBeforeUpdate);
        B testB = bs.get(bs.size() - 1);
        assertThat(testB.getNameB()).isEqualTo(UPDATED_NAME_B);
    }

    @Test
    @Transactional
    public void deleteB() throws Exception {
        // Initialize the database
        bRepository.saveAndFlush(b);

		int databaseSizeBeforeDelete = bRepository.findAll().size();

        // Get the b
        restBMockMvc.perform(delete("/api/bs/{id}", b.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<B> bs = bRepository.findAll();
        assertThat(bs).hasSize(databaseSizeBeforeDelete - 1);
    }
}

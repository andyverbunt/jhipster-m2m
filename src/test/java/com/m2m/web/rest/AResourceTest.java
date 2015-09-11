package com.m2m.web.rest;

import com.m2m.Application;
import com.m2m.domain.A;
import com.m2m.repository.ARepository;
import com.m2m.web.rest.dto.ADTO;
import com.m2m.web.rest.mapper.AMapper;

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
 * Test class for the AResource REST controller.
 *
 * @see AResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AResourceTest {

    private static final String DEFAULT_NAME_A = "SAMPLE_TEXT";
    private static final String UPDATED_NAME_A = "UPDATED_TEXT";

    @Inject
    private ARepository aRepository;

    @Inject
    private AMapper aMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restAMockMvc;

    private A a;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AResource aResource = new AResource();
        ReflectionTestUtils.setField(aResource, "aRepository", aRepository);
        ReflectionTestUtils.setField(aResource, "aMapper", aMapper);
        this.restAMockMvc = MockMvcBuilders.standaloneSetup(aResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        a = new A();
        a.setNameA(DEFAULT_NAME_A);
    }

    @Test
    @Transactional
    public void createA() throws Exception {
        int databaseSizeBeforeCreate = aRepository.findAll().size();

        // Create the A
        ADTO aDTO = aMapper.aToADTO(a);

        restAMockMvc.perform(post("/api/as")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(aDTO)))
                .andExpect(status().isCreated());

        // Validate the A in the database
        List<A> as = aRepository.findAll();
        assertThat(as).hasSize(databaseSizeBeforeCreate + 1);
        A testA = as.get(as.size() - 1);
        assertThat(testA.getNameA()).isEqualTo(DEFAULT_NAME_A);
    }

    @Test
    @Transactional
    public void getAllAs() throws Exception {
        // Initialize the database
        aRepository.saveAndFlush(a);

        // Get all the as
        restAMockMvc.perform(get("/api/as"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(a.getId().intValue())))
                .andExpect(jsonPath("$.[*].nameA").value(hasItem(DEFAULT_NAME_A.toString())));
    }

    @Test
    @Transactional
    public void getA() throws Exception {
        // Initialize the database
        aRepository.saveAndFlush(a);

        // Get the a
        restAMockMvc.perform(get("/api/as/{id}", a.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(a.getId().intValue()))
            .andExpect(jsonPath("$.nameA").value(DEFAULT_NAME_A.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingA() throws Exception {
        // Get the a
        restAMockMvc.perform(get("/api/as/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateA() throws Exception {
        // Initialize the database
        aRepository.saveAndFlush(a);

		int databaseSizeBeforeUpdate = aRepository.findAll().size();

        // Update the a
        a.setNameA(UPDATED_NAME_A);
        
        ADTO aDTO = aMapper.aToADTO(a);

        restAMockMvc.perform(put("/api/as")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(aDTO)))
                .andExpect(status().isOk());

        // Validate the A in the database
        List<A> as = aRepository.findAll();
        assertThat(as).hasSize(databaseSizeBeforeUpdate);
        A testA = as.get(as.size() - 1);
        assertThat(testA.getNameA()).isEqualTo(UPDATED_NAME_A);
    }

    @Test
    @Transactional
    public void deleteA() throws Exception {
        // Initialize the database
        aRepository.saveAndFlush(a);

		int databaseSizeBeforeDelete = aRepository.findAll().size();

        // Get the a
        restAMockMvc.perform(delete("/api/as/{id}", a.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<A> as = aRepository.findAll();
        assertThat(as).hasSize(databaseSizeBeforeDelete - 1);
    }
}

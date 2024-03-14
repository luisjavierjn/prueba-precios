package com.inditex.pruebaprecios;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inditex.pruebaprecios.domain.dto.ApplicableRangeDTO;
import com.inditex.pruebaprecios.domain.requests.CreatePriceRequest;
import com.inditex.pruebaprecios.repositories.PriceRepository;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class PriceControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PriceRepository priceRepository;

    @Test
    void fetchAllUsersEndpoint() throws Exception{
        MvcResult result = mockMvc.perform(get("/api/v1/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn(); // Obtiene el resultado para su posterior análisis

        String contentAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> responseList = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(priceRepository.findAll()).hasSize(responseList.size());
    }

    @Test
    void getApplicableRanges() throws Exception {
        CreatePriceRequest priceRequest=new CreatePriceRequest(LocalDateTime.parse("2020-06-14T10:00:00"),35455,"1");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mockMvc.perform(get("/api/v1/ranges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(priceRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productId" , is(35455)))
                .andExpect(jsonPath("$[0].startDate", is("2020-06-14T00:00:00")))
                .andExpect(jsonPath("$[0].endDate", is("2020-12-31T23:59:59")))
                .andExpect(jsonPath("$[0].chainIdentifier" , is(1)))
                .andExpect(jsonPath("$[0].rateToApply" , is(1)))
                .andExpect(jsonPath("$[0].finalPrice" , is(35.50)));
    }

    @ParameterizedTest
    @CsvSource({
            "2020-06-14T10:00:00,35455,1,35455,2020-06-14T00:00:00,2020-06-14T15:00:00,1,1,35.50", // Test 1: request at 10:00 a.m. on the 14th for product 35455 for brand 1 (XYZ)
            "2020-06-14T16:00:00,35455,1,35455,2020-06-14T15:00:00,2020-06-14T18:30:00,1,2,25.45", // Test 2: request at 4:00 p.m. on the 14th for product 35455 for brand 1 (XYZ)
            "2020-06-14T21:00:00,35455,1,35455,2020-06-14T18:30:00,2020-06-15T00:00:00,1,1,35.50", // Test 3: request at 9:00 p.m. on day 14th for product 35455 for brand 1 (XYZ)
            "2020-06-15T10:00:00,35455,1,35455,2020-06-15T00:00:00,2020-06-15T11:00:00,1,3,30.50", // Test 4: request at 10:00 a.m. on the 15th for product 35455 for brand 1 (XYZ)
            "2020-06-16T21:00:00,35455,1,35455,2020-06-15T16:00:00,2020-12-31T23:59:59,1,4,38.95"  // Test 5: request at 9:00 p.m. on day 16th for product 35455 for brand 1 (XYZ)
    })
    void testScenarios(@AggregateWith(PriceRequestAggregator.class) CreatePriceRequest createPriceRequest,
                       @AggregateWith(ApplicableRangeAggregator.class) ApplicableRangeDTO applicableRangeDTO) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        mockMvc.perform(get("/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createPriceRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId" , is(applicableRangeDTO.getProductId())))
                .andExpect(jsonPath("$.startDate", is(applicableRangeDTO.getStartDate().format(formatter))))
                .andExpect(jsonPath("$.endDate", is(applicableRangeDTO.getEndDate().format(formatter))))
                .andExpect(jsonPath("$.chainIdentifier" , is(applicableRangeDTO.getBrandId())))
                .andExpect(jsonPath("$.rateToApply" , is(applicableRangeDTO.getPriceList())))
                .andExpect(jsonPath("$.finalPrice" , is(applicableRangeDTO.getPrice().doubleValue())));
    }
}

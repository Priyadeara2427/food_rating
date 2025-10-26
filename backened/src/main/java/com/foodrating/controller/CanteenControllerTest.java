// package com.foodrating.controller;

// import com.foodrating.entity.Canteen;
// import com.foodrating.repository.CanteenRepository;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.Arrays;
// import java.util.Optional;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(CanteenController.class)
// public class CanteenControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private CanteenRepository canteenRepository;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @Test
//     public void testGetAllCanteens() throws Exception {
//         Canteen canteen = new Canteen();
//         canteen.setId(1L);
//         canteen.setName("Test Canteen");
//         canteen.setLocation("Test Location");

//         when(canteenRepository.findAll()).thenReturn(Arrays.asList(canteen));

//         mockMvc.perform(get("/api/canteens"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].name").value("Test Canteen"))
//                 .andExpect(jsonPath("$[0].location").value("Test Location"));
//     }

//     @Test
//     public void testGetCanteenById() throws Exception {
//         Canteen canteen = new Canteen();
//         canteen.setId(1L);
//         canteen.setName("Test Canteen");

//         when(canteenRepository.findById(1L)).thenReturn(Optional.of(canteen));

//         mockMvc.perform(get("/api/canteens/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name").value("Test Canteen"));
//     }

//     @Test
//     public void testGetCanteenByIdNotFound() throws Exception {
//         when(canteenRepository.findById(1L)).thenReturn(Optional.empty());

//         mockMvc.perform(get("/api/canteens/1"))
//                 .andExpect(status().isNotFound());
//     }

//     @Test
//     public void testCreateCanteen() throws Exception {
//         Canteen canteen = new Canteen();
//         canteen.setName("New Canteen");
//         canteen.setLocation("Campus");
//         canteen.setDescription("Test description");

//         when(canteenRepository.existsByName("New Canteen")).thenReturn(false);
//         when(canteenRepository.save(any(Canteen.class))).thenReturn(canteen);

//         mockMvc.perform(post("/api/canteens")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(canteen)))
//                 .andExpect(status().isCreated());
//     }
// }
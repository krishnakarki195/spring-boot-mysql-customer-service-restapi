package com.galvanize.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.dto.Resolution;
import com.galvanize.dto.ServiceRequest;
import com.galvanize.entities.Customer;
import com.galvanize.entities.ServiceNote;
import com.galvanize.entities.ServiceTicket;
import com.galvanize.repositories.CustomerRepo;
import com.galvanize.repositories.ServiceNoteRepo;
import com.galvanize.repositories.ServiceTicketRepo;
import com.galvanize.services.CustSvcService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ServiceRequestControllerTest {
    @Autowired
    MockMvc mvc;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    CustSvcService service;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    ServiceTicketRepo ticketRepo;

    @Autowired
    ServiceNoteRepo noteRepo;

    private Customer testCustomer;
    private ServiceTicket testTicket;
    private static final String PARAGRAPH = "Lorem ipsum dolor sit amet, consectetur " +
            "adipiscing elit. Minime id quidem, inquam, alienum, multumque ad ea, quae " +
            "quaerimus, explicatio tua ista profecerit. Duo Reges: constructio interrete. " +
            "Inde igitur, inquit, ordiendum est. Sic consequentibus vestris sublatis prima " +
            "tolluntur. Quae diligentissime contra Aristonem dicuntur a Chryippo. Cur igitur, " +
            "inquam, res tam dissimiles eodem nomine appellas? Hoc Hieronymus summum bonum esse" +
            " dixit.";
    private String jsonSvcRequest;

    @BeforeEach
    void setUp() {
        // Create a test customer
        testCustomer = new Customer("test", "customer", "123 any street", "my city", "US", "12345");
        testCustomer.setTelephone("876-555-1212");
        customerRepo.save(testCustomer);
        assertNotNull(testCustomer.getId());

        // Create a ticket
        testTicket = service.createServiceTicket(testCustomer.getTelephone(), "Dryer won't dry", PARAGRAPH);
        assertNotNull(testTicket.getId());
    }

    @Test
    void createAndGetCustomer() throws Exception{
        Customer customer = new Customer("Jay", "Unit", "101 Java St", "Java", "US", "88888");
        customer.setTelephone("800-555-1212");

        mvc.perform(post("/api/service/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getCustomer() throws Exception {
        Customer customer = new Customer("Jay", "Unit", "101 Java St", "Java", "US", "88888");
        customer.setTelephone("800-555-1212");
        service.createCustomer(customer);

        mvc.perform(get("/api/service/customer/"+customer.getTelephone()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void postRequestWithId() throws Exception {
        //Setup
        ServiceRequest serviceRequest = new ServiceRequest(testCustomer.getId(),
                "it's broke and I need it fixed", PARAGRAPH);
        String jsonRequest = mapper.writeValueAsString(serviceRequest);
        //Execute
        mvc.perform(post("/api/service").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.problem", is("it's broke and I need it fixed")))
                .andExpect(jsonPath("$.problemDetails", is(PARAGRAPH)))
                .andDo(print());
    }

    @Test
    void postRequestWithTelephone() throws Exception {
        //Setup
        ServiceRequest serviceRequest = new ServiceRequest(testCustomer.getTelephone(),
                "it's broke and I need it fixed", PARAGRAPH);
        String jsonRequest = mapper.writeValueAsString(serviceRequest);
        //Execute
        mvc.perform(post("/api/service").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.problem", is("it's broke and I need it fixed")))
                .andExpect(jsonPath("$.problemDetails", is(PARAGRAPH)))
                .andDo(print());
    }

    @Test
    void getAllRequests() throws Exception {
        generateTickets();
        mvc.perform(get("/api/service"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(25))))
                .andDo(print());
    }

    @Test
    void getOneRequestById() throws Exception {
        mvc.perform(get("/api/service/"+testTicket.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testTicket.getId().intValue())))
                .andExpect(jsonPath("$.problem", is(testTicket.getProblem())))
                .andExpect(jsonPath("$.problemDetails", is(testTicket.getProblemDetails())))
                .andExpect(jsonPath("$.status", is(testTicket.getStatus())))
                .andDo(print());
    }

    @Test
    void assignRequest() throws Exception {
        mvc.perform(put("/api/service/"+testTicket.getId())
                                    .param("technician", "Terry Tech"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.technician", is("Terry Tech")))
                .andDo(print());
    }

    @Test
    void addNote() throws Exception {
        String note = "{\"createdBy\": \"JUNIT\", \"note\":\"JUNIT Test Note\"}";
        mvc.perform(put("/api/service/"+testTicket.getId()+"/notes")
                    .contentType(MediaType.APPLICATION_JSON).content(note))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void resolveTicket() throws Exception{
        ServiceTicket ticket = service.createServiceTicket(testCustomer.getTelephone(), "description", PARAGRAPH);
        service.assign(ticket.getId(), "Johnny Fixer");
        Resolution resolution = new Resolution(ticket.getTechnician(), "Just fixed it");

        mvc.perform(patch("/api/service/"+ticket.getId()+"/resolve")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(resolution)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private void generateTickets(){
        ServiceTicket ticket;
        for (int i = 0; i < 25; i++) {
            ticket = service.createServiceTicket(testCustomer.getTelephone(),
                    "description", PARAGRAPH);
            ticket.setTechnician("JUNIT-"+i);
            ticket.setAppointmentDate(LocalDateTime.now().plusDays(i));
            for (int j = 0; j < 5; j++) {
                ticket.addNote("JUNIT", "Note number "+i);
            }
            service.save(ticket);
        }
    }

}
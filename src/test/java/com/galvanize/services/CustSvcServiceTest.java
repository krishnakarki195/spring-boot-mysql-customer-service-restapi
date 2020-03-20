package com.galvanize.services;

import com.galvanize.entities.Customer;
import com.galvanize.entities.ServiceNote;
import com.galvanize.entities.ServiceTicket;
import com.galvanize.repositories.CustomerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CustSvcServiceTest {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustSvcService service;

    Customer testCustomer;
    ServiceTicket testTicket;

    private static final String PARAGRAPH = "Lorem ipsum dolor sit amet, consectetur " +
            "adipiscing elit. Minime id quidem, inquam, alienum, multumque ad ea, quae " +
            "quaerimus, explicatio tua ista profecerit. Duo Reges: constructio interrete. " +
            "Inde igitur, inquit, ordiendum est. Sic consequentibus vestris sublatis prima " +
            "tolluntur. Quae diligentissime contra Aristonem dicuntur a Chryippo. Cur igitur, " +
            "inquam, res tam dissimiles eodem nomine appellas? Hoc Hieronymus summum bonum esse" +
            " dixit.";

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
    void createServiceTicketWithId() {
        ServiceTicket ticket = service.createServiceTicket(testCustomer.getId(), "it's broke and I need it fixed!", PARAGRAPH);
        ticket.setProblemDetails(PARAGRAPH);
        assertNotNull(ticket.getId());
        System.out.println("\n*****\n\n"+ticket+"\n\n*****\n");
    }

    @Test
    void createServiceTicketWithPhone() {
        ServiceTicket ticket = service.createServiceTicket(testCustomer.getTelephone(), "it's broke and I need it fixed", PARAGRAPH);
        assertNotNull(ticket.getId());
        System.out.println("\n*****\n\n"+ticket+"\n\n*****\n");
    }

    @Test
    void getAllTickets() {
        service.createServiceTicket(testCustomer.getTelephone(), "Dryer won't dry", PARAGRAPH);
        service.createServiceTicket(testCustomer.getTelephone(), "it's broke and I need it fixed", PARAGRAPH);

        List<ServiceTicket> tickets = service.getAllTickets();
        assertFalse(tickets.isEmpty());

        System.out.println(tickets);
    }

    @Test
    void getOneTicket() {
        Optional<ServiceTicket> actual = service.getTicket(testTicket.getId());
        assertNotNull(actual);
        assertEquals(testTicket.getProblem(), actual.get().getProblem());
        assertEquals(testTicket.getProblemDetails(), actual.get().getProblemDetails());
    }

    @Test
    void assignTicket() {
        Optional<ServiceTicket> actual = service.getTicket(testTicket.getId());

        service.assign(testTicket.getId(), "Bobby Fixit");

        assertEquals("Bobby Fixit", actual.get().getTechnician());
    }

    @Test
    void addNote() {
        ServiceTicket actual = service.assign(testTicket.getId(), "Robert Fixer");
        service.addNote(testTicket.getId(), "CUSTOMER", PARAGRAPH);
        service.addNote(testTicket.getId(), "TECHNICIAN", PARAGRAPH);
        service.addNote(testTicket.getId(), "My Manager Says", PARAGRAPH);
        List<ServiceNote> notes = actual.getNotes();
        assertFalse(notes.isEmpty());
        assertEquals(3, notes.size());
        boolean found = false;
        for(ServiceNote note : notes){
            if(note.getCreatedBy().equals("CUSTOMER") &&
               note.getNote().equals(PARAGRAPH)){
                found = true;
            }
        }
        assertTrue(found);
        System.out.println("\n\n***************\n");
        notes.forEach(System.out::println);
        System.out.println("\n******************\n\n");
    }

    @Test
    void setAppointment() {
        LocalDateTime nowPlusTen = LocalDateTime.now().plusDays(10);
        ServiceTicket ticket = service.setAppointment(testTicket.getId(), nowPlusTen);

        assertEquals(nowPlusTen, ticket.getAppointmentDate());
    }

    @Test
    void resolveTicket() {
        String resolutionNote = "I Plugged it in and it works now";
        ServiceTicket ticket = service.resolve(testTicket.getId(), "CUSTOMER", resolutionNote);

        assertEquals("RESOLVED", ticket.getStatus());
        // Check that note was added.
        boolean found = false;
        for(ServiceNote note : ticket.getNotes()){
            if (note.getCreatedBy().equals("CUSTOMER") && note.getNote().equals(resolutionNote))
                found = true;
        }
        assertTrue(found);
        System.out.println(ticket);
    }

    @Test
    void createCustomer() {
        Customer customer = new Customer("Jay", "Unit", "101 Java St", "Java", "US", "88888");
        service.createCustomer(customer);
        assertNotNull(customer.getId());

        Customer c2 = service.getCustomer(customer.getId());
        assertEquals(customer.getId(), c2.getId());
    }
}
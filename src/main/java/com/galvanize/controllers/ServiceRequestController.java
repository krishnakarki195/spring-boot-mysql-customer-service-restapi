package com.galvanize.controllers;

import com.galvanize.dto.Resolution;
import com.galvanize.dto.ServiceRequest;
import com.galvanize.entities.Customer;
import com.galvanize.entities.ServiceNote;
import com.galvanize.entities.ServiceTicket;
import com.galvanize.services.CustSvcService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/service")
public class ServiceRequestController {

    final CustSvcService service;

    public ServiceRequestController(CustSvcService service) {
        this.service = service;
    }

    @PostMapping("/customer")
    public Customer createCustomer(@RequestBody Customer customer){
        return service.createCustomer(customer);
    }

    @GetMapping("/customer/{telephone}")
    public Customer findCustomerByPhone(@PathVariable String telephone){
        return service.getCustomerByTelephone(telephone);
    }

    @PostMapping
    public ResponseEntity<ServiceTicket> createRequest(@RequestBody ServiceRequest serviceRequest){
        ServiceTicket ticket = null;
        if (serviceRequest.getCustomerNumber() != null){
            //Should have telephone number then
            ticket = service.createServiceTicket(serviceRequest.getCustomerNumber(),
                    serviceRequest.getProblem(),
                    serviceRequest.getProblemDetails());
        }else if (serviceRequest.getTelephone() != null){
            //Create ticket with phone number
            ticket = service.createServiceTicket(serviceRequest.getTelephone(),
                    serviceRequest.getProblem(), serviceRequest.getProblemDetails());
        }else{
            //error condition
            return ResponseEntity.badRequest().header("errorMsg",
                    "Telephone or customer id is required")
                    .build();
        }
//        assert ticket != null;
        return ResponseEntity.ok(ticket);
    }

    @GetMapping
    public ResponseEntity<List<ServiceTicket>> getAllServiceTickets(){
        List<ServiceTicket> tickets = service.getAllTickets();
        if(tickets.isEmpty()){
            return ResponseEntity.noContent().header("errorMsg", "No tickets found").build();
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTicket> getTicketById(@PathVariable Long id){
        Optional<ServiceTicket> ticket = service.getTicket(id);
        return ticket.isPresent() ? ResponseEntity.ok(ticket.get()) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}") @Transactional
    public ResponseEntity<ServiceTicket> assignServiceRequest(@PathVariable Long id, @RequestParam String technician){
        ServiceTicket ticket = service.assign(id, technician);
        ticket = service.addNote(ticket.getId(), "WEB", "Assigned ticket to '"+technician+"'");
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{id}/notes")
    public ResponseEntity<ServiceTicket> addNote(@PathVariable Long id, @RequestBody ServiceNote note){
        ServiceTicket ticket = service.addNote(id, note.getCreatedBy(), note.getNote());
        if (ticket != null){
            return ResponseEntity.ok(ticket);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/resolve")
    public ServiceTicket resolve(@PathVariable Long id, @RequestBody Resolution resolution){
        return service.resolve(id, resolution.getResolvedBy(), resolution.getResolutionNotes());
    }
}

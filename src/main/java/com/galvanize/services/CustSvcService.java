package com.galvanize.services;

import com.galvanize.entities.Customer;
import com.galvanize.entities.ServiceNote;
import com.galvanize.entities.ServiceTicket;
import com.galvanize.repositories.CustomerRepo;
import com.galvanize.repositories.ServiceNoteRepo;
import com.galvanize.repositories.ServiceTicketRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustSvcService {
    CustomerRepo customerRepo;
    ServiceNoteRepo noteRepo;
    ServiceTicketRepo ticketRepo;

    public CustSvcService(CustomerRepo customerRepo, ServiceNoteRepo noteRepo, ServiceTicketRepo ticketRepo) {
        this.customerRepo = customerRepo;
        this.noteRepo = noteRepo;
        this.ticketRepo = ticketRepo;
    }

    public Customer createCustomer(Customer customer){
        return customerRepo.save(customer);
    }

    public Customer getCustomer(Long id){
        Optional<Customer> customer = customerRepo.findById(id);
        return customer.orElse(null);
    }
    public Customer getCustomerByTelephone(String phone){
        Optional<Customer> customer = customerRepo.findByTelephone(phone);
        return customer.orElse(null);
    }

    public ServiceTicket createServiceTicket(Long customerId, String description, String problemDetails){
        Optional<Customer> customer = customerRepo.findById(customerId);
        if(customer.isPresent()){
            return createServiceTicket(customer.get(), description, problemDetails );
        }else{
            throw new RuntimeException("Customer id '"+customerId+"' not found");
        }
    }

    public ServiceTicket createServiceTicket(String telephone, String description, String problemDetails){
        Optional<Customer> customer = customerRepo.findByTelephone(telephone);
        if(customer.isPresent()){
            return createServiceTicket(customer.get(), description, problemDetails);
        }else{
            throw new RuntimeException("Customer with telephone '"+telephone+"' not found");
        }
    }

    public List<ServiceTicket> getAllTickets(){
        return ticketRepo.findAll();
    }

    public Optional<ServiceTicket> getTicket(Long id){
        Optional<ServiceTicket> ticket =  ticketRepo.findById(id);
        return ticket;
    }

    private ServiceTicket createServiceTicket(Customer customer, String problem, String problemDetails){
        ServiceTicket ticket = new ServiceTicket();
        ticket.setCustomer(customer);
        ticket.setProblem(problem);
        ticket.setProblemDetails(problemDetails);
        ticket.setRequestDate(LocalDateTime.now());
        return ticketRepo.save(ticket);
    }

    public ServiceTicket assign(Long id, String technician) {
        ServiceTicket ticket = getServiceTicket(id);
        ticket.setTechnician(technician);
        ticket.setStatus("ASSIGNED");
//        ticket.addNote("SYSTEM", "Ticket assigned to '"+technician+"'");
        ServiceTicket newTicket = ticketRepo.saveAndFlush(ticket);

        return newTicket;
    }

    public ServiceTicket addNote(Long id, String createdBy, String note) {
        ServiceTicket ticket = getServiceTicket(id);
        ticket.addNote(createdBy, note);
        ticketRepo.save(ticket);
        return ticket;
    }

    public ServiceTicket setAppointment(Long id, LocalDateTime nowPlusTen) {
        ServiceTicket ticket = getServiceTicket(id);
        ticket.setAppointmentDate(nowPlusTen);
        return ticketRepo.save(ticket);
    }

    private ServiceTicket getServiceTicket(Long id) {
        Optional<ServiceTicket> ticket = ticketRepo.findById(id);
        if(!ticket.isPresent()){
            throw new RuntimeException("Ticket id '"+id+"' not found");
        }
        return ticket.get();
    }

    public ServiceTicket resolve(Long id, String resolvedBy, String resolutionNote) {
        ServiceTicket ticket = getServiceTicket(id);
        ticket.setStatus("RESOLVED");
        ticket.addNote(resolvedBy, resolutionNote);
        return ticketRepo.save(ticket);
    }

    public ServiceTicket save(ServiceTicket ticket){
        return ticket;
    }
}

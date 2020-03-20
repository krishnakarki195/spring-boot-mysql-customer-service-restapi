package com.galvanize.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "service_tickets")
public class ServiceTicket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    private String problem;
    private String technician;
    private String status;
    @JsonFormat(pattern = "MM/dd/yyyy HH:mm")
    private LocalDateTime requestDate;
    private LocalDateTime appointmentDate;
    @OneToMany(mappedBy = "serviceTicket", orphanRemoval = true, cascade = CascadeType.ALL)
    List<ServiceNote> notes;
    @Column(columnDefinition = "LONGTEXT")
    private String problemDetails;

    @Override
    public String toString() {
        return "ServiceTicket{" +
                "id=" + id +
                ", description='" + problem + '\'' +
                ", technician='" + technician + '\'' +
                ", status='" + status + '\'' +
                ", requestDate=" + requestDate +
                ", appointmentDate=" + appointmentDate +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String description) {
        this.problem = description;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<ServiceNote> getNotes() {
        return notes;
    }

    public void setNotes(List<ServiceNote> notes) {
        this.notes = notes;
    }

    public String getProblemDetails() {
        return problemDetails;
    }

    public void setProblemDetails(String problemDetails) {
        this.problemDetails = problemDetails;
    }

    public void addNote(String createdBy, String note) {
        if (notes == null) notes = new ArrayList<>();
        ServiceNote newNote = new ServiceNote();
        newNote.setServiceTicket(this);
        newNote.setCreatedBy(createdBy);
        newNote.setNote(note);
        notes.add(newNote);
    }
}

package com.galvanize.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "service_notes")
public class ServiceNote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonIgnore
    ServiceTicket serviceTicket;
    @Column(columnDefinition = "LONGTEXT")
    String note;
    @JsonFormat(pattern = "MM/dd/yyyy HH:mm")
    LocalDateTime createdAt = LocalDateTime.now();
    String createdBy;

    public ServiceNote() {
    }

    @Override
    public String toString() {
        return "ServiceNotes{" +
                "id=" + id +
                ", serviceTicketId=" + serviceTicket +
                ", note='" + note + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceTicket getServiceTicket() {
        return serviceTicket;
    }

    public void setServiceTicketId(ServiceTicket serviceTicket) {
        this.serviceTicket = serviceTicket;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setServiceTicket(ServiceTicket serviceTicket) {
        this.serviceTicket = serviceTicket;
    }
}

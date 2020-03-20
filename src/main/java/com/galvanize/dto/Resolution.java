package com.galvanize.dto;

public class Resolution {
    Long ticketNumber;
    String resolvedBy;
    String resolutionNotes;

    public Resolution() {
    }

    public Resolution(String resolvedBy, String resolutionNotes) {
        this.resolvedBy = resolvedBy;
        this.resolutionNotes = resolutionNotes;
    }

    public Long getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(Long ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }
}

package com.galvanize.repositories;

import com.galvanize.entities.ServiceTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTicketRepo extends JpaRepository<ServiceTicket, Long> {
}

package com.galvanize.repositories;

import com.galvanize.entities.ServiceNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceNoteRepo extends JpaRepository<ServiceNote, Long> {
}

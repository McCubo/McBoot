package org.cubias.repositories;

import org.cubias.entities.FowAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FowAuditRepository extends JpaRepository<FowAudit, Integer> {

}

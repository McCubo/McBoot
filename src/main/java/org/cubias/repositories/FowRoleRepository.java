package org.cubias.repositories;

import org.cubias.entities.FowRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FowRoleRepository extends JpaRepository<FowRole, Integer> {

}

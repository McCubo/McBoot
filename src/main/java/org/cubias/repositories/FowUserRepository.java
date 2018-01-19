package org.cubias.repositories;

import org.cubias.entities.FowUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FowUserRepository extends JpaRepository<FowUser, Integer> {

	public FowUser findOneByUseAdUser(String username);
}

package hornet.framework.repository;


import org.springframework.data.repository.CrudRepository;

import hornet.framework.entity.Utilisateur;

public interface UtilisateurRepository
extends CrudRepository<Utilisateur, Long>,
hornet.framework.repository.CrudProjectionRepository<Utilisateur, Long> {

}

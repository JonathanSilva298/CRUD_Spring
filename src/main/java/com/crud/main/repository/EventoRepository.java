package com.crud.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.main.model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>{
	Evento findByCodigo(long codigo);
}

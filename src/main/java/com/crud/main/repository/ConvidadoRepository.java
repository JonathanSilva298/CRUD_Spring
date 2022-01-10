package com.crud.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.main.model.Convidado;
import com.crud.main.model.Evento;

@Repository
public interface ConvidadoRepository extends JpaRepository<Convidado, String> {
	Iterable<Convidado> findByEvento(Evento evento);
	Convidado findByRg(String rg);
}

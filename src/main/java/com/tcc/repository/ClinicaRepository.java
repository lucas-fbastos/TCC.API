package com.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.domain.Clinica;

@Repository
public interface ClinicaRepository extends JpaRepository<Clinica,Long>{

}

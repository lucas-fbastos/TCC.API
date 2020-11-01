package com.tcc.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.DTO.ProcedimentoMedicoDTO;
import com.tcc.DTO.ProcedimentoMedicoFormDTO;
import com.tcc.domain.ProcedimentoMedico;
import com.tcc.domain.TipoProcedimento;
import com.tcc.domain.User;
import com.tcc.repository.ProcedimentoMedicoRepository;
import com.tcc.repository.TipoProcedimentoRepository;
import com.tcc.repository.UserRepository;
import com.tcc.security.UserSecurity;
import com.tcc.service.exceptions.DataIntegrityException;
import com.tcc.service.exceptions.NoElementException;

@Service
public class ProcedimentoMedicoService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private ProcedimentoMedicoRepository procedimentoMedicoRepository;
	 
	@Autowired 
	private TipoProcedimentoRepository tipoProcedimentoRepository;

	public List<ProcedimentoMedicoDTO> getAllByUser(){
		
		UserSecurity logado = UserService.authenticated();
		if(logado!=null) {
			Long id = logado.getId();
			User usuario = this.userRepository.findById(id).orElseThrow();
			List<ProcedimentoMedico> procedimentos = this.procedimentoMedicoRepository.findByUserOrderByDtProcedimentoDesc(usuario);
			List<ProcedimentoMedicoDTO> dtos = new ArrayList<>();
			if(procedimentos != null && !procedimentos.isEmpty()) {
				for(ProcedimentoMedico procedimento : procedimentos) {
					
					String nmProfissionalSaude = null;
					if(procedimento.getProfissionalSaude()!=null)
						nmProfissionalSaude = procedimento.getProfissionalSaude().getNome();
					
					ProcedimentoMedicoDTO dto  = new ProcedimentoMedicoDTO(procedimento.getId(), procedimento.getTitulo(), procedimento.getDescLocal(), 
														procedimento.getDescricao(), procedimento.getDtRegistro(), procedimento.getDtRetorno(), 
														procedimento.getDtProcedimento(), procedimento.getUser().getNome(), 
														procedimento.getTipoProcedimento().getDescTipoProcedimeto(), nmProfissionalSaude);
					dtos.add(dto);
				}
				return dtos;
			}else {
				throw new NoElementException("Não existem dados médicos para este usuário!");
			}
		}else {
			throw new NoElementException("Usuário inválido, tente logar novamente");
		}
	}
	
	public List<TipoProcedimento> getTiposProcedimentosMedicos(){
		List<TipoProcedimento> list= this.tipoProcedimentoRepository.findAll();
		if(list!= null && !list.isEmpty())
			return list;
		else
			throw new NoElementException("Não foram cadastrados tipos de procedimento");
	}
	
	public void save(ProcedimentoMedicoFormDTO dto) {
		UserSecurity logado = UserService.authenticated();
		if(logado!=null) {
			Long id = logado.getId();
			User usuario = this.userRepository.findById(id).orElseThrow();
			ProcedimentoMedico pm = new ProcedimentoMedico();
			TipoProcedimento tp = this.tipoProcedimentoRepository.findById(dto.getIdTipoProcedimento()).orElseThrow();
			pm.setDescLocal(dto.getDescLocal());
			pm.setDescricao(dto.getDescricao());
			pm.setDtRegistro(LocalDate.now());
			pm.setDtRetorno(dto.getDtRetorno());
			pm.setDtProcedimento(dto.getDtProcedimento());
			pm.setTipoProcedimento(tp);
			pm.setDescLocal(dto.getDescLocal());
			pm.setTitulo(dto.getTitulo());
			pm.setUser(usuario);
			this.procedimentoMedicoRepository.save(pm);
		}else {
			throw new NoElementException("Usuário inválido, tente logar novamente");
		}
	}
	
	public void save(ProcedimentoMedicoFormDTO dto, User paciente) {
		UserSecurity logado = UserService.authenticated();
		if(logado!=null) {
			Long id = logado.getId();
			User profissionalSaude = this.userRepository.findById(id).orElseThrow();
			ProcedimentoMedico pm = new ProcedimentoMedico();
			TipoProcedimento tp = this.tipoProcedimentoRepository.findById(dto.getIdTipoProcedimento()).orElseThrow();
			pm.setDescLocal(dto.getDescLocal());
			pm.setDescricao(dto.getDescricao());
			pm.setDtRegistro(LocalDate.now());
			pm.setDtRetorno(LocalDate.now());
			pm.setDtProcedimento(dto.getDtProcedimento());
			pm.setTipoProcedimento(tp);
			pm.setDescLocal(dto.getDescLocal());
			pm.setTitulo(dto.getTitulo());
			pm.setUser(paciente);
			pm.setProfissionalSaude(profissionalSaude);
			this.procedimentoMedicoRepository.save(pm);
		}else {
			throw new NoElementException("Usuário inválido, tente logar novamente");
		}
	}

	public ProcedimentoMedicoFormDTO update(ProcedimentoMedicoFormDTO dto) {
		ProcedimentoMedico pm = this.procedimentoMedicoRepository.findById(dto.getId()).orElseThrow();
		TipoProcedimento tp = this.tipoProcedimentoRepository.findById(dto.getIdTipoProcedimento()).orElseThrow();
		pm.setDescLocal(dto.getDescLocal());
		pm.setDescricao(dto.getDescricao());
		pm.setDtRetorno(dto.getDtRetorno());
		pm.setDtProcedimento(dto.getDtProcedimento());
		pm.setTipoProcedimento(tp);
		pm.setDescLocal(dto.getDescLocal());
		pm.setTitulo(dto.getTitulo());
		this.procedimentoMedicoRepository.save(pm);
		return new ProcedimentoMedicoFormDTO(pm.getId(),pm.getTitulo(), pm.getDescLocal(), pm.getDescricao(), pm.getDtRetorno(), 
				pm.getDtProcedimento(), pm.getTipoProcedimento().getId());
	}
	
	public ProcedimentoMedicoFormDTO update(ProcedimentoMedicoFormDTO dto, User paciente) {
		UserSecurity logado = UserService.authenticated();
		if(logado!=null) {
			Long id = logado.getId();
			User profissionalSaude = this.userRepository.findById(id).orElseThrow();
			ProcedimentoMedico pm = this.procedimentoMedicoRepository.findById(dto.getId()).orElseThrow();
			TipoProcedimento tp = this.tipoProcedimentoRepository.findById(dto.getIdTipoProcedimento()).orElseThrow();
			pm.setDescLocal(dto.getDescLocal());
			pm.setDescricao(dto.getDescricao());
			pm.setDtRetorno(dto.getDtRetorno());
			pm.setDtProcedimento(dto.getDtProcedimento());
			pm.setTipoProcedimento(tp);
			pm.setDescLocal(dto.getDescLocal());
			pm.setTitulo(dto.getTitulo());
			pm.setProfissionalSaude(profissionalSaude);
			this.procedimentoMedicoRepository.save(pm);
			return new ProcedimentoMedicoFormDTO(pm.getId(),pm.getTitulo(), pm.getDescLocal(), pm.getDescricao(), pm.getDtRetorno(), 
					pm.getDtProcedimento(), pm.getTipoProcedimento().getId());
		}else {
			throw new NoElementException("Usuário inválido, tente logar novamente");
		}
		
	}

	public void delete(Long id) {
		ProcedimentoMedico pm = this.procedimentoMedicoRepository.findById(id).orElseThrow();
		UserSecurity logado = UserService.authenticated();
		if(logado!=null) {
			Long idUser = logado.getId();
			User usuario = this.userRepository.findById(idUser).orElseThrow();
			if(usuario.equals(pm.getUser())) {
				this.procedimentoMedicoRepository.delete(pm);
			}else {
				throw new DataIntegrityException("Usuário não autorizado a apagar tal recurso");
			}
		}else {
			throw new NoElementException("Usuário inválido, tente logar novamente");
		}
	}
}

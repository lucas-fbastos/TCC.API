package com.tcc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.DTO.ProntuarioDTO;
import com.tcc.domain.Consulta;
import com.tcc.domain.Prontuario;
import com.tcc.repository.ProntuarioRepository;
import com.tcc.service.exceptions.NoElementException;

@Service
public class ProntuarioService {

	@Autowired
	private ProntuarioRepository repository;
	
	@Autowired
	private ConsultaService consultaService;
	
	@Autowired
	private AlergiaService alergiaService;
	
	@Autowired
	private DoencaCronicaService doencaCronicaService;
	
	@Autowired
	private MedicamentoService medicamentoService;
	
	public Prontuario inciaAtendimento(Long idConsulta) {
		Consulta consulta = this.consultaService.verificaAtendimento(idConsulta);
		Optional<Prontuario> opProntuario = repository.findByConsulta(consulta);
		if(opProntuario.isPresent())
			return opProntuario.get();
		
		return cadastraProntuario(consulta);
	}

	public Prontuario cadastraProntuario(Consulta consulta) {
		Prontuario prontuario = new Prontuario();
		prontuario.setConsulta(consulta);
		return repository.save(prontuario);
	}

	public Prontuario save(ProntuarioDTO prontuario) {
		Prontuario p = getById(prontuario.getId());
		p.setConcordouTermos(prontuario.getConcordaTermos());
		p.setDescProntuario(prontuario.getDesc());
		p.setTemAlergia(prontuario.getTemAlergia());
		p.setDescSumario(prontuario.getSumario());
		p.setTemDoencaCronica(prontuario.getTemDoencaCronica());
		if(prontuario.getAlergias()!=null && !prontuario.getAlergias().isEmpty()) 
			this.alergiaService.addAlergia(prontuario.getAlergias(), p.getConsulta().getPaciente());
		
		if(prontuario.getMedicamentos()!=null && !prontuario.getMedicamentos().isEmpty()) 
			this.medicamentoService.addMedicamentos(prontuario.getMedicamentos(), p.getConsulta().getPaciente());
		
		if(prontuario.getDoencas()!=null && !prontuario.getDoencas().isEmpty()) 
			this.doencaCronicaService.save(prontuario.getDoencas(), p.getConsulta().getPaciente());
		
		return repository.save(p);
	}
	
	public Prontuario finalizaAtendimento(ProntuarioDTO prontuario) {
		Prontuario p = save(prontuario);
		Consulta consulta = this.consultaService.finalizaConsulta(p.getConsulta());
		p.setConsulta(consulta);
		return p;
	}
	
	public Prontuario getById(Long id) {
		Optional<Prontuario> p = repository.findById(id);
		if(p.isPresent())
			return p.get();
		throw new NoElementException("Prontuário não encontrado");
	}
}

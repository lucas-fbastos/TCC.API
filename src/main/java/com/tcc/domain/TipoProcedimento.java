package com.tcc.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tipo_procedimento")
public class TipoProcedimento implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="desc_tipo_procedimento")
	private String descTipoProcedimeto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescTipoProcedimeto() {
		return descTipoProcedimeto;
	}

	public void setDescTipoProcedimeto(String descTipoProcedimeto) {
		this.descTipoProcedimeto = descTipoProcedimeto;
	}

	public TipoProcedimento(Long id, String descTipoProcedimeto) {
		this.id = id;
		this.descTipoProcedimeto = descTipoProcedimeto;
	}

	public TipoProcedimento() {	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoProcedimento other = (TipoProcedimento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}

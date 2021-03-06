package com.milkpointapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.milkpointapi.model.Responsavel;
import com.milkpointapi.repository.ResponsavelRepository;

@Service
public class ResponsavelService {

	@Autowired
	private ResponsavelRepository repository;

	public Responsavel save(Responsavel Responsavel) {
		return repository.saveAndFlush(Responsavel);
	}

	public List<Responsavel> findAll() {
		return repository.findAll();
	}

	public Responsavel findOne(Long id) {
		return repository.getOne(id);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public Responsavel findByEmailAndPassword(String email, String senha) {
		return repository.findByEmailAndPassword(email, senha);
	}

	public Responsavel buscaLogin(String email) {
		return repository.findByEmailIgnoreCaseContaining(email);
	}

	public List<Responsavel> searchFor(String nome, String apelido, String cpf, String email) {
		return repository.findByNomeOrApelidoOrCpfOrEmailLike(nome, apelido, cpf, email);
	}
}

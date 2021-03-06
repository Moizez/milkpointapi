package com.milkpointapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.milkpointapi.model.Tecnico;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

	@Query
	public Tecnico findByEmailIgnoreCaseContaining(String email);

	@Query
	public Tecnico findByEmailAndPassword(String email, String senha);
	
	@Query(value = "select * from tecnico p where p.id =?", nativeQuery = true)
	public Tecnico getOne(Long id);

	@Query(value = "select * from tecnico p where p.apelido like concat('%', ?, '%')\n"
			+ "	|| p.nome like concat('%', ?, '%')\n" + " || p.cpf like concat('%', ?, '%')\n"
			+ " || p.email like concat('%', ?, '%')\n" + ";", nativeQuery = true)
	public List<Tecnico> findByNomeOrApelidoOrCpfOrEmailLike(String nome, String apelido, String cpf, String email);
}

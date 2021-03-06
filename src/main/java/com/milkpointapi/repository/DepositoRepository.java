package com.milkpointapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.milkpointapi.model.Deposito;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {

	@Query(value = "select * from deposito d where d.excluido = 0 and d.confirmacao = 0 ORDER BY data_now ASC", nativeQuery = true)
	public List<Deposito> buscaPendentes();

	@Query(value = "SELECT * FROM deposito d \n" + "inner join produtor p on (d.deposito_produtor = p.id)\n"
			+ "where (d.excluido = 0 and d.confirmacao = 0) and (p.id like concat ('%', :id, '%'))ORDER BY data_now ASC", nativeQuery = true)
	public List<Deposito> buscaPendentesPorProdutor(@Param("id") Long id);

	@Query(value = "select * from deposito d where d.excluido = 1 or d.confirmacao = 1 ORDER BY data_now DESC", nativeQuery = true)
	public List<Deposito> buscaResolvidos();

	@Query(value = "SELECT * FROM deposito d \n" + "inner join produtor p on (d.deposito_produtor = p.id)\n"
			+ "where (d.confirmacao = 1) and (p.id like concat ('%', :id, '%'))ORDER BY data_now DESC", nativeQuery = true)
	public List<Deposito> buscaConfirmados(@Param("id") Long id);

	@Query(value = "SELECT * FROM deposito d \n" + "inner join produtor p on (d.deposito_produtor = p.id)\n"
			+ "where (d.excluido = 1) and (p.id like concat ('%', :id, '%'))ORDER BY data_now DESC", nativeQuery = true)
	public List<Deposito> buscaCancelados(@Param("id") Long id);

	@Query(value = "select * from deposito d where d.confirmacao = 1 ORDER BY data_now ASC", nativeQuery = true)
	public List<Deposito> buscaTodosConfirmados();

	@Query(value = "select * from deposito d where d.excluido = 1 ORDER BY data_now ASC", nativeQuery = true)
	public List<Deposito> buscaTodosCancelados();

	@Query(value = "SELECT * FROM deposito d \n" + "inner join produtor p on(d.deposito_produtor = p.id)\n"
			+ "where (d.confirmacao or d.excluido) and (p.nome like concat('%', :nome, '%')) ORDER BY data_now DESC;", nativeQuery = true)
	public List<Deposito> buscaProdutor(@Param("nome") String nome);

	@Query(value = "SELECT * FROM deposito d where (d.deposito_tanque like concat('%', :id, '%'))\n"
			+ "and d.excluido = 0 and d.confirmacao = 0", nativeQuery = true)
	public List<Deposito> buscaDepositosPendentesPorTanque(@Param("id") Long id);

	@Query(value = "SELECT * FROM deposito d \n" + "inner join tanque t on(d.deposito_tanque = t.id)\n"
			+ "inner join responsavel r on (t.responsavel_id = r.id)\n"
			+ "where (r.id like concat('%', :id, '%')) and (d.excluido or d.confirmacao) ORDER BY data_now DESC;", nativeQuery = true)
	public List<Deposito> buscaDepositosPorTanqueResponsavel(@Param("id") Long id);
	
	@Query(value = "SELECT * FROM deposito d \n" + "inner join tanque t on(d.deposito_tanque = t.id)\n"
			+ "inner join responsavel r on (t.responsavel_id = r.id)\n"
			+ "where (r.id like concat('%', :id, '%')) and d.confirmacao = 1 ORDER BY data_now DESC;", nativeQuery = true)
	public List<Deposito> buscaDepositosConfirmadosPorTanqueResponsavel(@Param("id") Long id);
	
	@Query(value = "SELECT * FROM deposito d \n" + "inner join tanque t on(d.deposito_tanque = t.id)\n"
			+ "inner join responsavel r on (t.responsavel_id = r.id)\n"
			+ "where (r.id like concat('%', :id, '%')) and d.excluido = 1 ORDER BY data_now DESC;", nativeQuery = true)
	public List<Deposito> buscaDepositosCanceladosPorTanqueResponsavel(@Param("id") Long id);

}

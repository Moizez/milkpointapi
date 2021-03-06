package com.milkpointapi.api;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.milkpointapi.model.Laticinio;
import com.milkpointapi.model.Retirada;
import com.milkpointapi.model.Tanque;
import com.milkpointapi.service.LaticinioService;
import com.milkpointapi.service.RetiradaService;
import com.milkpointapi.service.TanqueService;

@RestController
@RequestMapping("/api")
public class RetiradaResource {

	@Autowired
	private RetiradaService service;

	@Autowired
	private TanqueService tanqueService;

	@Autowired
	private LaticinioService laticinioService;

	public ResponseEntity<Retirada> add(Retirada retirada) {

		if (retirada != null) {
			service.save(retirada);
			return ResponseEntity.ok(retirada);
		}

		return ResponseEntity.notFound().build();
	}

	public ZonedDateTime data() {
		ZonedDateTime data = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
		return data;
	}

	@PostMapping("/retirada")
	public ResponseEntity<Retirada> retirada(@RequestParam("quantidade") float quantidade,
			@RequestParam("idLat") Long idLat, @RequestParam("idTanque") Long idTanque) {
		Tanque tanque = tanqueService.findOne(idTanque);
		Laticinio laticinio = laticinioService.findOne(idLat);
		Retirada retirada = new Retirada();
		retirada.setDataNow(data());
		retirada.setDataSolicitacao(data());
		retirada.setLaticinio(laticinio);
		retirada.setTanque(tanque);
		retirada.setQuantidade(quantidade);
		tanque.setRetCount(tanque.getRetCount() + 1);
		tanque.setRetPendenteCount(tanque.getRetPendenteCount() + 1);
		retirada.setValor(quantidade * 0.84);
		tanqueService.save(tanque);
		return add(retirada);
	}

	@PostMapping("/retirada/confirmacao")
	public ResponseEntity<Retirada> confirmacao(@RequestParam("confirmacao") boolean confirmacao,
			@RequestParam("idRetirada") Long idRetirada, @RequestParam("whoCanceled") String nameWhoCanceled,
			@RequestParam("idWhoCanceled") Integer idWhoCanceled, @RequestParam("observacao") String observacao) {

		Retirada retirada = service.findOne(idRetirada);

		if (retirada != null) {
			retirada.setConfirmacao(confirmacao);
			retirada.setDataNow(data());

			if (confirmacao) {
				Tanque tanque = retirada.getTanque();
				tanque.setQtdAtual(tanque.getQtdAtual() - retirada.getQuantidade());
				tanque.setQtdRestante(tanque.getQtdRestante() + retirada.getQuantidade());
				tanque.setRetPendenteCount(tanque.getRetPendenteCount() - 1);
				tanqueService.save(tanque);
			} else {
				Tanque tanque = retirada.getTanque();
				retirada.setExcluido(true);
				retirada.setWhoCanceled(nameWhoCanceled);
				retirada.setIdWhoCanceled(idWhoCanceled);
				tanque.setRetPendenteCount(tanque.getRetPendenteCount() - 1);
				tanqueService.save(tanque);
				if (observacao.isEmpty())
					retirada.setObservacao(null);
				else
					retirada.setObservacao(observacao);
			}

			service.save(retirada);
			return ResponseEntity.ok(retirada);
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("/retirada/listatodos")
	public List<Retirada> listAll() {
		return service.findAll();
	}

	@GetMapping("/retirada/listapendentes")
	public List<Retirada> buscaPendentes() {
		return service.buscaPendentes();
	}

	@GetMapping("/retirada/resolvidos")
	public List<Retirada> buscaResolvidos() {
		return service.buscaResolvidos();
	}

	@GetMapping("/retirada/confirmados/{id}")
	public List<Retirada> buscaConfirmados(@PathVariable("id") Long id) {
		return service.buscaConfirmados(id);
	}

	@GetMapping("/retirada/cancelados/{id}")
	public List<Retirada> buscaCancelados(@PathVariable("id") Long id) {
		return service.buscaCancelados(id);
	}

	@GetMapping("/retirada/buscar/{nome}")
	public List<Retirada> buscaLaticinio(@PathVariable("nome") String nome) {
		return service.buscaLaticinio(nome);
	}

	@GetMapping("/retirada/confirmados")
	public List<Retirada> buscaTodosConfirmados() {
		return service.buscaTodosConfirmados();
	}

	@GetMapping("/retirada/cancelados")
	public List<Retirada> buscaTodosCancelados() {
		return service.buscaTodosCancelados();
	}

	@GetMapping("/retirada/pendentes/{id}")
	public List<Retirada> buscaPendentesPorLaticinio(@PathVariable("id") Long id) {
		return service.buscaPendentesPorLaticinio(id);
	}

	@GetMapping("/retirada/pendentes/tanque/{idTanque}")
	public List<Retirada> buscaRetiradasPendentesPorTanque(@PathVariable("idTanque") Long id) {
		return service.buscaRetiradasPendentesPorTanque(id);
	}

	@GetMapping("/retirada/resolvidos/responsavel/{id}")
	public List<Retirada> buscaRetiradasPorTanqueResponsavel(@PathVariable("id") Long id) {
		return service.buscaRetiradasPorTanqueResponsavel(id);
	}
	
	@GetMapping("/retirada/confirmados/responsavel/{id}")
	public List<Retirada> buscaRetiradasConfirmadasPorTanqueResponsavel(@PathVariable("id") Long id) {
		return service.buscaRetiradasConfirmadasPorTanqueResponsavel(id);
	}
	
	@GetMapping("/retirada/cancelados/responsavel/{id}")
	public List<Retirada> buscaRetiradasCanceladasPorTanqueResponsavel(@PathVariable("id") Long id) {
		return service.buscaRetiradasCanceladasPorTanqueResponsavel(id);
	}

}

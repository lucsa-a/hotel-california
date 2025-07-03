package br.edu.ufcg.computacao.p2lp2.hotelcalifornia;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller.*;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.Quarto;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao.Refeicao;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.Usuario;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HotelCaliforniaSistema {
	private FormaDePagamentoController formaDePagamentoController;
	private PagamentoController pagamentoController;
	private QuartoController quartoController;
	private RefeicaoController refeicaoController;
	private ReservasSessionController reservasSessionController;
	private UsuarioController usuarioController;
	private AreaComumController areaComumController;


	public HotelCaliforniaSistema() {
		// Inicialização de controllers
		this.formaDePagamentoController = FormaDePagamentoController.getInstance();
		this.formaDePagamentoController.init();

		this.pagamentoController = PagamentoController.getInstance();

		this.quartoController = QuartoController.getInstance();
		this.quartoController.init();

		this.refeicaoController = RefeicaoController.getInstance();
		this.refeicaoController.init();

		this.reservasSessionController = ReservasSessionController.getInstance();
		this.reservasSessionController.init();

		this.usuarioController = UsuarioController.getInstance();
		this.usuarioController.init();

		this.areaComumController = AreaComumController.getInstance();
		this.areaComumController.init();

	}

	// Métodos de Usuário
	public String cadastrarUsuario(String idAutenticacao, String nome, String tipoUsuario, long documento) {
		return this.usuarioController.cadastrarUsuario(idAutenticacao, nome, tipoUsuario, documento);
	}

	public String atualizarUsuario(String idAutenticacao, String idUsuario, String novoTipoUsuario) {
		return this.usuarioController.atualizarUsuario(idAutenticacao, idUsuario, novoTipoUsuario);
	}

	public String exibirUsuario(String idUsuario) {
		return this.usuarioController.exibirUsuario(idUsuario);
	}

	public String[] listarUsuarios() {
		return this.usuarioController.listarUsuarios();
	}

	// Métodos de Quarto
	public String disponibilizarQuartoSingle(String idAutenticacao, int idQuartoNum, double valorBasico, double valorPorPessoa) {
		return this.quartoController.disponibilizarQuartoSingle(idAutenticacao, idQuartoNum, valorBasico, valorPorPessoa);
	}

	public String disponibilizarQuartoDouble(String idAutenticacao, int idQuartoNum, double valorBasico, double valorPorPessoa, String[] melhorias) {
		return this.quartoController.disponibilizarQuartoDouble(idAutenticacao, idQuartoNum, valorBasico, valorPorPessoa, melhorias);
	}

	public String disponibilizarQuartoFamily(String idAutenticacao, int idQuartoNum, double valorBasico, double valorPorPessoa, String[] melhorias, int qtdeHospedes) {
		return this.quartoController.disponibilizarQuartoFamily(idAutenticacao, idQuartoNum, qtdeHospedes, valorBasico, valorPorPessoa, melhorias);
	}

	public String exibirQuarto(int idQuartoNum) {
		return this.quartoController.exibirQuarto(idQuartoNum);
	}

	public String[] listarQuartos() {
		return this.quartoController.listarQuartos();
	}

	// Métodos de Reserva de Quarto
	public String reservarQuartoSingle(String idAutenticacao, String idCliente, int idQuartoNum, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes) {
		return this.reservasSessionController.reservarQuartoSingle(idAutenticacao, idCliente, idQuartoNum, dataInicio, dataFim, idRefeicoes);
	}

	public String reservarQuartoDouble(String idAutenticacao, String idCliente, int idQuartoNum, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes, String[] pedidos) {
		return this.reservasSessionController.reservarQuartoDouble(idAutenticacao, idCliente, idQuartoNum, dataInicio, dataFim, idRefeicoes, pedidos);
	}

	public String reservarQuartoFamily(String idAutenticacao, String idCliente, int idQuartoNum, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes, String[] pedidos, int qtdeHospedes) {
		return this.reservasSessionController.reservarQuartoFamily(idAutenticacao, idCliente, idQuartoNum, qtdeHospedes, dataInicio, dataFim, idRefeicoes, pedidos);
	}

	// Métodos de Visualizar Reserva
	public String exibirReserva(String idUsuario, Long idReserva) {
		return this.reservasSessionController.exibirReserva(idUsuario, idReserva);
	}

	public String[] listarReservasAtivasDoCliente(String idUsuario, String idCliente) {
		return this.reservasSessionController.listarReservasCliente(idUsuario, idCliente);
	}

	public String[] listarReservasAtivasDoClientePorTipo(String idUsuario, String idCliente, String tipo) {
		return this.reservasSessionController.listarReservasClienteTipo(idUsuario, idCliente, tipo);
	}

	public String[] listarReservasAtivasPorTipo(String idUsuario, String tipo) {
		return this.reservasSessionController.listarReservasTipo(idUsuario, tipo);
	}

	public String[] listarReservasAtivas(String idCliente) {
		return this.reservasSessionController.listarReservasAtivas(idCliente);
	}

	// Métodos de Refeição
	public String disponibilizarRefeicao(String idAutenticacao, String tipoRefeicao, String titulo, LocalTime horarioInicio, LocalTime horarioFim, double valor, boolean isDisponivel) {
		return this.refeicaoController.disponibilizarRefeicao(idAutenticacao, tipoRefeicao, titulo, horarioInicio, horarioFim, valor, isDisponivel);
	}

	public String alterarRefeicao(Long idRefeicao, LocalTime horarioInicio, LocalTime horarioFim, double valorPorPessoa, boolean isDisponivel) {
		return this.refeicaoController.alterarRefeicao(idRefeicao, horarioInicio, horarioFim, valorPorPessoa, isDisponivel);
	}

	public String exibirRefeicao(Long idRefeicao) {
		return this.refeicaoController.exibirRefeicao(idRefeicao);
	}

	public String[] listarRefeicoes() {
		return this.refeicaoController.listarRefeicoes();
	}

	// Método de Restaurante
	public String reservarRestaurante(String idAutenticacao, String idCliente, LocalDateTime dataInicio, LocalDateTime dataFim, int qtdPessoas, String tipoRefeicao) {
			return this.reservasSessionController.reservarRestaurante(idAutenticacao, idCliente, dataInicio, dataFim, qtdPessoas, tipoRefeicao);
	}

	// Métodos de Forma de Pagamento
	public String disponibilizarFormaDePagamento(String idAutenticacao, String formaPagamento, double percentualDesconto) {
		return this.formaDePagamentoController.disponibilizarFormaDePagamento(idAutenticacao, formaPagamento, percentualDesconto);
	}

	public String alterarFormaDePagamento(String idAutenticacao, int idFormaPagamento, String formaPagamento, double percentualDesconto) {
		return this.formaDePagamentoController.alterarFormaDePagamento(idAutenticacao, idFormaPagamento, formaPagamento, percentualDesconto);
	}

	public String exibirFormaPagamento(int idFormaDePagamento) {
		return this.formaDePagamentoController.exibirFormaPagamento(idFormaDePagamento);
	}

	public String[] listarFormasPagamentos() {
		return this.formaDePagamentoController.listarFormasPagamentos();
	}

	// Métodos de Pagamento
	public String pagarReservaComDinheiro(String idCliente, long idReserva, String nomeTitular) {
		return this.pagamentoController.realizarPagamentoDinheiro(idCliente, idReserva, nomeTitular);
	}

	public String pagarReservaComCartao(String idCliente, long idReserva, String nomeTitular, String numCartao, String validade, String codigoDeSeguranca, int qtdeParcelas) {
		return this.pagamentoController.realizarPagamentoCartao(idCliente, idReserva, nomeTitular, numCartao, validade, codigoDeSeguranca, qtdeParcelas);
	}

	public String pagarReservaComPix(String idCliente, long idReserva, String nomeTitular, String cpf, String banco) {
		return this.pagamentoController.realizarPagamentoPix(idCliente, idReserva, nomeTitular, cpf, banco);
	}

	// Métodos de Cancelar Reserva
	public String cancelarReserva(String idAutenticacao, String idReserva) {
		return this.reservasSessionController.cancelarReserva(idAutenticacao, idReserva);
	}

	//Métodos de Área Comum
	public String disponibilizarAreaComum(String idAutenticacao, String tipoAreaComum, String titulo, LocalTime horarioInicio, LocalTime horarioFinal, double valorPessoa, boolean disponivel, int qtdMaxPessoas) {
		return this.areaComumController.disponibilizarAreaComum(idAutenticacao, tipoAreaComum, titulo, horarioInicio, horarioFinal, valorPessoa, disponivel, qtdMaxPessoas);
	}

	public String alterarAreaComum(String idAutenticacao, long idAreaComum, LocalTime novoHorarioInicio, LocalTime novoHorarioFinal, double novoPreco, int capacidadeMax, boolean ativa) {
		return this.areaComumController.alterarAreaComum(idAutenticacao, idAreaComum, novoHorarioInicio, novoHorarioFinal, novoPreco, capacidadeMax, ativa);
	}

	public String exibirAreaComum(long idAreaComum) {
		return this.areaComumController.exibirAreaComum(idAreaComum);
	}

	public String[] listarAreasComuns() {
		return this.areaComumController.listarAreasComuns();
	}

	//Método de Reservar Auditório

	public String reservarAuditorio(String idAutenticacao, String idCliente, long idAuditorio, LocalDateTime dataInicio, LocalDateTime dataFim, int qtdPessoas) {
		return this.reservasSessionController.reservarAuditorio(idAutenticacao, idCliente, Long.valueOf(idAuditorio), dataInicio, dataFim, qtdPessoas);
	}

}

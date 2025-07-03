package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.areaComum.AreaComum;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.Quarto;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao.Refeicao;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva.Reserva;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva.ReservaAuditorio;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva.ReservaQuarto;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva.ReservaRestaurante;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.Usuario;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservasSessionController {

    private static ReservasSessionController instance;
    private RefeicaoController refeicaoController;
    private UsuarioController usuarioController;
    private QuartoController quartoController;
    private AreaComumController areaComumController;
    private static final int QTD_HOSPEDES_SINGLE = 1;
    private static final int QTD_HOSPEDES_DOUBLE = 2;
    private static final String TIPO_RESERVA_QUARTO = "QUARTO";
    private Map<Long, Reserva> reservas;

    public ReservasSessionController() {
        this.reservas = new HashMap<>();

        this.refeicaoController = RefeicaoController.getInstance();
        this.refeicaoController.init();

        this.usuarioController = UsuarioController.getInstance();
        this.usuarioController.init();

        this.quartoController = QuartoController.getInstance();
        this.quartoController.init();

        this.areaComumController = AreaComumController.getInstance();
        this.areaComumController.init();
    }

    public static ReservasSessionController getInstance() {
        if (instance == null) {
            instance = new ReservasSessionController();
        }
        return instance;
    }

    public Reserva getReserva(long id) {
        return this.reservas.get(id);
    }

    public Map<Long, Reserva> getReservas() {
        return this.reservas;
    }

    //Reservas de Quarto
    public String reservarQuartoSingle(String idAutenticacao, String idCliente, int idQuartoNum, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioReserva(idAutenticacao, new String[]{"GER", "FUN"});
        Validador.validarUsuarioCliente(idCliente);
        Validador.validarDataFimPosteriorDataInicio(dataInicio, dataFim);
        Validador.validarPeriodoMinimoAntecedenciaReserva(dataInicio);

        Usuario cliente = this.usuarioController.getUsuario(idCliente);

        Validador.validarReservaQuartoDisponivel(idQuartoNum, dataInicio, dataFim);
        Quarto quarto = this.quartoController.getQuarto(idQuartoNum);

        List<Refeicao> refeicoes = new ArrayList<>();
        for (String idRefeicao : idRefeicoes) {
            refeicoes.add(this.refeicaoController.getRefeicao(Long.parseLong(idRefeicao)));
        }

        Reserva reserva = new ReservaQuarto(Long.valueOf(reservas.size() + 1), cliente, quarto, QTD_HOSPEDES_SINGLE, dataInicio, dataFim, refeicoes);
        this.reservas.put(Long.valueOf(reservas.size() + 1), reserva);

        return reserva.toString();
    }

    public String reservarQuartoDouble(String idAutenticacao, String idCliente, int idQuartoNum, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes, String[] pedidos) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioReserva(idAutenticacao, new String[]{"GER", "FUN"});
        Validador.validarUsuarioCliente(idCliente);
        Validador.validarDataFimPosteriorDataInicio(dataInicio, dataFim);
        Validador.validarPeriodoMinimoAntecedenciaReserva(dataInicio);

        Usuario cliente = this.usuarioController.getUsuario(idCliente);

        Validador.validarReservaQuartoDisponivel(idQuartoNum, dataInicio, dataFim);
        Quarto quarto = this.quartoController.getQuarto(idQuartoNum);

        List<Refeicao> refeicoes = new ArrayList<>();
        for (String idRefeicao : idRefeicoes) {
            refeicoes.add(this.refeicaoController.getRefeicao(Long.parseLong(idRefeicao)));
        }

        Reserva reserva = new ReservaQuarto(Long.valueOf(reservas.size() + 1), cliente, quarto, QTD_HOSPEDES_DOUBLE, dataInicio, dataFim, refeicoes, pedidos);
        this.reservas.put(Long.valueOf(reservas.size() + 1), reserva);

        return reserva.toString();
    }

    public String reservarQuartoFamily(String idAutenticacao, String idCliente, int idQuartoNum, int qtdHospedes, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes, String[] pedidos) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioReserva(idAutenticacao, new String[]{"GER", "FUN"});
        Validador.validarUsuarioCliente(idCliente);
        Validador.validarDataFimPosteriorDataInicio(dataInicio, dataFim);
        Validador.validarPeriodoMinimoAntecedenciaReserva(dataInicio);

        Usuario cliente = this.usuarioController.getUsuario(idCliente);

        Validador.validarReservaQuartoDisponivel(idQuartoNum, dataInicio, dataFim);
        Quarto quarto = this.quartoController.getQuarto(idQuartoNum);

        List<Refeicao> refeicoes = new ArrayList<>();
        for (String idRefeicao : idRefeicoes) {
            refeicoes.add(this.refeicaoController.getRefeicao(Long.parseLong(idRefeicao)));
        }

        Validador.validarQtdeHospedesQuarto(qtdHospedes);
        Validador.validarQtdeHospedesReserva(qtdHospedes, quarto.getQtdeHospedes());

        Reserva reserva = new ReservaQuarto(Long.valueOf(reservas.size() + 1), cliente, quarto, qtdHospedes, dataInicio, dataFim, refeicoes, pedidos);
        this.reservas.put(Long.valueOf(reservas.size() + 1), reserva);

        return reserva.toString();
    }

    //Reserva de Restaurante
    public String reservarRestaurante(String idAutenticacao, String idCliente, LocalDateTime dataInicio, LocalDateTime dataFim, int qtdPessoas, String idRefeicao) {
        Validador.validaNuloOuVazio(idAutenticacao);
        Validador.validarQtdeHospedesReserva(qtdPessoas, 50);
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioReserva(idAutenticacao, new String[]{"GER", "FUN"});
        Validador.validarUsuarioCliente(idCliente);
        Validador.validarDataFimPosteriorDataInicio(dataInicio, dataFim);
        Validador.validarValor(qtdPessoas);
        Validador.validarPeriodoMinimoAntecedenciaReserva(dataInicio);
        Validador.validarReservaDisponivel("RESTAURANTE", dataInicio, dataFim);

        Refeicao refeicao = refeicaoController.getRefeicao(Long.parseLong(idRefeicao));
        Usuario cliente = usuarioController.getUsuario(idCliente);

        Reserva reserva = new ReservaRestaurante(Long.valueOf(reservas.size() + 1), cliente, dataInicio, dataFim, qtdPessoas, refeicao);
        this.reservas.put(Long.valueOf(reservas.size() + 1), reserva);
        return reserva.toString();
    }

    //Reserva de Auditório
    public String reservarAuditorio(String idAutenticacao, String idCliente, long idAuditorio, LocalDateTime dataInicio, LocalDateTime dataFim, int qtdPessoas) {
        Validador.validaNuloOuVazio(idAutenticacao);
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioReserva(idAutenticacao, new String[]{"GER", "FUN"});
        Validador.validarUsuarioCliente(idCliente);
        Validador.validarDataFimPosteriorDataInicio(dataInicio, dataFim);
        Validador.validarValor(qtdPessoas);
        Validador.validarQtdeHospedesReserva(qtdPessoas, 150);
        Validador.validarPeriodoMinimoAntecedenciaReserva(dataInicio);
        Validador.validarReservaDisponivel("AUDITORIO", dataInicio, dataFim);

        Usuario cliente = usuarioController.getUsuario(idCliente);
        AreaComum auditorio = areaComumController.getAreaComum(idAuditorio);

        if (auditorio.isDisponivel()) {
            Reserva reserva = new ReservaAuditorio(Long.valueOf(reservas.size() + 1), cliente, auditorio, dataInicio, dataFim, qtdPessoas);
            this.reservas.put(Long.valueOf(reservas.size() + 1), reserva);
            return reserva.toString();
        } else {
            throw new HotelCaliforniaException("AUDITORIO INDISPONIVEL");
        }
    }

    //Cancelar Reserva
    public String cancelarReserva(String idAutenticacao, String idReserva) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioCancelarReserva(idAutenticacao, new String[]{"CLI"});
        Validador.validarReservaExistente(Long.parseLong(idReserva));

        Reserva reserva = this.getReserva(Long.parseLong(idReserva));
        if (!reserva.getCliente().getId().equals(idAutenticacao)) {
            throw new HotelCaliforniaException("SOMENTE O PROPRIO CLIENTE PODERA CANCELAR A SUA RESERVA");
        }

        Validador.validarPeriodoMinimoAntecedenciaReserva(reserva.getDataInicio());

        StringBuffer out = new StringBuffer();
        out.append("[CANCELADA] ").append(reserva);

        this.reservas.remove(idReserva);

        return out.toString();
    }

    //Exibir Reserva
    public String exibirReserva(String idAutenticacao, Long idReserva) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioExibicao(idAutenticacao, new String[]{"GER", "FUN", "CLI"});
        Validador.validarReservaExistente(idReserva);

        return this.reservas.get(idReserva).toString();
    }

    //Listagens de Reserva
    public String[] listarReservasCliente(String idAutenticacao, String idUsuario) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioExibicao(idAutenticacao, new String[]{"GER", "FUN", "CLI"});
        Validador.validarUsuarioCliente(idUsuario);

        ArrayList<String> reservas = new ArrayList<>();

        for (Reserva reserva : this.reservas.values()) {
            if (reserva.getCliente().getId().equals(idUsuario)) {
                reservas.add(reserva.toString());
            }
        }

        Validador.validarReservaExistente(reservas);
        return reservas.toArray(new String[reservas.size()]);
    }

    public String[] listarReservasClienteTipo(String idAutenticacao, String idUsuario, String tipo) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioExibicao(idAutenticacao, new String[]{"GER", "FUN", "CLI"});
        Validador.validarUsuarioCliente(idUsuario);
        Validador.validarTipoReserva(tipo);

        ArrayList<String> reservas = new ArrayList<>();

        for (Reserva reserva : this.reservas.values()) {
            if (reserva.getCliente().getId().equals(idUsuario) && reserva.verificarTipoReserva(tipo)) {
                reservas.add(reserva.toString());
            }
        }

        Validador.validarReservaExistente(reservas);
        return reservas.toArray(new String[reservas.size()]);
    }

    public String[] listarReservasTipo(String idAutenticacao, String tipo) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioExibicao(idAutenticacao, new String[]{"GER", "FUN", "CLI"});
        Validador.validarTipoReserva(tipo);

        ArrayList<String> reservas = new ArrayList<>();

        for (Reserva reserva : this.reservas.values()) {
            if (reserva.verificarTipoReserva(tipo)) {
                reservas.add(reserva.toString());
            }
        }

        Validador.validarReservaExistente(reservas);
        return reservas.toArray(new String[reservas.size()]);
    }

    public String[] listarReservasAtivas(String idAutenticacao) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioExibicao(idAutenticacao, new String[]{"GER", "FUN", "CLI"});

        ArrayList<String> reservas = new ArrayList<>();

        for (Reserva reserva : this.reservas.values()) {
            reservas.add(reserva.toString());
        }

        Validador.validarReservaExistente(reservas);
        return reservas.toArray(new String[reservas.size()]);
    }

    //Verificações
    public boolean verificarReservaQuartoDisponivel(int idQuarto, LocalDateTime dataInicio, LocalDateTime dataFim) {
        for (Reserva reserva : this.reservas.values()) {
            if (reserva.verificarTipoReserva(TIPO_RESERVA_QUARTO) && ((ReservaQuarto) reserva).verificarIdQuarto(idQuarto)) {
                if (dataInicio.isBefore(reserva.getDataFim()) && dataFim.isAfter(reserva.getDataInicio())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean verificarReservaDisponivel(String tipo, LocalDateTime dataInicio, LocalDateTime dataFim) {
        for (Reserva reserva : this.reservas.values()) {
            if (reserva.verificarTipoReserva(tipo.toUpperCase())) {
                if (reserva.getDataInicio().isBefore(dataFim) && reserva.getDataFim().isAfter(dataInicio)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean verificarReservaExistente(Long idReserva) {
        return this.reservas.containsKey(idReserva);
    }

    public void init() {
        this.reservas.clear();
    }
}
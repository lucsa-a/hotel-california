package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller.QuartoController;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller.ReservasSessionController;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller.UsuarioController;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.Quarto;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Validador {

    //Validações de Usuário
    public static void validarIdUsuario(String idAutenticacao) {
        validaNuloOuVazio(idAutenticacao);
        UsuarioController usuarioController = UsuarioController.getInstance();

        if (!usuarioController.verificarIdValido(idAutenticacao)) {
            throw new HotelCaliforniaException("USUARIO NAO EXISTE");
        }
    }
    public static void validarUsuarioAdministrador(String idAutenticacao) {
        UsuarioController usuarioController = UsuarioController.getInstance();

        if (!usuarioController.verificarTipoUsuario(idAutenticacao, "ADM")) {
            throw new HotelCaliforniaException("USUARIO NAO E ADMINISTRADOR");
        }
    }

    public static void validarUsuarioCliente(String idAutenticacao) {
        validaNuloOuVazio(idAutenticacao);
        validarIdUsuario(idAutenticacao);
        UsuarioController usuarioController = UsuarioController.getInstance();

        if (!usuarioController.verificarTipoUsuario(idAutenticacao, "CLI")) {
            throw new HotelCaliforniaException("USUARIO NAO E CLIENTE");
        }
    }

    public static void validarTipoUsuarioReserva(String idAutenticacao, String[] tipos) {
        UsuarioController usuarioController = UsuarioController.getInstance();

        boolean hasTipoUsuario = false;
        for (String tipo : tipos) {
            if (usuarioController.verificarTipoUsuario(idAutenticacao, tipo)) {
                hasTipoUsuario = true;
                break;
            }
        }

        if (!hasTipoUsuario) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA RESERVA");
        }
    }

    public static void validarTipoUsuarioCancelarReserva(String idAutenticacao, String[] tipos) {
        UsuarioController usuarioController = UsuarioController.getInstance();

        boolean hasTipoUsuario = false;
        for (String tipo : tipos) {
            if (usuarioController.verificarTipoUsuario(idAutenticacao, tipo)) {
                hasTipoUsuario = true;
                break;
            }
        }

        if (!hasTipoUsuario) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CANCELAR UMA RESERVA");
        }
    }
    public static void validarTipoUsuarioRefeicao(String idAutenticacao, String[] tipos) {
        UsuarioController usuarioController = UsuarioController.getInstance();

        boolean hasTipoUsuario = false;
        for (String tipo : tipos) {
            if (usuarioController.verificarTipoUsuario(idAutenticacao, tipo)) {
                hasTipoUsuario = true;
                break;
            }
        }

        if (!hasTipoUsuario) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA REFEICAO");
        }
    }

    public static void validarTipoUsuarioExibicao(String idAutenticacao, String[] tipos) {
        boolean hasTipoUsuario = false;
        for (String tipo : tipos) {
            if (idAutenticacao.contains(tipo)) {
                hasTipoUsuario = true;
                break;
            }
        }

        if (!hasTipoUsuario) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO EXIBIR/LISTAR RESERVA(S) DO CLIENTE");
        }
    }

    public static void validarUsuarioFormaDePagamento(String idAutenticacao) {
        UsuarioController usuarioController = UsuarioController.getInstance();

        if(!usuarioController.getUsuario(idAutenticacao).getTipo().equals("ADM")) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA FORMA DE PAGAMENTO");
        }
    }

    public static void validarUsuarioAreaComum(String idAutenticacao) {
        UsuarioController usuarioController = UsuarioController.getInstance();

        if(!usuarioController.getUsuario(idAutenticacao).getTipo().equals("ADM")) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA AREA COMUM");
        }
    }

    public static void validarUsuarioAltrerarAreaComum(String idAutenticacao) {
        UsuarioController usuarioController = UsuarioController.getInstance();

        if(!usuarioController.getUsuario(idAutenticacao).getTipo().equals("ADM")) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO ALTERAR UMA AREA COMUM");
        }
    }

    //Validações de Reserva
    public static void validarTipoReserva(String tipo) {
        if(!tipo.equals("QUARTO") && !tipo.equals("RESTAURANTE") && !tipo.equals("AUDITORIO")) {
            throw new HotelCaliforniaException("TIPO INVÁLIDO DE RESERVA");
        }
    }

    public static void validarReservaExistente(Long idReserva) {
        ReservasSessionController reservasSessionController = ReservasSessionController.getInstance();
        if (!reservasSessionController.verificarReservaExistente(idReserva)) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
    }

    public static void validarReservaExistente(List<String> reservas) {
        if (reservas.isEmpty()) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
    }

    public static void validarReservaQuartoDisponivel(int idQuarto, LocalDateTime dataInicio, LocalDateTime dataFim) {
        ReservasSessionController reservasSessionController = ReservasSessionController.getInstance();

        if (!reservasSessionController.verificarReservaQuartoDisponivel(idQuarto, dataInicio, dataFim)) {
            throw new HotelCaliforniaException("JA EXISTE RESERVA PARA ESTA DATA");
        }
    }

    public static void validarReservaDisponivel(String tipo, LocalDateTime dataInicio, LocalDateTime dataFim) {
        ReservasSessionController reservasSessionController = ReservasSessionController.getInstance();

        if (!reservasSessionController.verificarReservaDisponivel(tipo, dataInicio, dataFim)) {
            throw new HotelCaliforniaException("JA EXISTE RESERVA PARA ESTA DATA");
        }
    }

    public static void validarPeriodoMinimoAntecedenciaReserva(LocalDateTime dataInicio) {
        LocalDateTime today = LocalDateTime.now();

        Duration duracao = Duration.between(today, dataInicio);
        if (duracao.toDays() < 1) {
            throw new HotelCaliforniaException("NECESSARIO ANTECEDENCIA MINIMA DE 01 (UM) DIA");
        }
    }

    public static void validarQtdeHospedesReserva(int qtdeHospedes, int qtdeHospedesLimite) {
        if (qtdeHospedes > qtdeHospedesLimite) {
            throw new HotelCaliforniaException("CAPACIDADE EXCEDIDA");
        }
    }

    //Validações de Quarto
    public static void validarQuartoExistente(int id) {
        QuartoController quartoController = QuartoController.getInstance();
        if (quartoController.verificarQuartoExistente(id)) {
            throw new HotelCaliforniaException("QUARTO JA EXISTE");
        }
    }
    public static void validarQtdeHospedesQuarto(int qtdeHospedes) {
        if (qtdeHospedes <= 0) {
            throw new HotelCaliforniaException("QUANTIDADE DE HOSPÉDES DEVE SER NO MÍNIMO 1");
        }

        if (qtdeHospedes > 10) {
            throw new HotelCaliforniaException("QUANTIDADE DE HOSPÉDES DEVE SER NO MÁXIMO 10");
        }
    }

    //Validação de Data
    public static void validarDataFimPosteriorDataInicio(LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (dataInicio.isAfter(dataFim)) {
            throw new HotelCaliforniaException("DATA FINAL DEVE SER POSTERIOR A DATA INICIAL");
        }
    }

    //Validação de horário
    public static void validarHorarioFimPosteriorHorarioInicio(LocalTime horarioInicio, LocalTime horarioFim) {
        if (horarioInicio.isAfter(horarioFim)) {
            throw new HotelCaliforniaException("HORARIO DE FIM DEVE SER POSTERIOR AO HORARIO DE INICIO");
        }
    }

    //Validações de Valor
    public static void validarValor(double valor) {
        if (valor <= 0.0) {
            throw new HotelCaliforniaException("VALOR INVÁLIDO");
        }
    }

    //Valida nulo ou vazio
    public static void validaNuloOuVazio(String texto) {
        if (texto == null) {
            throw new NullPointerException();
        }

        if (texto.trim().equals("")) {
            throw new IllegalArgumentException();
        }
    }

}

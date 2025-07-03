package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.areaComum.AreaComum;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;

import java.time.LocalTime;
import java.util.*;

import static br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador.*;

public class AreaComumController {
    private static AreaComumController instance;
    private UsuarioController usuarioController;
    private Map<Long, AreaComum> areasComuns;

    public AreaComumController() {
        this.usuarioController = UsuarioController.getInstance();
        this.usuarioController.init();
        this.areasComuns = new HashMap<>();
    }

    public String disponibilizarAreaComum(String idAutenticacao, String tipoAreaComum, String titulo, LocalTime horarioInicio, LocalTime horarioFinal, double valorPessoa, boolean disponivel, int qtdMaxPessoas) {
        validarIdUsuario(idAutenticacao);
        validarUsuarioAreaComum(idAutenticacao);

        if(verificarTituloAreaComum(titulo)) {
            throw new HotelCaliforniaException("AREA COMUM JA EXISTE");
        }

        this.areasComuns.put(Long.valueOf(areasComuns.size() + 1), new AreaComum((areasComuns.size() + 1), tipoAreaComum, titulo, horarioInicio, horarioFinal, valorPessoa, disponivel, qtdMaxPessoas));

        return getAreaComum(areasComuns.size()).toString();
    }

    public String alterarAreaComum(String idAutenticacao, long idAreaComum, LocalTime novoHorarioInicio, LocalTime novoHorarioFinal, double novoPreco, int capacidadeMax, boolean ativa) {
        validarIdUsuario(idAutenticacao);
        validarUsuarioAltrerarAreaComum(idAutenticacao);

        if(!verificarAreaComum(idAreaComum)) {
            throw new HotelCaliforniaException("AREA COMUM NAO EXISTE");
        }

        validarHorarioFimPosteriorHorarioInicio(novoHorarioInicio, novoHorarioFinal);

        if(novoPreco < 0) {
            throw new HotelCaliforniaException("VALOR POR PESSOA INVALIDO");
        }

        validarValor(capacidadeMax);

        this.areasComuns.get(idAreaComum).alterarDados(novoHorarioInicio, novoHorarioFinal, novoPreco, capacidadeMax, ativa);

        return getAreaComum(idAreaComum).toString();

    }

    public String exibirAreaComum(long idAreaComum) {
        if(!verificarAreaComum(idAreaComum)) {
            throw new HotelCaliforniaException("AREA COMUM NAO EXISTE");
        }

        return getAreaComum(idAreaComum).toString();
    }

    public String[] listarAreasComuns() {
        String[] out = new String[areasComuns.size()];
        int i = 0;

        for(AreaComum a: areasComuns.values()) {
            out[i] = a.toString();
            i++;
        }

        return out;
    }

    public AreaComum getAreaComum(long idAreaComum) {
        return this.areasComuns.get(idAreaComum);
    }

    public boolean verificarAreaComum(long id) {
        if (this.areasComuns.containsKey(id)) {
            return true;
        }

        return false;
    }

    public int qtdAreasComuns() {
        return this.areasComuns.size();
    }

    private boolean verificarTituloAreaComum(String titulo) {
        for(AreaComum a: areasComuns.values()) {
            if (a.getTitulo().equals(titulo)) {
                return true;
            }
        }

        return false;
    }

    public static AreaComumController getInstance() {
        if (instance == null) {
            instance = new AreaComumController();
        }
        return instance;
    }

    public void init() {
        this.areasComuns.clear();
    }
}

package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.QuartoDouble;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.QuartoFamily;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.Quarto;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.QuartoSingle;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador;

import java.util.HashMap;
import java.util.Map;

public class QuartoController {

    private static QuartoController instance;

    private Map<Integer, Quarto> quartos;

    public QuartoController() {
        this.quartos = new HashMap<>();
    }

    public static QuartoController getInstance() {
        if (instance == null) {
            instance = new QuartoController();
        }
        return instance;
    }

    public Quarto getQuarto(int id) {
        return this.quartos.get(id);
    }

    public String disponibilizarQuartoSingle(String idAutenticacao, int id, double valorBasico, double valorPorPessoa) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarUsuarioAdministrador(idAutenticacao);
        Validador.validarQuartoExistente(id);

        Quarto quarto = new QuartoSingle(id, valorBasico, valorPorPessoa);
        this.quartos.put(id, quarto);

        return quarto.toString();
    }


    public String disponibilizarQuartoDouble(String idAutenticacao, int id, double valorBasico, double valorPorPessoa, String[] melhorias) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarUsuarioAdministrador(idAutenticacao);
        Validador.validarQuartoExistente(id);

        Quarto quarto = new QuartoDouble(id, valorBasico, valorPorPessoa, melhorias);
        this.quartos.put(id, quarto);

        return quarto.toString();
    }

    public String disponibilizarQuartoFamily(String idAutenticacao, int id, int qtdeHospedes, double valorBasico, double valorPorPessoa, String[] melhorias) {
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarUsuarioAdministrador(idAutenticacao);
        Validador.validarQuartoExistente(id);

        Quarto quarto = new QuartoFamily(id, qtdeHospedes, valorBasico, valorPorPessoa, melhorias);
        this.quartos.put(id, quarto);

        return quarto.toString();
    }

    public String exibirQuarto(int id) {
        if (!this.quartos.containsKey(id)) {
            throw new HotelCaliforniaException("NÚMERO DE QUARTO INVÁLIDO");
        }
        return this.quartos.get(id).toString();
    }

    public String[] listarQuartos() {
        String[] out = new String[this.quartos.size()];

        int i = 0;
        for (Quarto quarto : this.quartos.values()) {
            out[i] = quarto.toString();
            i++;
        }

        return out;
    }

    public boolean verificarQuartoExistente(int id) {
        if (this.quartos.containsKey(id)) {
            return true;
        }
        return false;
    }

    public void init() {
        this.quartos.clear();
    }
}

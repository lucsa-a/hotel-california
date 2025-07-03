package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.*;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador;

import java.util.*;

import static br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador.validaNuloOuVazio;
import static br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador.validarIdUsuario;

public class UsuarioController {

    private static UsuarioController instance;
    private Map<String, Usuario> usuarios;

    private UsuarioController() {
        //Cria listas de usuários.
        this.usuarios = new HashMap<>();

        //Cria o Administrador inicial.
        this.usuarios.put("ADM1", new Usuario("ADM1", "Joao Costa", 123456, "ADM"));
    }

    public static UsuarioController getInstance() {
        if (instance == null) {
            instance = new UsuarioController();
        }
        return instance;
    }

    public String cadastrarUsuario(String idAutenticacao, String nome, String tipoUsuario, long documento) {
        validarIdUsuario(idAutenticacao);
        verificarTipoValido(tipoUsuario);

        if (verificarTipoUsuario(idAutenticacao, "CLI")) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO " + idAutenticacao + " CADASTRAR UM NOVO USUARIO DO TIPO " + tipoUsuario);
        }

        if (verificarGerente() && tipoUsuario.equals("GER")) {
            throw new HotelCaliforniaException("SO DEVE HAVER UM GERENTE NO HOTEL");
        }

        String idUsuario = tipoUsuario + (usuarios.size() + 1);

        if(tipoUsuario.equals("ADM") || tipoUsuario.equals("GER")) {
            if(!verificarTipoUsuario(idAutenticacao, "ADM")) {
                throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO " + idAutenticacao + " CADASTRAR UM NOVO USUARIO DO TIPO " + tipoUsuario);
            }
        }

        if(tipoUsuario.equals("FUN")) {
            if(verificarTipoUsuario(idAutenticacao, "FUN")) {
                throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO " + idAutenticacao + " CADASTRAR UM NOVO USUARIO DO TIPO " + tipoUsuario);
            }
        }

        Usuario novoUsuario = new Usuario(idUsuario, nome, documento, tipoUsuario);
        this.usuarios.put(idUsuario, novoUsuario);

        return novoUsuario.toString();

    }

    public String atualizarUsuario(String idAutenticacao, String idUsuario, String novoTipoUsuario) {
        validaNuloOuVazio(novoTipoUsuario);
        validarIdUsuario(idAutenticacao);
        validarIdUsuario(idUsuario);
        verificarTipoValido(novoTipoUsuario);

        if (!this.usuarios.get(idAutenticacao).getTipo().equals("ADM")) {
            throw new HotelCaliforniaException("APENAS O ADMINISTRADOR PODE ATUALIZAR OS USUARIOS");
        }

        if (verificarTipoUsuario(idUsuario, "CLI")) {
            throw new HotelCaliforniaException("CLIENTE NÃO PODE TER SEU TIPO ATUALIZADO");
        }

        String novoId = novoTipoUsuario + (usuarios.size() + 1);

        if (verificarGerente() && novoTipoUsuario.equals("GER")) {
            atualizarUsuario(idAutenticacao, getIdGerente(), "FUN");
        }

        this.usuarios.get(idUsuario).setTipo(novoTipoUsuario);
        this.usuarios.get(idUsuario).setId(novoId);
        Usuario usuarioAtualizado = this.usuarios.get(idUsuario);
        this.usuarios.remove(idUsuario);
        this.usuarios.put(novoId, usuarioAtualizado);

        return "USUÁRIO ATUALIZADO. NOVO ID: [" + novoId + "]";
    }

    private void verificarTipoValido(String tipo) {
        if(!tipo.equals("CLI") && !tipo.equals("FUN") && !tipo.equals("GER") && !tipo.equals("ADM")) {
            throw new HotelCaliforniaException("TIPO INVÁLIDO");
        }
    }

    private String getIdGerente() {
        for(Usuario u : this.usuarios.values()) {
            if(u.getTipo().equals("GER")) {
                return u.getId();
            }
        }

        return "";
    }

    private boolean verificarGerente() {
        for(Usuario u : this.usuarios.values()) {
            if(u.getTipo().equals("GER")) {
                return true;
            }
        }

        return false;
    }

    public boolean verificarIdValido(String id) {
        return this.usuarios.containsKey(id);
    }

    public boolean verificarTipoUsuario(String id, String tipo) {
        if (!this.usuarios.containsKey(id)) {
                return false;
        }

        if(this.usuarios.get(id).getTipo().equals(tipo)) {
            return true;
        }

        return false;
    }

    public Usuario getUsuario(String id) {
        return (this.usuarios.get(id));

    }

    public String exibirUsuario(String idUsuario) {
        if(!this.usuarios.containsKey(idUsuario)) {
            throw new HotelCaliforniaException("ID DO USUÁRIO NÃO EXISTE");
        }

        return getUsuario(idUsuario).toString();
    }

    public String[] listarUsuarios() {
        ArrayList<String> out = new ArrayList<>();

        for(Usuario u: usuarios.values()) {
            out.add(u.toString());
        }

        return out.toArray(new String[out.size()]);
    }

    public void init() {
        this.usuarios.clear();
        this.usuarios.put("ADM1", new Usuario("ADM1", "Joao Costa", 123456, "ADM"));
    }
}

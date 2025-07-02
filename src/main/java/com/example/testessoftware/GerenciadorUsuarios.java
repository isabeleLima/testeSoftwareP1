package com.example.testessoftware;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorUsuarios {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private final Gson gson = new Gson();
    private final String arquivoUsuarios = "usuarios.json";

    public GerenciadorUsuarios() {
        carregarUsuarios();
    }

    public boolean adicionarUsuario(String login, String senha, String avatar) {
        if (usuarios.containsKey(login)) return false;
        usuarios.put(login, new Usuario(login, senha, avatar));
        salvarUsuarios();
        return true;
    }

    public boolean removerUsuario(String login) {
        if (usuarios.remove(login) != null) {
            salvarUsuarios();
            return true;
        }
        return false;
    }

    public Usuario autenticar(String login, String senha) {
        Usuario u = usuarios.get(login);
        return (u != null && u.verificarSenha(senha)) ? u : null;
    }

    public Map<String, Usuario> getTodosUsuarios() {
        return usuarios;
    }

    public void salvarUsuarios() {
        try (Writer writer = new FileWriter(arquivoUsuarios)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarUsuarios() {
        try (Reader reader = new FileReader(arquivoUsuarios)) {
            Type tipo = new TypeToken<Map<String, Usuario>>(){}.getType();
            Map<String, Usuario> carregados = gson.fromJson(reader, tipo);
            if (carregados != null) {
                usuarios = carregados;
            }
        } catch (FileNotFoundException e) {
            // Arquivo ainda não existe, ok
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.example.testessoftware;

public class Usuario {
    private String login;
    private String senha;
    private String avatar;
    private int simulacoesBemSucedidas = 0;
    private int totalSimulacoes = 0;

    // Construtor padrão necessário para Gson
    public Usuario() {}

    public Usuario(String login, String senha, String avatar) {
        this.login = login;
        this.senha = senha;
        this.avatar = avatar;
    }

    // getters e setters (opcional para Gson, mas recomendável)
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public int getSimulacoesBemSucedidas() { return simulacoesBemSucedidas; }
    public void setSimulacoesBemSucedidas(int simulacoesBemSucedidas) { this.simulacoesBemSucedidas = simulacoesBemSucedidas; }

    public int getTotalSimulacoes() { return totalSimulacoes; }
    public void setTotalSimulacoes(int totalSimulacoes) { this.totalSimulacoes = totalSimulacoes; }

    public boolean verificarSenha(String s) { return this.senha.equals(s); }
    public void incrementarSucesso() { simulacoesBemSucedidas++; }
    public void incrementarTotal() { totalSimulacoes++; }
}

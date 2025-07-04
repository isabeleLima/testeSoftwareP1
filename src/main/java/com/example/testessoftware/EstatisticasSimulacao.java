package com.example.testessoftware;

import java.util.Collection;

/**
 * Um Data Transfer Object (DTO) para armazenar um snapshot consolidado
 * das estatísticas da simulação.
 */
public class EstatisticasSimulacao {
    private final int totalSimulacoesGlobais;
    private final int totalSucessosGlobais;
    private final Collection<Usuario> usuarios;

    public EstatisticasSimulacao(int totalSimulacoesGlobais, int totalSucessosGlobais, Collection<Usuario> usuarios) {
        this.totalSimulacoesGlobais = totalSimulacoesGlobais;
        this.totalSucessosGlobais = totalSucessosGlobais;
        this.usuarios = usuarios;
    }

    // Getters para acessar os dados
    public int getTotalSimulacoesGlobais() { return totalSimulacoesGlobais; }
    public int getTotalSucessosGlobais() { return totalSucessosGlobais; }
    public Collection<Usuario> getUsuarios() { return usuarios; }

    /**
     * Calcula a média de simulações bem-sucedidas por utilizador.
     */
    public double getMediaSucessosPorUsuario() {
        if (usuarios == null || usuarios.isEmpty()) return 0.0;

        double somaSucessos = 0;
        for (Usuario u : usuarios) {
            somaSucessos += u.getSimulacoesBemSucedidas();
        }
        return somaSucessos / usuarios.size();
    }

    /**
     * Calcula a taxa percentual de sucesso global.
     */
    public double getTaxaSucessoGlobal() {
        if (totalSimulacoesGlobais == 0) return 0.0;

        return ((double) totalSucessosGlobais / totalSimulacoesGlobais) * 100;
    }
}

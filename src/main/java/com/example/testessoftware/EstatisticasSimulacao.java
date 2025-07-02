package com.example.testessoftware;

import java.util.Collection;

public class EstatisticasSimulacao {
    private int totalSimulacoes = 0;
    private int totalSucessos = 0;

    public void registrarSimulacao(Usuario usuario, boolean sucesso) {
        totalSimulacoes++;
        usuario.incrementarTotal();
        if (sucesso) {
            totalSucessos++;
            usuario.incrementarSucesso();
        }
    }

    public int getTotalSimulacoes() {
        return totalSimulacoes;
    }

    public int getTotalSucessos() {
        return totalSucessos;
    }

    public double getMediaSucessosPorUsuario(Collection<Usuario> usuarios) {
        if (usuarios.isEmpty()) return 0.0;
        int soma = 0;
        for (Usuario u : usuarios) {
            soma += u.getSimulacoesBemSucedidas();
        }
        return (double) soma / usuarios.size();
    }

    public double getTaxaSucessoGlobal() {
        return totalSimulacoes == 0 ? 0.0 : (double) totalSucessos / totalSimulacoes;
    }
}

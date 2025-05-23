# Simulador de Criaturas Saltitantes

Simulação em Java de criaturas que pulam no horizonte e roubam ouro da criatura mais próxima. Cada criatura começa com 1 milhão de moedas de ouro e se move com base em um número aleatório a cada iteração.

---

## Requisitos

- Java 17 (OpenJDK 17 ou Oracle JDK 17)
- (Opcional) Maven 3.6+ para facilitar compilação e testes

---

## Estrutura do Projeto

```bash
src/
├── main/
│   └── java/
│       └── com/
│           └── simulacao/
│               ├── Creature.java
│               └── Simulator.java
└── test/
    └── java/
        └── com/
            └── simulacao/
                ├── CreatureTest.java
                └── SimulatorTest.java
```

## Como Executar

* 1. Usando Maven (recomendado)
     #### Clonar o projeto:
     ```bash
     git clone https://github.com/isabeleLima/testeSoftwareP1.git
        cd testeSoftwareP1
        ```
     ou use o "Download ZIP".

        #### Compilar e executar:
        quando usado o IntelliJ, o Maven já está configurado para compilar o projeto.
        ou seja basta clicar no botão de executar. Após isso, o projeto será executado e você verá a saída no console.
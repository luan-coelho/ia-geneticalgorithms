package br.unitins.ia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Horario {

    private int diaSemana; // 1-5 (Segunda a Sexta-feira)
    private boolean periodoMatutino; // Matutino (true) ou Vespertino (false)
    private Sala sala;
    private Disciplina disciplina;

    public String getDescricaoDiaSemana() {
        return switch (diaSemana) {
            case 1 -> "Segunda-feira";
            case 2 -> "Terça-feira";
            case 3 -> "Quarta-feira";
            case 4 -> "Quinta-feira";
            case 5 -> "Sexta-feira";
            case 6 -> "Sábado";
            default -> "Dia da semana inválido";
        };
    }
}

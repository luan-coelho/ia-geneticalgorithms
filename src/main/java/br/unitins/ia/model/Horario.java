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
}

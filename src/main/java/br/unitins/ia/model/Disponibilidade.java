package br.unitins.ia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Disponibilidade {

    private int diaSemana;
    private boolean periodoMatutino;
    private boolean periodoVespertino;

    public Disponibilidade(int diaSemana, boolean periodoMatutino) {
        this.diaSemana = diaSemana;
        this.periodoMatutino = periodoMatutino;
    }
}


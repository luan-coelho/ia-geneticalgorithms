package br.unitins.ia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Individuo {

    private List<Horario> horarios = new ArrayList<>();
    private double fitness;

    public Individuo(Individuo outroIndividuo) {
        this.horarios = outroIndividuo.getHorarios().stream()
                .map(horario -> new Horario(horario.getDiaSemana(), horario.isPeriodoMatutino(), horario.getSala(), horario.getDisciplina()))
                .collect(Collectors.toList());
        this.fitness = outroIndividuo.getFitness();
    }

    public boolean professorOcupado(Professor professor, int diaSemana, boolean periodoMatutino) {
        for (Horario horario : horarios) {
            if (horario.getDisciplina().getProfessor().equals(professor) &&
                    horario.getDiaSemana() == diaSemana &&
                    horario.isPeriodoMatutino() == periodoMatutino) {
                return true;
            }
        }
        return false;
    }
}

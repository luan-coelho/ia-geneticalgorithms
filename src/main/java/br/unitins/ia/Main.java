package br.unitins.ia;

import br.unitins.ia.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Professores com a suas disponibilidades
        List<Professor> professores = new ArrayList<>();
        professores.add(new Professor(1, "Prof. Jeferson", List.of(new Disponibilidade(1, true))));
        professores.add(new Professor(2, "Prof. Tayse", List.of(new Disponibilidade(2, false))));
        professores.add(new Professor(3, "Prof. Leandra", List.of(new Disponibilidade(2, true))));
        professores.add(new Professor(4, "Prof. Thamyres", List.of(new Disponibilidade(3, true))));
        professores.add(new Professor(5, "Prof. Silvano", List.of(new Disponibilidade(4, true))));
        professores.add(new Professor(6, "Prof. Mailson", List.of(new Disponibilidade(5, true))));
        professores.add(new Professor(7, "Prof. Bossô", Arrays.asList(new Disponibilidade(6, true), new Disponibilidade(2, false))));

        // Salas
        List<Sala> salas = new ArrayList<>();
        salas.add(new Sala(1, "Sala 1"));
        salas.add(new Sala(2, "Sala 2"));
        salas.add(new Sala(3, "Labin IV"));
        salas.add(new Sala(4, "Labin II"));
        salas.add(new Sala(5, "Labin III"));

        // Disciplinas
        List<Disciplina> disciplinas = new ArrayList<>();
        disciplinas.add(new Disciplina(1, "Interface homem máquina", professores.get(0)));
        disciplinas.add(new Disciplina(2, "Engenharia de qualidade", professores.get(1)));
        disciplinas.add(new Disciplina(3, "Governança de TI", professores.get(2)));
        disciplinas.add(new Disciplina(4, "Inteligência artificial", professores.get(3)));
        disciplinas.add(new Disciplina(5, "Mobile I", professores.get(4)));
        disciplinas.add(new Disciplina(6, "Redes II", professores.get(5)));
        disciplinas.add(new Disciplina(7, "Estatística Computacional", professores.get(6)));

        // Parâmetros do algoritmo genético
        int tamanhoPopulacao = 100;
        double taxaCrossover = 0.8;
        double taxaMutacao = 0.1;
        int maxGeracoes = 100;

        AlgoritmoGenetico algoritmoGenetico = new AlgoritmoGenetico(tamanhoPopulacao, taxaCrossover, taxaMutacao, maxGeracoes);
        Individuo melhorIndividuo = algoritmoGenetico.executar(disciplinas, salas, professores);

        System.out.println("Melhor solução encontrada - Grade de Horários gerada:\n");

        melhorIndividuo.getHorarios().sort(Comparator.comparingInt(Horario::getDiaSemana));

        for (Horario horario : melhorIndividuo.getHorarios()) {
            System.out.println("\t" + horario.getDisciplina().getProfessor().getNome() + " - " + horario.getDisciplina().getNome() + " - " + horario.getSala().getNome() + " - " + (horario.isPeriodoMatutino() ? "08:00-12:00" : "14:00-18:00") + " - [" + horario.getDescricaoDiaSemana() + "]");
        }
    }
}

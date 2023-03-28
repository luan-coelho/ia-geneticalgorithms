package br.unitins.ia;

import br.unitins.ia.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Crie alguns professores com a suas disponibilidades
        List<Professor> professores = new ArrayList<>();
        professores.add(new Professor(1, "Prof. João", Arrays.asList(new Disponibilidade(1, true), new Disponibilidade(2, false))));
        professores.add(new Professor(2, "Prof. Maria", Arrays.asList(new Disponibilidade(1, false), new Disponibilidade(3, true))));
        professores.add(new Professor(3, "Prof. José", Arrays.asList(new Disponibilidade(2, true), new Disponibilidade(4, false))));

        // Crie algumas salas
        List<Sala> salas = new ArrayList<>();
        salas.add(new Sala(1, "Sala 101"));
        salas.add(new Sala(2, "Sala 102"));

        // Crie algumas disciplinas
        List<Disciplina> disciplinas = new ArrayList<>();
        disciplinas.add(new Disciplina(1, "Matemática", professores.get(0)));
        disciplinas.add(new Disciplina(2, "Física", professores.get(1)));
        disciplinas.add(new Disciplina(3, "Química", professores.get(2)));

        // Defina os parâmetros do algoritmo genético
        int tamanhoPopulacao = 50;
        double taxaCrossover = 0.8;
        double taxaMutacao = 0.1;
        int maxGeracoes = 100;

        // Execute o algoritmo genético
        AlgoritmoGenetico algoritmoGenetico = new AlgoritmoGenetico(tamanhoPopulacao, taxaCrossover, taxaMutacao, maxGeracoes);
        Individuo melhorIndividuo = algoritmoGenetico.executar(disciplinas, salas, professores);

        // Exibir os resultados
        System.out.println("Melhor solução encontrada:");
        System.out.println("Fitness: " + melhorIndividuo.getFitness());
        System.out.println("Horários:");

        for (Horario horario : melhorIndividuo.getHorarios()) {
            System.out.println("\t" + horario.getDisciplina().getNome() + " - " + horario.getSala().getNome() + " - " +
                    (horario.isPeriodoMatutino() ? "08:00-12:00" : "14:00-18:00") + " - " +
                    horario.getDiaSemana());
        }
    }
}

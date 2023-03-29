package br.unitins.ia.model;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class AlgoritmoGenetico {

    private final int tamanhoPopulacao;
    private final double taxaCruzamento;
    private final double taxaMutacao;
    private final int maxGeracoes;

    public Individuo executar(List<Disciplina> disciplinas, List<Sala> salas, List<Professor> professores) {
        // Inicializar a população
        List<Individuo> populacao = inicializarPopulacao(disciplinas, salas, professores);

        int geracao = 0;

        while (geracao < maxGeracoes) {
            // Avaliar a população
            for (Individuo individuo : populacao) {
                calcularFitness(individuo);
            }

            // Seleciona os pais
            List<Individuo> pais = selecionarPais(populacao);

            // Cruzamento
            List<Individuo> filhos = cruzamento(pais);

            // Mutação
            mutacao(filhos, disciplinas, salas, professores);

            // Substituir a população
            populacao = filhos;

            // Incrementar a geração
            geracao++;
        }

        // Encontrar o melhor indivíduo
        return populacao.stream().max(Comparator.comparingDouble(Individuo::getFitness)).orElse(null);
    }

    private List<Individuo> inicializarPopulacao(List<Disciplina> disciplinas, List<Sala> salas, List<Professor> professores) {
        List<Individuo> populacao = new ArrayList<>();

        for (int i = 0; i < tamanhoPopulacao; i++) {
            Individuo individuo = new Individuo();
            List<Horario> horarios = gerarHorariosAleatorios(disciplinas, salas, professores);
            individuo.setHorarios(horarios);
            populacao.add(individuo);
        }

        return populacao;
    }

    private List<Horario> gerarHorariosAleatorios(List<Disciplina> disciplinas, List<Sala> salas, List<Professor> professores) {
        List<Horario> horarios = new ArrayList<>();
        Random random = new Random();

        for (Disciplina disciplina : disciplinas) {
            Professor professor = disciplina.getProfessor();
            Disponibilidade disponibilidade = professor.getDisponibilidades().get(random.nextInt(professor.getDisponibilidades().size()));
            int diaSemana = disponibilidade.getDiaSemana();
            boolean periodoMatutino = disponibilidade.isPeriodoMatutino();

            Sala sala = salas.get(random.nextInt(salas.size()));

            Horario horario = new Horario(diaSemana, periodoMatutino, sala, disciplina);
            horarios.add(horario);
        }

        return horarios;
    }

    private double calcularFitness(Individuo individuo) {
        int conflitos = 0;
        List<Horario> horarios = individuo.getHorarios();

        for (int i = 0; i < horarios.size(); i++) {
            for (int j = i + 1; j < horarios.size(); j++) {
                Horario horario1 = horarios.get(i);
                Horario horario2 = horarios.get(j);

                if (horario1.getDiaSemana() == horario2.getDiaSemana() &&
                        horario1.isPeriodoMatutino() == horario2.isPeriodoMatutino()) {

                    // Verifica se dois professores estão na mesma sala no mesmo horário
                    if (horario1.getSala().getId() == horario2.getSala().getId()) {
                        conflitos++;
                    }

                    // Verifica se o mesmo professor está ministrando duas disciplinas no mesmo horário
                    if (horario1.getDisciplina().getProfessor().getId() == horario2.getDisciplina().getProfessor().getId()) {
                        conflitos++;
                    }
                }
            }
        }

        return 1.0 / (1 + conflitos);
    }

    private List<Individuo> selecionarPais(List<Individuo> populacao) {
        // Seleção por torneio
        List<Individuo> pais = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < tamanhoPopulacao; i++) {
            Individuo individuo1 = populacao.get(random.nextInt(tamanhoPopulacao));
            Individuo individuo2 = populacao.get(random.nextInt(tamanhoPopulacao));

            if (individuo1.getFitness() > individuo2.getFitness()) {
                pais.add(new Individuo(individuo1));
            } else {
                pais.add(new Individuo(individuo2));
            }
        }

        return pais;
    }

    private List<Individuo> cruzamento(List<Individuo> pais) {
        List<Individuo> filhos = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < tamanhoPopulacao / 2; i++) {
            Individuo pai1 = pais.get(i);
            Individuo pai2 = pais.get(tamanhoPopulacao - i - 1);

            if (random.nextDouble() < taxaCruzamento) {
                int pontoCorte = random.nextInt(pai1.getHorarios().size());

                Individuo filho1 = new Individuo();
                Individuo filho2 = new Individuo();

                filho1.getHorarios().addAll(pai1.getHorarios().subList(0, pontoCorte));
                filho1.getHorarios().addAll(pai2.getHorarios().subList(pontoCorte, pai2.getHorarios().size()));

                filho2.getHorarios().addAll(pai2.getHorarios().subList(0, pontoCorte));
                filho2.getHorarios().addAll(pai1.getHorarios().subList(pontoCorte, pai1.getHorarios().size()));

                filhos.add(filho1);
                filhos.add(filho2);
            } else {
                filhos.add(pai1);
                filhos.add(pai2);
            }
        }

        return filhos;
    }

    private void mutacao(List<Individuo> filhos, List<Disciplina> disciplinas, List<Sala> salas, List<Professor> professores) {
        Random random = new Random();

        for (Individuo filho : filhos) {
            if (random.nextDouble() < taxaMutacao) {
                int indiceHorario = random.nextInt(filho.getHorarios().size());
                Horario horarioAntigo = filho.getHorarios().get(indiceHorario);
                List<Horario> horariosAleatorios = gerarHorariosAleatorios(disciplinas, salas, professores);
                horariosAleatorios.stream().filter(h -> h.getDisciplina().getId() == horarioAntigo.getDisciplina().getId()).findFirst().ifPresent(horarioNovo -> filho.getHorarios().set(indiceHorario, horarioNovo));
            }
        }
    }
}



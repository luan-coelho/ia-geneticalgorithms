package br.unitins.ia.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgoritmoGenetico {
    private int tamanhoPopulacao;
    private double taxaCrossover;
    private double taxaMutacao;
    private int maxGeracoes;

    public AlgoritmoGenetico(int tamanhoPopulacao, double taxaCrossover, double taxaMutacao, int maxGeracoes) {
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.taxaCrossover = taxaCrossover;
        this.taxaMutacao = taxaMutacao;
        this.maxGeracoes = maxGeracoes;
    }

    public Individuo executar(List<Disciplina> disciplinas, List<Sala> salas, List<Professor> professores) {
        // Inicialize a população
        List<Individuo> populacao = inicializarPopulacao(disciplinas, salas, professores);

        int geracao = 0;

        while (geracao < maxGeracoes) {
            // Avalie a população
            for (Individuo individuo : populacao) {
                calcularFitness(individuo, disciplinas, salas, professores);
            }

            // Selecione os pais
            List<Individuo> pais = selecionarPais(populacao);

            // Faça crossover
            List<Individuo> filhos = crossover(pais);

            // Faça mutação
            mutacao(filhos, disciplinas, salas, professores);

            // Substitua a população
            populacao = filhos;

            // Incrementar a geração
            geracao++;
        }

        // Encontre o melhor indivíduo
        Individuo melhorIndividuo = populacao.stream().max((i1, i2) -> Double.compare(i1.getFitness(), i2.getFitness())).orElse(null);
        return melhorIndividuo;
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

    private void calcularFitness(Individuo individuo, List<Disciplina> disciplinas, List<Sala> salas, List<Professor> professores) {
        double fitness = 1.0;
        double penalidade = 0.0;

        List<Horario> horarios = individuo.getHorarios();

        // Verificar critérios 1 e 2
        for (int i = 0; i < horarios.size(); i++) {
            Horario horario1 = horarios.get(i);
            for (int j = i + 1; j < horarios.size(); j++) {
                Horario horario2 = horarios.get(j);

                if (horario1.getDiaSemana() == horario2.getDiaSemana() && horario1.isPeriodoMatutino() == horario2.isPeriodoMatutino()) {
                    // Critério 1: Dois professores não podem ocupar a mesma sala no mesmo horário
                    if (horario1.getSala().getId() == horario2.getSala().getId()) {
                        penalidade += 1;
                    }

                    // Critério 2: Um professor não pode ministrar duas disciplinas no mesmo horário
                    if (horario1.getDisciplina().getProfessor().getId() == horario2.getDisciplina().getProfessor().getId()) {
                        penalidade += 1;
                    }
                }
            }
        }

        // Verificar critério 3
        for (Horario horario : horarios) {
            Disciplina disciplina = horario.getDisciplina();
            Professor professor = disciplina.getProfessor();
            boolean disponivel = false;

            for (Disponibilidade disponibilidade : professor.getDisponibilidades()) {
                if (disponibilidade.getDiaSemana() == horario.getDiaSemana() && disponibilidade.isPeriodoMatutino() == horario.isPeriodoMatutino()) {
                    disponivel = true;
                    break;
                }
            }

            if (!disponivel) {
                penalidade += 1;
            }
        }

        fitness -= penalidade / (disciplinas.size() * 2); // Dividir pela quantidade total de violações possíveis
        individuo.setFitness(fitness);
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

    private List<Individuo> crossover(List<Individuo> pais) {
        List<Individuo> filhos = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < tamanhoPopulacao / 2; i++) {
            Individuo pai1 = pais.get(i);
            Individuo pai2 = pais.get(tamanhoPopulacao - i - 1);

            if (random.nextDouble() < taxaCrossover) {
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
                Horario horarioNovo = horariosAleatorios.stream().filter(h -> h.getDisciplina().getId() == horarioAntigo.getDisciplina().getId()).findFirst().orElse(null);

                if (horarioNovo != null) {
                    filho.getHorarios().set(indiceHorario, horarioNovo);
                }
            }
        }
    }
}



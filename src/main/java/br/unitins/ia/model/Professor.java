package br.unitins.ia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Professor {

    private int id;
    private String nome;
    private List<Disponibilidade> disponibilidades;
}

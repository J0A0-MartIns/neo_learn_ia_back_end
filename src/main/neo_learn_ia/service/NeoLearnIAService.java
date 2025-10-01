package br.ueg.progwebi.collegeapi.service;

import br.ueg.progwebi.collegeapi.model.Veiculo;

import java.util.List;

public interface VeiculoService {
    List<Veiculo> listAll();
    Veiculo getById(Long id);
    Veiculo create(Veiculo veiculo);
    Veiculo update(Long id, Veiculo veiculo);
    Veiculo delete(Long id);
}
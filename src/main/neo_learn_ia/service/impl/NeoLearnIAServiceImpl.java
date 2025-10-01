package br.ueg.progwebi.collegeapi.service.impl;

import br.ueg.progwebi.collegeapi.service.exceptions.BusinessException;
import br.ueg.progwebi.collegeapi.model.Veiculo;
import br.ueg.progwebi.collegeapi.repository.VeiculoRepository;
import br.ueg.progwebi.collegeapi.service.VeiculoService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class VeiculoServiceImpl implements VeiculoService {

    @Autowired
    private VeiculoRepository repository;

    @Override
    public List<Veiculo> listAll() {
        return repository.findAll();
    }

    @Override
    public Veiculo getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Veículo com id " + id + " não encontrado", 404));
    }

    @Override
    @Transactional
    public Veiculo create(Veiculo veiculo) {
        validateCreate(veiculo);
        return repository.save(veiculo);
    }

    private void validateCreate(Veiculo veiculo) {
        if (Strings.isEmpty(veiculo.getModelo()) || Strings.isEmpty(veiculo.getChassi()) ||
                Strings.isEmpty(veiculo.getPlaca()) || veiculo.getDataFabricacao() == null) {
            throw new BusinessException("Dados obrigatórios não preenchidos (modelo, chassi, placa, data fabricação).");
        }

        if (repository.existsByChassi(veiculo.getChassi())) {
            throw new BusinessException("Já existe um veículo com esse chassi: " + veiculo.getChassi());
        }

        if (repository.existsByPlaca(veiculo.getPlaca())) {
            throw new BusinessException("Já existe um veículo com essa placa: " + veiculo.getPlaca());
        }

        if (veiculo.getChassi().length() != 17){
            throw new BusinessException("Tamanho de chassi maior ou menor que 17 caracteres.");
        }
    }

    @Override
    @Transactional
    public Veiculo update(Long id, Veiculo veiculoAtualizado) {
        Veiculo dbVeiculo = this.getById(id);
        validateUpdate(veiculoAtualizado);

        dbVeiculo.setModelo(veiculoAtualizado.getModelo());
        dbVeiculo.setChassi(veiculoAtualizado.getChassi());
        dbVeiculo.setDataFabricacao(veiculoAtualizado.getDataFabricacao());
        dbVeiculo.setPlaca(veiculoAtualizado.getPlaca());
        dbVeiculo.setVendido(veiculoAtualizado.getVendido());
        dbVeiculo.setQuilometragem(veiculoAtualizado.getQuilometragem());

        return repository.save(dbVeiculo);
    }

    private void validateUpdate(Veiculo veiculo) {
        if (Objects.isNull(veiculo.getId()) || veiculo.getId() == 0) {
            throw new BusinessException("ID inválido para atualização.");
        }

        if (Strings.isEmpty(veiculo.getModelo())) {
            throw new BusinessException("Modelo não pode ser vazio.");
        }
    }

    @Override
    @Transactional
    public Veiculo delete(Long id) {
        Veiculo veiculo = this.getById(id);
        try {
            repository.delete(veiculo);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Veículo id: " + id + " não pode ser removido por questões de integridade.");
        }
        return veiculo;
    }
}
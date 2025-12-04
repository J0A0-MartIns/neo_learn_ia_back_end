package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ScheduleGetResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyScheduleResponseDTO;
import neo_learn_ia_api.Neo.Learn.Ia.API.mapper.StudyScheduleMapper;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudySchedule;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.StudyScheduleRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.StudyScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyScheduleServiceImpl  implements StudyScheduleService {
    private final StudyScheduleRepository studyScheduleRepository;
    private final StudyScheduleMapper studyScheduleMapper;

    public StudyScheduleResponseDTO getScheduleById(Long id) {
        StudySchedule schedule = studyScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cronograma não encontrado: " + id));

        return studyScheduleMapper.toDto(schedule);
    }

    public List<ScheduleGetResponse> getAllSchedule(){
        List<StudySchedule> schedule = studyScheduleRepository.findAllWithProject();

        return studyScheduleMapper.toListDto(schedule);
    }

    @Transactional
    public void deleteScheduleById(Long id){
        studyScheduleRepository.deleteById(id);
    }
}

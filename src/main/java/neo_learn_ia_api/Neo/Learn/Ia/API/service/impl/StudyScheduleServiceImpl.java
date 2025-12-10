package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.QuizPdfRequestDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ScheduleGetResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyScheduleResponseDTO;
import neo_learn_ia_api.Neo.Learn.Ia.API.mapper.StudyScheduleMapper;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudySchedule;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.StudyScheduleRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.StudyScheduleService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class StudyScheduleServiceImpl  implements StudyScheduleService {
    private final StudyScheduleRepository studyScheduleRepository;
    private final StudyScheduleMapper studyScheduleMapper;
    private final SpringTemplateEngine templateEngine;

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

    public byte[] generatePdf(QuizPdfRequestDto request) {
        try {
            Context context = new Context();
            context.setVariable("fileId", request.fileId());
            context.setVariable("projectId", request.projectId());
            context.setVariable("questions", request.questions());

            String html = templateEngine.process("quiz-template", context);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.useFastMode();
            builder.useDefaultPageSize(8.27f, 11.69f, PdfRendererBuilder.PageSizeUnits.INCHES);
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF via HTML", e);
        }
    }
}

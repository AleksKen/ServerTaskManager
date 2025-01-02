package hexlet.code.app.service;

import hexlet.code.app.dto.label.LabelCreateDTO;
import hexlet.code.app.dto.label.LabelDTO;
import hexlet.code.app.dto.label.LabelUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    public List<LabelDTO> index() {
        return labelRepository.findAll().stream().map(labelMapper::map).toList();
    }

    public LabelDTO show(Long id) {
        return labelMapper.map(labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found!")));
    }

    public LabelDTO create(LabelCreateDTO dto) {
        var label = labelMapper.map(dto);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public LabelDTO update(Long id, LabelUpdateDTO dto) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found!"));
        labelMapper.update(dto, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void destroy(Long id) {
        labelRepository.deleteById(id);
    }
}

package app.code.service;

import app.code.dto.label.LabelCreateDTO;
import app.code.dto.label.LabelDTO;
import app.code.dto.label.LabelUpdateDTO;
import app.code.exception.ResourceNotFoundException;
import app.code.mapper.LabelMapper;
import app.code.repository.LabelRepository;
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

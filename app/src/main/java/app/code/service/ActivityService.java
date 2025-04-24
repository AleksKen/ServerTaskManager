package app.code.service;


import app.code.dto.activity.ActivityCreateDTO;
import app.code.dto.activity.ActivityDTO;
import app.code.dto.activity.ActivityUpdateDTO;
import app.code.exception.ResourceNotFoundException;
import app.code.mapper.ActivityMapper;
import app.code.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMapper activityMapper;

    public List<ActivityDTO> index() {
        return activityRepository.findAll().stream().map(activityMapper::map).toList();
    }

    public ActivityDTO show(Long id) {
        return activityMapper.map(activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity with id " + id + " not found!")));
    }

    public ActivityDTO create(ActivityCreateDTO dto) {
        var label = activityMapper.map(dto);
        activityRepository.save(label);
        return activityMapper.map(label);
    }

    public ActivityDTO update(Long id, ActivityUpdateDTO dto) {
        var label = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity with id " + id + " not found!"));
        activityMapper.update(dto, label);
        activityRepository.save(label);
        return activityMapper.map(label);
    }

    public void destroy(Long id) {
        activityRepository.deleteById(id);
    }
}

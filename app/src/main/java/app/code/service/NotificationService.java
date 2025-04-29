package app.code.service;


import app.code.dto.notification.NotificationCreateDTO;
import app.code.dto.notification.NotificationDTO;
import app.code.dto.notification.NotificationUpdateDTO;
import app.code.exception.ResourceNotFoundException;
import app.code.mapper.NotificationMapper;
import app.code.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    public List<NotificationDTO> index() {
        return notificationRepository.findAll().stream().map(notificationMapper::map).toList();
    }

    public NotificationDTO show(Long id) {
        return notificationMapper.map(notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification with id " + id + " not found!")));
    }

    public NotificationDTO create(NotificationCreateDTO dto) {
        var notification = notificationMapper.map(dto);
        notificationRepository.save(notification);
        return notificationMapper.map(notification);
    }

    public NotificationDTO update(Long id, NotificationUpdateDTO dto) {
        var notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification with id " + id + " not found!"));
        notificationMapper.update(dto, notification);
        notificationRepository.save(notification);
        return notificationMapper.map(notification);
    }

    public void destroy(Long id) {
        notificationRepository.deleteById(id);
    }
}

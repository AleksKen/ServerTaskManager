package app.code.service;

import app.code.dto.user.UserCreateDTO;
import app.code.dto.user.UserDTO;
import app.code.dto.user.UserUpdateDTO;
import app.code.exception.ResourceNotFoundException;
import app.code.mapper.UserMapper;
import app.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDTO findByEmail(String email) {
        return userMapper.map(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found!")));
    }

    public List<UserDTO> index() {
        return userRepository.findAll().stream().map(userMapper::map).toList();
    }

    public UserDTO show(Long id) {
        return userMapper.map(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!")));
    }

    public UserDTO create(UserCreateDTO userCreateDTO) {
        var user = userMapper.map(userCreateDTO);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public UserDTO update(Long id, UserUpdateDTO userUpdateDTO) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        userMapper.update(userUpdateDTO, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void destroy(Long id) {
        userRepository.deleteById(id);
    }
}

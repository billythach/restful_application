package com.bthach.service;

import com.bthach.persistence.entity.User;
import com.bthach.persistence.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User save(InputStream jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(jsonFile, User.class);
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.delete(userRepository.findOne(id));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

}

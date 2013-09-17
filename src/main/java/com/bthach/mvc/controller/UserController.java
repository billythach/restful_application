package com.bthach.mvc.controller;

import com.bthach.persistence.entity.User;
import com.bthach.persistence.entity.UserType;
import com.bthach.service.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private static final String IMPORT = "IMPORT";

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User>  listUsersJson(ModelMap model) throws JSONException {
        List<User> all = userService.getAll();
        return all;
    }

    @RequestMapping(value = "/user/add/m1", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody User user) {
        userService.save(user);
    }

    @RequestMapping(value = "/user/add/m2", method = RequestMethod.POST)
    public ResponseEntity<User> createUserByMultiParams(@RequestParam("lastName") String lastName, @RequestParam("firstName") String firstName, @RequestParam("email") String email) {
        User user = new User(firstName, lastName, email);
        user = userService.save(user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/add/m3", method = RequestMethod.POST)
    public ResponseEntity<User> createUserByMultiParamsWithEnum(@RequestParam("lastName") String lastName, @RequestParam("firstName") String firstName, @RequestParam("email") String email, @RequestParam("userType") UserType type) {
        User user = new User(firstName, lastName, email, type);
        user = userService.save(user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/add/m4", method = RequestMethod.POST)
    public ResponseEntity<User> createUserByRequestBody(User user) {
        user = userService.save(user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);

    }

    @RequestMapping(value = "/user/add/m5", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUserByMultiPartFile(@RequestParam("status") String status, @RequestParam("userFile") MultipartFile userFile) {
        if (IMPORT.equals(status)) {
            try {
                InputStream input = userFile.getInputStream();
                User user = userService.save(input);
                return new ResponseEntity<User>(user, HttpStatus.CREATED);
            } catch (Exception e) {
                throw new IllegalArgumentException("File '" + userFile.getOriginalFilename() + "' is not valid (only Json format is allowed).");
            }
        } else {
            throw new IllegalArgumentException("Status " + status + " is not valid. Only 'IMPORT' value is allowed.");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage();
    }

}

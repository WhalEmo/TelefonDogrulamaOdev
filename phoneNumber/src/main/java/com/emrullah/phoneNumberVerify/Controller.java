package com.emrullah.phoneNumberVerify;

import com.emrullah.phoneNumberVerify.Request.PhoneVerifyReqDTO;
import com.emrullah.phoneNumberVerify.Request.UserRegisterReqDTO;
import com.emrullah.phoneNumberVerify.Response.PhoneVerifyResDTO;
import com.emrullah.phoneNumberVerify.Response.UserRegisterResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class Controller {

    private final AppService service;

    @Autowired
    public Controller(AppService service){
        this.service = service;
    }

    @PostMapping("/phone/validate")
    public PhoneVerifyResDTO phoneValidate(@RequestBody PhoneVerifyReqDTO phoneDTO){
        return service.verifyPhone(phoneDTO.getNumber());
    }

    @PostMapping("/registration")
    public ResponseEntity<UserRegisterResDTO> registerUser(@RequestBody UserRegisterReqDTO userDTO){
        return service.registerUser(userDTO);
    }

    @GetMapping("/phone/count")
    public ResponseEntity<Map<String, Long>> countPhone(){
        return new ResponseEntity<>(service.phoneCount(), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size){
        return new ResponseEntity<>(service.getUsers(page, size), HttpStatus.OK);
    }



}

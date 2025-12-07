package com.emrullah.phoneNumberVerify;

import com.emrullah.phoneNumberVerify.Request.UserRegisterReqDTO;
import com.emrullah.phoneNumberVerify.Response.PhoneVerifyResDTO;
import com.emrullah.phoneNumberVerify.Response.UserRegisterResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppService {
    private final UserRepo userRepo;

    @Autowired
    public AppService(UserRepo userRepo){
        this.userRepo = userRepo;
    }


    @Transactional
    public ResponseEntity<UserRegisterResDTO> registerUser(UserRegisterReqDTO userDTO){

        PhoneVerifyResDTO phoneDTO = verifyPhone(userDTO.getPhone());
        if (!phoneDTO.getValid()){
            return new ResponseEntity<>(
                    new UserRegisterResDTO(
                            "denied",
                            "Geçersiz telefon numarası. Lütfen yeni bir numara deneyin.",
                            false
                    ),HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        userRepo.save(user);

        return new ResponseEntity<>(
                new UserRegisterResDTO(
                        "accepted",
                        "Telefon numarası geçerli, kayıt başarıyla oluşturuldu.",
                        user,
                        true
                ),HttpStatus.CREATED
        );
    }

    @Transactional
    public Map<String, Long> phoneCount(){
        Long count = userRepo.countByPhoneIsNotNull();
        Map<String, Long> map = new HashMap<>();
        map.put("telefon sayısı",count);
        return map;
    }


    @Transactional
    public List<User> getUsers(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return userRepo.findAll(pageable).getContent();
    }


    public PhoneVerifyResDTO verifyPhone(String number){
        PhoneVerifyResDTO dto = new PhoneVerifyResDTO();
        dto.setValid(false);
        dto.setNumber(number);

        if (number.length() != 6){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number size is not six");
        }
        int zeroCounter = 0;
        for(int i=0; i<number.length(); i++){
            char digit = number.charAt(i);
            if(!Character.isDigit(digit)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number have a letter!");
            }
            if(digit == '0'){
                zeroCounter++;
            }
        }
        dto.getRules().put("hasNonZeroDigit", !(zeroCounter==6));

        dto.getRules().put("sumFirstEqualsLast", sumFirstEqualsLast(number));

        dto.getRules().put("sumOddEqualsEven",sumOddEqualsEven(number));

        dto.setValid(
                dto.getRules().get("hasNonZeroDigit")
                && dto.getRules().get("sumFirstEqualsLast")
                && dto.getRules().get("sumOddEqualsEven")
        );
        return dto;
    }

    private int Dgt(String number, int index){

        return Character.getNumericValue(number.charAt(index));
    }

    private boolean sumFirstEqualsLast(String number){
        int firstNumbers = 0;
        int lastNumbers = 0;
        for(int i=0; i<number.length()/2; i++){
            firstNumbers += Dgt(number,i);
            lastNumbers += Dgt(number,i+3);
        }
        return firstNumbers == lastNumbers;
    }

    private boolean sumOddEqualsEven(String number){
        int oddNumbers = 0;
        int evenNumbers = 0;
        for(int i=0; i<number.length(); i+=2){
            oddNumbers += Dgt(number,i);
            evenNumbers += Dgt(number,i+1);
        }
        return oddNumbers == evenNumbers;
    }

}

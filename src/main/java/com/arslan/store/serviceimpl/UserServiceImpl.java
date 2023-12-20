package com.arslan.store.serviceimpl;

import com.arslan.store.constants.StoreConstants;
import com.arslan.store.service.UserService;
import com.arslan.store.utils.StoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        if(validateSignUpMap(requestMap)){

        }else {
            return StoreUtils.getResponseEntity(StoreConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }

        return false;
    }
}

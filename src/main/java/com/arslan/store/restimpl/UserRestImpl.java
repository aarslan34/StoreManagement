package com.arslan.store.restimpl;

import com.arslan.store.constants.StoreConstants;
import com.arslan.store.rest.UserRest;
import com.arslan.store.service.UserService;
import com.arslan.store.utils.StoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;


    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }

        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {


        try{
            return userService.login(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> logout(Map<String, String> requestMap) {
        try{
            return userService.logout(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {

        try{
            return userService.changePassword(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }

        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

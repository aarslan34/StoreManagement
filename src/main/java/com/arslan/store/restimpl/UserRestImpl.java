package com.arslan.store.restimpl;

import com.arslan.store.constants.StoreConstants;
import com.arslan.store.rest.UserRest;
import com.arslan.store.service.UserService;
import com.arslan.store.utils.StoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
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
}

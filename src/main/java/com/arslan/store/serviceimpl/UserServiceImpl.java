package com.arslan.store.serviceimpl;

import com.arslan.store.constants.StoreConstants;
import com.arslan.store.dao.UserDao;
import com.arslan.store.model.User;
import com.arslan.store.service.UserService;
import com.arslan.store.utils.StoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    requestMap.put("status", "out");
                    userDao.save(getUserFromMap(requestMap));
                    return StoreUtils.getResponseEntity("Successfully registered.", HttpStatus.OK);
                } else {
                    return StoreUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return StoreUtils.getResponseEntity(StoreConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login {}", requestMap);
        try {
            User user = userDao.findByEmailId(requestMap.get("email"));
            if (Objects.isNull(user)) {
                return StoreUtils.getResponseEntity("Email does not exist. Please register!", HttpStatus.OK);
            } else if( user.getEmail().equals(requestMap.get("email")) && user.getPassword().equals(requestMap.get("password"))){
                user.setStatus("in");
                userDao.save(user);

                return StoreUtils.getResponseEntity("Logged in successfully.", HttpStatus.OK);
            }else {
                return StoreUtils.getResponseEntity("Wrong username/password", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);



    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        log.info("Inside changePassword {}", requestMap);
        try {
            User user = userDao.findByEmailId(requestMap.get("email"));

            System.out.println(user.getPassword());
            System.out.println(requestMap);
            if (!Objects.isNull(user)) {
                if(user.getPassword().equals(requestMap.get("oldPassword")) && user.getStatus().equals("in")){
                    user.setPassword(requestMap.get("newPassword"));
                    userDao.save(user);
                    return StoreUtils.getResponseEntity("Password changed successfully.", HttpStatus.OK);
                }else if(!user.getStatus().equals("in")){
                    return StoreUtils.getResponseEntity("Session expired, please login!", HttpStatus.OK);
                }else if(!user.getPassword().equals(requestMap.get("oldPassword"))){
                    return StoreUtils.getResponseEntity("Incorrect old password!", HttpStatus.OK);
                }
                return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            e.printStackTrace();
        }
        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> logout(Map<String, String> requestMap) {
        log.info("Inside logout {}", requestMap);
        try {
            User user = userDao.findByEmailId(requestMap.get("email"));
            if (!Objects.isNull(user)) {
                user.setStatus("out");
                userDao.save(user);
                return StoreUtils.getResponseEntity("Logged out successfully!", HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }

        return false;
    }

    private User getUserFromMap(Map<String ,String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus(requestMap.get("status"));

        return user;
    }


}

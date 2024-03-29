package com.arslan.store.restimpl;

import com.arslan.store.constants.StoreConstants;
import com.arslan.store.model.Bill;
import com.arslan.store.rest.BillRest;
import com.arslan.store.service.BillService;
import com.arslan.store.utils.StoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BillRestImpl implements BillRest {

    @Autowired
    BillService billService;
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {

        try {
            return billService.generateReport(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try {
            return billService.getBills();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

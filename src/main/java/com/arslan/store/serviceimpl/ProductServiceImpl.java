package com.arslan.store.serviceimpl;

import com.arslan.store.constants.StoreConstants;
import com.arslan.store.dao.ProductDao;
import com.arslan.store.model.Category;
import com.arslan.store.model.Product;
import com.arslan.store.service.ProductService;
import com.arslan.store.utils.StoreUtils;
import com.arslan.store.wrapper.ProductWrapper;
import jdk.jfr.Experimental;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        log.info("inside serviceImpl");
        try {
            if(validateProductMap(requestMap, false)){
                productDao.save(getProductFromMap(requestMap, false));
                return StoreUtils.getResponseEntity("Product added successfully", HttpStatus.OK);
            }
            return StoreUtils.getResponseEntity(StoreConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try{
            return new ResponseEntity<>(productDao.getAllProducts(), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (validateProductMap(requestMap, true)){
               Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));

               if(!optional.isEmpty()) {
                   Product product = getProductFromMap(requestMap, true);
                   product.setStatus(optional.get().getStatus());
                   productDao.save(product);
                   return StoreUtils.getResponseEntity("Product updated successfully", HttpStatus.OK);
               }else {
                   return StoreUtils.getResponseEntity("Product id does not exist.", HttpStatus.OK);
               }
            }else {
                return StoreUtils.getResponseEntity(StoreConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();
        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));
        }else{
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));

        return product;

    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {

        if(requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            }else if(!validateId){
                return true;
            }
        }
        return false;
    }
}

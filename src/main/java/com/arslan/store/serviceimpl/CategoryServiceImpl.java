package com.arslan.store.serviceimpl;

import com.arslan.store.constants.StoreConstants;
import com.arslan.store.dao.CategoryDao;
import com.arslan.store.model.Category;
import com.arslan.store.service.CategoryService;
import com.arslan.store.utils.StoreUtils;;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try{
            List<Category> category = categoryDao.getAllCategory();
            boolean containsCategory = false;
            for (Category category1: category) {
                if (category1.getName().equalsIgnoreCase(requestMap.get("name"))){
                    containsCategory = true;
                    break;
                }
            }
            System.out.println("BOOLEAN: " + containsCategory);
            if (containsCategory){
                return StoreUtils.getResponseEntity("Category already exists", HttpStatus.CONFLICT);
            }
            if(validateCategoryMap(requestMap, false) ){
                categoryDao.save(getCategoryFromMap(requestMap, false));
                return StoreUtils.getResponseEntity("Category added successfully", HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {

        try{
            if(!(filterValue==null || filterValue=="") && filterValue.equalsIgnoreCase("true")){
                log.info("inside if of getAllCategory");
                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
            }

            return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (validateCategoryMap(requestMap, true)){
                Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!optional.isEmpty()){
                    categoryDao.save(getCategoryFromMap(requestMap, true));
                    return StoreUtils.getResponseEntity("Category updated successfully", HttpStatus.OK);
                }else {
                   return StoreUtils.getResponseEntity("Category id does not exist", HttpStatus.OK);

                }
            }

            return StoreUtils.getResponseEntity(StoreConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }

        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if (requestMap.containsKey("id")){
                return true;
            } else if (!validateId) {
                return true;
            }
        }

        return false;

    }

    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd){
        Category category = new Category();
        if(isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }

        category.setName(requestMap.get("name"));
        return category;
    }
}

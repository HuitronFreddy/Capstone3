package org.yearup.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

import static com.sun.org.apache.xerces.internal.impl.xpath.regex.Token.categories;

//Steeven
// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController
{

    private CategoryDao categoryDao;
    private ProductDao productDao;

    //Nathan
    // create an Autowired controller to inject the categoryDao and ProductDao
    // add the appropriate annotation for a get action
    @RestController
    public class YourController {

        private final CategoryDao categoryDao;
        private final ProductDao productDao;

        @Autowired
        public YourController(CategoryDao categoryDao, ProductDao productDao) {
            this.categoryDao = categoryDao;
            this.productDao = productDao;
        }

        @GetMapping("/categories")
    public List<Category> getAll()
    {
        // find and return all categories
        List<Category> categories = categoryDao.getAllCategories();
        return categories;
    }
        @GetMapping("/products")
        public List<Product> getAllProducts() {
            // Find and return all products using productDao
            List<Product> products = productDao.getAllProducts();
            return products;
        }

    //David
    // add the appropriate annotation for a get action
    public Category getById(@PathVariable int id)
    {
        // get the category by id
        return null;
    }

    //Freddy
    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        try{
            var category = categoryDao.getById(categoryId);

            if (category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
            return productDao.search(categoryId, null, null, null);
        }
        catch(Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Please try again.");
        }
    }

    //Steeven
    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category addCategory(@RequestBody Category category) {
        try {
            return categoryDao.create(category);
        }
        catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"add category error");
        }
    }

    //Nathan
    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PutMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(@PathVariable int Id, @RequestBody Category category)
    {
        // update the category by id
    }

    //David
    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    public void deleteCategory(@PathVariable int id)
    {
        // delete the category by id
    }
}

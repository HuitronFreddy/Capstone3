package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.ShoppingCartDto;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/api/shopping-cart")
@CrossOrigin
public class ShoppingCartController {

    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCart getCart(Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();

            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            return cart;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping("/products/{productId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCart addProductToCart(@PathVariable int productId, Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();
            ShoppingCart updatedCart = shoppingCartDao.getByUserId(userId);

            if(updatedCart.contains(productId)){
                ShoppingCartItem item = updatedCart.get(productId);
                int currentQuantity = item.getQuantity();
                int newQuantity = currentQuantity + 1;

                shoppingCartDao.updateItem(newQuantity, userId, productId);
            }
            else {
                shoppingCartDao.addItem(productId, userId);
            }
            return shoppingCartDao.getByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("/products/{productId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCart updateCartItem(@PathVariable int productId,
                                       @RequestParam int quantity,
                                       Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();

            shoppingCartDao.updateItem(userId, productId, quantity);

            return shoppingCartDao.getByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping(path = "/products/{productId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCart clearCart(Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();

            shoppingCartDao.clearCart(userId);

            return shoppingCartDao.getByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
    @DeleteMapping()
    public ShoppingCart removeItemFromCart(@PathVariable int productId, Principal principal){
        try{
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();

            shoppingCartDao.removeItem(productId, userId);

            return shoppingCartDao.getByUserId(userId);
        }
        catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... Our bad.");
        }
    }
}
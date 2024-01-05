package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    ShoppingCart addItem(int productId, int userId);
    void updateItem(int productId, int userId, int quantity);
    void removeItem(int productId, int userId);
    void clearCart(int userId);
}

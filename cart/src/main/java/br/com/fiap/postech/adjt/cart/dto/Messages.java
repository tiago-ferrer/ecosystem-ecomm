package br.com.fiap.postech.adjt.cart.dto;

public class Messages {

    public static final String ADD_ITEM_SUCCESS = "Item added to cart successfully";
    public static final String DEC_ITEM_SUCCESS = "Item removed from cart successfully";
    public static final String INC_ITEM_SUCCESS = "Item removed from cart successfully";
    public static final String ITEM_NOT_FOUND = "Invalid itemId";
    public static final String CART_EMPTY = "Empty cart";
    public static final String CART_CLEAR_ITEMS = "Items removed from cart successfully";

    protected Messages() {
        throw new IllegalStateException("Class not instance");
    }

}

package exception;

import java.util.UUID;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(UUID consumerId) {
        super("Cart not found for consumer ID: " + consumerId);
    }
}
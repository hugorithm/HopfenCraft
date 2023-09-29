export interface ShoppingCartResponse {
    cartItems: CartItem[];
}

export interface CartItem {
    cartItemId:    number;
    product:       Product;
    total:         string;
    quantity:      number;
    addedDateTime: string;
}

export interface Product {
    productId:        number;
    brand:            string;
    name:             string;
    description:      string;
    quantity:         number;
    price:            string;
    currency:         string;
    registerDateTime: string;
}


export interface UserResponse {
    userId:    number;
    username:  string;
    email:     string;
    firstName: string;
    lastName:  string;
    roles:     string[];
    cartItems: CartItem[];
    orders:    Order[];
}

export interface CartItem {
    cartItemId:    number;
    product:       Product;
    quantity:      number;
    total:         string;
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

export interface Order {
    orderId:   number;
    total:     string;
    currency:  string;
    cartItems: CartItem[];
    orderDate: string;
}

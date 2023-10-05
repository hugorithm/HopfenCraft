export interface OAuth2UserResponse {
    userId:     number;
    name:       string;
    username:   string;
    email:      string;
    firstName:  string;
    lastName:   string;
    roles:      string[];
    cartItems:  CartItem[];
    orders:     Order[];
    attributes: { [key: string]: string };
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
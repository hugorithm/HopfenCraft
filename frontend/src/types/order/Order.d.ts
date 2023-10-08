export interface Order {
    orderId:     number;
    total:       string;
    currency:    string;
    orderItems:   OrderItem[];
    orderStatus: string;
    orderDate:   string;
}

export interface OrderItem {
    orderItemId:   number;
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

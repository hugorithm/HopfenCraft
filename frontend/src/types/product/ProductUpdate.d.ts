export interface ProductUpdate {
    productId:    number;
    brand:       string | undefined;
    name:        string | undefined;
    description: string | undefined;
    quantity:    string | undefined;
    price:       string | undefined;
}
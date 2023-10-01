export interface OAuth2UserResponse {
    userId:     number;
    name:       string;
    username:   string;
    email:      string;
    firstName:  string;
    lastName:   string;
    cartItems:  any[];
    orders:     any[];
    attributes: { [key: string]: string };
}



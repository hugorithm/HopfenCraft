import { ACCESS_TOKEN } from "../config/constants";

export const isAuthenticated = () : boolean => {
    const jwt = localStorage.getItem(ACCESS_TOKEN); 
    return !!jwt; 
}
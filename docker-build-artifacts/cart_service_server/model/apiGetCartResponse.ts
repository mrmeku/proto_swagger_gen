import { ApiCart } from './apiCart';


export interface ApiGetCartResponse { 
    status?: number;
    cart?: ApiCart;
}

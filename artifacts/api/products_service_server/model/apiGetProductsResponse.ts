import { ApiProduct } from './apiProduct';


export interface ApiGetProductsResponse { 
    status?: number;
    products?: Array<ApiProduct>;
}

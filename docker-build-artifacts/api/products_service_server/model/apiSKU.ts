import { ApiImage } from './apiImage';
import { ApiSkuPrice } from './apiSkuPrice';


export interface ApiSKU { 
    skuCode?: string;
    skuPrices?: Array<ApiSkuPrice>;
    images?: Array<ApiImage>;
    badges?: Array<string>;
}

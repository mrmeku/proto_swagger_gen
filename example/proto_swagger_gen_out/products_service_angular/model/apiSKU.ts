/**
 * example/api/products_service.proto
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: version not set
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
import { ApiImage } from './apiImage';
import { ApiSkuPrice } from './apiSkuPrice';


export interface ApiSKU { 
    skuCode?: string;
    skuPrices?: Array<ApiSkuPrice>;
    images?: Array<ApiImage>;
    badges?: Array<string>;
}

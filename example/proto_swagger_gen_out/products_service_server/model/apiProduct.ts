import { ApiColorOption } from './apiColorOption';
import { ApiSKU } from './apiSKU';


export interface ApiProduct { 
    familyCode?: string;
    familyName?: string;
    manufacturer?: string;
    ratingsCount?: number;
    deviceRank?: number;
    colorOptions?: Array<ApiColorOption>;
    averageStarRating?: string;
    operatingSystem?: string;
    defaultSku?: string;
    skus?: Array<ApiSKU>;
}

import { HapiServerPlugin } from '@tmo/hapi/utils';
import * as Boom from 'boom';

import { ApiGetProductsRequest } from '../model/apiGetProductsRequest';
import { ApiGetProductsResponse } from '../model/apiGetProductsResponse';


export interface ProductsServicePluginConfig {
  getProducts: (
    config: {
      server: any;
      options: any;
      request: ApiGetProductsRequest;
    }
  ) => Promise<ApiGetProductsResponse>;
  warmCache?: (options: any) => void;
}

export class ProductsServicePlugin implements HapiServerPlugin {
  readonly name = 'ProductsServicePlugin';
  readonly version = '1.0.0';
  readonly once = true;

  constructor(private readonly config: ProductsServicePluginConfig) {}

  readonly register = async (server, options) => {
    if (this.config.warmCache) {
      this.config.warmCache(options);
    }

    server.route({
      path: '/some_url/endpoint',
      method: 'POST',
      handler: async request => {
        try {
          return await this.config.getProducts({ server, options, request });
        } catch (error) {
          return Boom.serverUnavailable(error);
        }
      }
    });
  };
}

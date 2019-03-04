import * as Boom from 'boom';

import { ApiGetCartRequest } from '../model/apiGetCartRequest';
import { ApiGetCartResponse } from '../model/apiGetCartResponse';


export interface CartServicePluginConfig {
  getCart: (
    config: {
      server: any;
      options: any;
      request: ApiGetCartRequest;
    }
  ) => Promise<ApiGetCartResponse>;
  warmCache?: (options: any) => void;
}

export class CartServicePlugin {
  readonly name = 'CartServicePlugin';
  readonly version = '1.0.0';
  readonly once = true;

  constructor(private readonly config: CartServicePluginConfig) {}

  readonly register = async (server, options) => {
    if (this.config.warmCache) {
      this.config.warmCache(options);
    }

    server.route({
      path: '/some_url/cart',
      method: 'POST',
      handler: async request => {
        try {
          return await this.config.getCart({ server, options, request });
        } catch (error) {
          return Boom.serverUnavailable(error);
        }
      }
    });
  };
}

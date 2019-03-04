## proto_swagger_gen

A bazel macro generates RPC services and stubs from a proto service file

* `service.proto -> swagger.json -> swagger-codegen -l typescript-angular`
* `service.proto -> swagger.json -> swagger-codegen -l typescript-hapi-server`
* ...

## Supported languages and plugins

Swagger codegen is invoked by this macro to generate services/stubs

It supports codegeneration in many languages out of the box

![Swagger codegen languages](https://swagger.io/swagger/media/Images/Tools/Opensource/Swagger_codegen.png?ext=.png)

If they are missing your favorite language or framework combination then you can generate a plugin. 

(More docs to come on plugin creation)

### Check out the example

- See `example/api` for how use the macro.
- See `example/proto_swagger_gen_out` to see the output of the macro.
- See `example/plugins/typescript-hapi-server` for how to create a plugin.
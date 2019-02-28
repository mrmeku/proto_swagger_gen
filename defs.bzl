load("@grpc_ecosystem_grpc_gateway//protoc-gen-swagger:defs.bzl", "protoc_gen_swagger")
load("@io_bazel_rules_openapi//openapi:openapi.bzl", "openapi_gen")

def proto_swagger_gen(name, proto, deps = []):
    native.proto_library(
        name = name + "_proto_library",
        srcs = [proto],
        deps = deps + [
            "@go_googleapis//google/api:annotations_proto",
        ],
    )

    protoc_gen_swagger(
        name = name,
        proto = ":" + name + "_proto_library",
    )

    openapi_gen(
        name = name + "_angular",
        language = "typescript-angular",
        spec = ":" + name,
        additional_properties = {
            "ngVersion": "6",
        },
    )

    openapi_gen(
        name = name + "_server",
        language = "typescript-hapi-server",
        spec = ":" + name,
        deps = [
            "//tools:typescript_hapi_server_swagger_codegen",
        ],
    )

# ✘-1 daniel-pc:~/proto_swagger_gen [hapi L|✚ 9 …1]> java -cp tools/swagger-codegen-cli.jar:tools/typescript-hapi-server-swagger-codegen.jar io.swagger.codegen.SwaggerCodegen generate -i bazel-out/k8-fastbuild/bin/api/api.swagger.json -l typescript-hapi-server -o bazel-out/k8-fastbuild/bin/api/api_server -D "" --additional-properties "" --type-mappings ""

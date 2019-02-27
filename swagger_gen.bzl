load("@grpc_ecosystem_grpc_gateway//protoc-gen-swagger:defs.bzl", "protoc_gen_swagger")
load("@io_bazel_rules_openapi//openapi:openapi.bzl", "openapi_gen")

def swagger_gen(name, proto_library):
    protoc_gen_swagger(
        name = name,
        proto = proto_library,
    )

    openapi_gen(
        name = name + "_angular",
        language = "typescript-angular",
        spec = ":" + name,
    )

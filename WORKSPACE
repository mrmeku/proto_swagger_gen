# The WORKSPACE file tells Bazel that this directory is a "workspace", which is like a project root.
# The content of this file specifies all the external dependencies Bazel needs to perform a build.

workspace(name = "proto_swagger_gen")

# This rule is built-into Bazel but we need to load it first to download more rules
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_jar")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

# Rules for producing a GRPC gateway and translating protocol buffers to swagger definitions
http_archive(
    name = "io_grpc_grpc_java",
    sha256 = "0b86e44f9530fd61eb044b3c64c7579f21857ba96bcd9434046fd22891483a6d",
    strip_prefix = "grpc-java-1.18.0",
    url = "https://github.com/grpc/grpc-java/archive/v1.18.0.tar.gz",
)

http_archive(
    name = "grpc_ecosystem_grpc_gateway",
    sha256 = "8b7afdfb6866c3f4d7630095fba1e7e7ff9b57491a5963d144ac58a9cc7dffa8",
    strip_prefix = "grpc-gateway-1.7.0",
    url = "https://github.com/grpc-ecosystem/grpc-gateway/archive/v1.7.0.zip",
)

# Swagger Code Gen Jar for producing Angular HTTP Services
http_jar(
    name = "io_swagger_swagger_codegen_cli",
    url = "https://oss.sonatype.org/content/repositories/snapshots/io/swagger/swagger-codegen-cli/3.0.0-SNAPSHOT/swagger-codegen-cli-3.0.0-20180710.190537-87.jar",
)

http_archive(
    name = "com_github_bazelbuild_buildtools",
    sha256 = "b5d7dbc6832f11b6468328a376de05959a1a9e4e9f5622499d3bab509c26b46a",
    strip_prefix = "buildtools-bf564b4925ab5876a3f64d8b90fab7f769013d42",
    url = "https://github.com/bazelbuild/buildtools/archive/bf564b4925ab5876a3f64d8b90fab7f769013d42.zip",
)

http_archive(
    name = "io_bazel_rules_go",
    sha256 = "492c3ac68ed9dcf527a07e6a1b2dcbf199c6bf8b35517951467ac32e421c06c1",
    urls = ["https://github.com/bazelbuild/rules_go/releases/download/0.17.0/rules_go-0.17.0.tar.gz"],
)

http_archive(
    name = "bazel_gazelle",
    sha256 = "7949fc6cc17b5b191103e97481cf8889217263acf52e00b560683413af204fcb",
    urls = ["https://github.com/bazelbuild/bazel-gazelle/releases/download/0.16.0/bazel-gazelle-0.16.0.tar.gz"],
)

rules_openapi_version = "0dfbfd09b54bad726547ec06f8c046a76be7f757"  # update this as needed

git_repository(
    name = "io_bazel_rules_openapi",
    commit = rules_openapi_version,
    remote = "git@github.com:mrmeku/rules_openapi.git",
)

load("@io_bazel_rules_openapi//openapi:openapi.bzl", "openapi_repositories")

openapi_repositories()

#######################################
# END DANS OWN DEPS
#######################################

load("@io_bazel_rules_go//go:deps.bzl", "go_register_toolchains", "go_rules_dependencies")

go_rules_dependencies()

go_register_toolchains()

load("@io_grpc_grpc_java//:repositories.bzl", "grpc_java_repositories")

grpc_java_repositories()

load("@bazel_gazelle//:deps.bzl", "gazelle_dependencies", "go_repository")

gazelle_dependencies()

# Also define in Gopkg.toml
go_repository(
    name = "com_github_ghodss_yaml",
    commit = "0ca9ea5df5451ffdf184b4428c902747c2c11cd7",
    importpath = "github.com/ghodss/yaml",
)

# Also define in Gopkg.toml
go_repository(
    name = "in_gopkg_yaml_v2",
    commit = "eb3733d160e74a9c7e442f435eb3bea458e1d19f",
    importpath = "gopkg.in/yaml.v2",
)

load("@com_github_bazelbuild_buildtools//buildifier:deps.bzl", "buildifier_dependencies")

buildifier_dependencies()

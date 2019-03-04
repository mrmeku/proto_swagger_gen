docker run -m 8GB --cpus 8 -e USER="$(id -u)" \
 -u="$(id -u)" \
 -v $(pwd):/src/workspace \
 -v $(pwd)/example/docker_cache:/tmp \
 -w /src/workspace \
 l.gcr.io/google/bazel:latest \
 --output_user_root=/tmp/build_output \
 --output_user_root=/tmp/execroot \
 --output_user_root=/tmp/cache \
 --output_user_root=/tmp/install \
 --output_user_root=/tmp/external \
 --output_user_root=/tmp/server \
 --output_base=/tmp build //example/api/...

rm -rf example/proto_swagger_gen_out
mkdir example/proto_swagger_gen_out

cp --no-preserve=mode,ownership -rLf example/docker_cache/execroot/proto_swagger_gen/bazel-out/k8-fastbuild/bin/example/api/* example/proto_swagger_gen_out
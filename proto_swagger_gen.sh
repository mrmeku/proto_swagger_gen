docker run -m 4GB --cpus 4 -e USER="$(id -u)" \
 -u="$(id -u)" \
 -v $(pwd):/src/workspace \
 -v $(pwd)/docker_cache:/tmp \
 -w /src/workspace \
 l.gcr.io/google/bazel:latest \
 --output_user_root=/tmp/build_output \
 --output_user_root=/tmp/execroot \
 --output_user_root=/tmp/cache \
 --output_user_root=/tmp/install \
 --output_user_root=/tmp/external \
 --output_user_root=/tmp/server \
 --output_base=/tmp build //api/...

rm -rf proto_swagger_gen_out
mkdir proto_swagger_gen_out

cp --no-preserve=mode,ownership -rLf docker_cache/execroot/proto_swagger_gen/bazel-out/k8-fastbuild/bin/api/* proto_swagger_gen_out
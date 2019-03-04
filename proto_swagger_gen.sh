docker run -m 8GB --cpus 8 -e USER="$(id -u)" \
 -u="$(id -u)" \
 -v ~/proto_swagger_gen:/src/workspace \
 -v ~/proto_swagger_gen/dist:/tmp/build_output \
 -w /src/workspace \
 l.gcr.io/google/bazel:latest \
 --output_user_root=/tmp/build_output \
 build //api/... --symlink_prefix=dist/

rm -rf proto_swagger_gen_out
mkdir proto_swagger_gen_out

cp --no-preserve=mode,ownership -rLf dist/a08c2e4811c846650b733c6fc815a920/execroot/proto_swagger_gen/bazel-out/k8-fastbuild/bin/api/* proto_swagger_gen_out
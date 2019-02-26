def _consolidate_swagger_impl(ctx):
    output = ctx.actions.declare_file("%s.swagger.json" % ctx.attr.name)

    args = ctx.actions.args()

    args.add(ctx.file._consolidate_swagger.path)
    args.add(output.path)
    for file in ctx.files.deps:
        args.add(file.path)

    outputs = [ctx.actions.declare_file("%s.swagger.json" % ctx.attr.name)]

    ctx.actions.run(
        executable = ctx.executable._node,
        inputs = [ctx.file._consolidate_swagger] + ctx.files.deps,
        outputs = outputs,
        arguments = [args],
    )

    return struct(files = depset(outputs))

consolidate_swagger = rule(
    attrs = {
        # openapi spec file
        "deps": attr.label_list(
            mandatory = True,
        ),
        "_consolidate_swagger": attr.label(
            default = Label("//tools:consolidate-swagger.js"),
            allow_single_file = FileType([".js"]),
        ),
        "_node": attr.label(
            default = Label("@nodejs//:node"),
            allow_single_file = True,
            cfg = "host",
            executable = True,
        ),
    },
    implementation = _consolidate_swagger_impl,
)

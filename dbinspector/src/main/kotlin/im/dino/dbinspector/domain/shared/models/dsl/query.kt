package im.dino.dbinspector.domain.shared.models.dsl

fun select(initializer: Select.() -> Unit): String {
    return Select().apply(initializer).build()
}

fun delete(initializer: Delete.() -> Unit): String {
    return Delete().apply(initializer).build()
}

fun dropView(initializer: DropView.() -> Unit): String {
    return DropView().apply(initializer).build()
}

fun dropTrigger(initializer: DropTrigger.() -> Unit): String {
    return DropTrigger().apply(initializer).build()
}

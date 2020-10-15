package im.dino.dbinspector.domain.shared.models.dsl.shared

import im.dino.dbinspector.domain.shared.models.dsl.conditions.And
import im.dino.dbinspector.domain.shared.models.dsl.conditions.Eq
import im.dino.dbinspector.domain.shared.models.dsl.conditions.Like
import im.dino.dbinspector.domain.shared.models.dsl.conditions.Or

abstract class Condition {

    protected abstract fun addCondition(condition: Condition)

    fun and(initializer: Condition.() -> Unit) {
        addCondition(And().apply(initializer))
    }

    fun or(initializer: Condition.() -> Unit) {
        addCondition(Or().apply(initializer))
    }

    infix fun String.eq(value: Any?) {
        addCondition(Eq(this, value))
    }

    infix fun String.like(value: Any?) {
        addCondition(Like(this, value))
    }
}

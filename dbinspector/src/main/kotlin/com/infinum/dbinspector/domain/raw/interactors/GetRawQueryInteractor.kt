package com.infinum.dbinspector.domain.raw.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.data.models.local.cursor.input.Query

internal class GetRawQueryInteractor(
    val source: Sources.Local.RawQuery
) : Interactors.GetRawQuery {

    override suspend fun invoke(input: Query): QueryResult =
        source.rawQuery(input)
}

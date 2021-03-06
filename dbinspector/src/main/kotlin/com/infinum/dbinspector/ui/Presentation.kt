package com.infinum.dbinspector.ui

import android.content.Context
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.ui.content.table.TableViewModel
import com.infinum.dbinspector.ui.content.trigger.TriggerViewModel
import com.infinum.dbinspector.ui.content.view.ViewViewModel
import com.infinum.dbinspector.ui.databases.DatabaseViewModel
import com.infinum.dbinspector.ui.databases.edit.EditDatabaseViewModel
import com.infinum.dbinspector.ui.edit.EditViewModel
import com.infinum.dbinspector.ui.pragma.PragmaViewModel
import com.infinum.dbinspector.ui.pragma.foreignkeys.ForeignKeysViewModel
import com.infinum.dbinspector.ui.pragma.indexes.IndexViewModel
import com.infinum.dbinspector.ui.pragma.tableinfo.TableInfoViewModel
import com.infinum.dbinspector.ui.schema.SchemaViewModel
import com.infinum.dbinspector.ui.schema.tables.TablesViewModel
import com.infinum.dbinspector.ui.schema.triggers.TriggersViewModel
import com.infinum.dbinspector.ui.schema.views.ViewsViewModel
import com.infinum.dbinspector.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object Presentation {

    object Constants {

        object Keys {
            const val DATABASE_PATH = "KEY_DATABASE_PATH"
            const val DATABASE_FILEPATH = "KEY_DATABASE_FILEPATH"
            const val DATABASE_NAME = "KEY_DATABASE_NAME"
            const val DATABASE_EXTENSION = "KEY_DATABASE_EXTENSION"
            const val SCHEMA_NAME = "KEY_SCHEMA_NAME"
            const val SHOULD_REFRESH = "KEY_SHOULD_REFRESH"
        }

        object Limits {
            const val PAGE_SIZE = 100
            const val INITIAL_PAGE = 1
        }

        object Settings {
            const val LINES_LIMIT_MINIMUM = 1
            const val LINES_LIMIT_MAXIMUM = 100
            const val BLOB_DATA_PLACEHOLDER = "[ DATA ]"
        }
    }

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    fun applicationContext(): Context {
        if (this::context.isInitialized) {
            return context.applicationContext
        } else {
            throw NullPointerException("Presentation context has not been initialized.")
        }
    }

    fun modules(): List<Module> =
        Domain.modules().plus(
            listOf(
                viewModels()
            )
        )

    private fun viewModels() = module {
        viewModel { DatabaseViewModel(get(), get(), get(), get(), get()) }
        viewModel { EditDatabaseViewModel(get(), get()) }

        viewModel { SettingsViewModel(get(), get(), get(), get(), get(), get(), get()) }

        viewModel { SchemaViewModel(get(), get()) }

        viewModel { TablesViewModel(get()) }
        viewModel { ViewsViewModel(get()) }
        viewModel { TriggersViewModel(get()) }

        viewModel { TableViewModel(get(), get(), get(), get(), get()) }
        viewModel { ViewViewModel(get(), get(), get(), get(), get()) }
        viewModel { TriggerViewModel(get(), get(), get(), get(), get()) }

        viewModel { PragmaViewModel(get(), get()) }

        viewModel { TableInfoViewModel(get()) }
        viewModel { ForeignKeysViewModel(get()) }
        viewModel { IndexViewModel(get()) }

        viewModel { EditViewModel(get(), get(), get(), get(), get(), get(), get()) }
    }
}

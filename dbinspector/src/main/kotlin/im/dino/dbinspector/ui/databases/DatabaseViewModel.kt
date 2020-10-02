package im.dino.dbinspector.ui.databases

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import im.dino.dbinspector.data.source.local.DatabaseManager
import im.dino.dbinspector.domain.pragma.database.VersionOperation
import im.dino.dbinspector.domain.database.models.Database
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import im.dino.dbinspector.ui.shared.bus.EventBus
import im.dino.dbinspector.ui.shared.bus.models.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

internal class DatabaseViewModel : BaseViewModel() {

    val databases: MutableLiveData<List<Database>> = MutableLiveData()

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun observe(action: suspend () -> Unit) =
        launch {
            io {
                EventBus.receive<Event.RefreshDatabases>().collectLatest { action() }
            }
        }

    fun browse() {
        launch {
            databases.value = io {
                DatabaseManager.browse().map {
                    Database(
                        absolutePath = it.absolutePath,
                        path = it.parentFile?.absolutePath.orEmpty(),
                        name = it.nameWithoutExtension,
                        extension = it.extension,
                        version = VersionOperation()(it.absolutePath, null)
                    )
                }
            }
        }
    }

    fun import(uris: List<Uri>) =
        launch {
            io {
                DatabaseManager.import(uris)
            }
            browse()
        }

    fun remove(databaseFilename: String) =
        launch {
            val ok = io {
                DatabaseManager.remove(databaseFilename)
            }
            if (ok) {
                browse()
            }
        }

    fun copy(databaseAbsolutePath: String, databasePath: String, databaseName: String, databaseExtension: String) =
        launch {
            val ok = io {
                DatabaseManager.copy(
                    databaseAbsolutePath,
                    databasePath,
                    databaseName,
                    databaseExtension
                )
            }
            if (ok) {
                browse()
            }
        }
}
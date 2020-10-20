package im.dino.dbinspector.ui.schema.shared

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorFragmentSchemaBinding
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.delegates.viewBinding
import im.dino.dbinspector.ui.shared.searchable.BaseSearchableFragment

internal abstract class SchemaFragment :
    BaseSearchableFragment(R.layout.dbinspector_fragment_schema) {

    companion object {

        fun bundle(databasePath: String, databaseName: String): Bundle =
            Bundle().apply {
                putString(Constants.Keys.DATABASE_PATH, databasePath)
                putString(Constants.Keys.DATABASE_NAME, databaseName)
            }
    }

    private lateinit var databasePath: String

    private lateinit var databaseName: String

    abstract var statement: String

    abstract val viewModel: SchemaSourceViewModel

    abstract fun childView(): Class<*>

    override val binding: DbinspectorFragmentSchemaBinding by viewBinding(
        DbinspectorFragmentSchemaBinding::bind
    )

    private lateinit var contract: ActivityResultLauncher<SchemaContract.Input>

    private lateinit var schemaAdapter: SchemaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            databasePath = it.getString(Constants.Keys.DATABASE_PATH, "")
            databaseName = it.getString(Constants.Keys.DATABASE_NAME, "")
        } ?: run {
            showError()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            schemaAdapter = SchemaAdapter(
                onClick = this@SchemaFragment::show
            )
            schemaAdapter.addLoadStateListener { loadState ->
                if (loadState.append.endOfPaginationReached) {
                    val isEmpty = schemaAdapter.itemCount < 1
                    emptyLayout.root.isVisible = isEmpty
                    swipeRefresh.isVisible = isEmpty.not()
                }
                if (loadState.prepend.endOfPaginationReached) {
                    swipeRefresh.isRefreshing = loadState.refresh !is LoadState.NotLoading
                }
            }

            swipeRefresh.setOnRefreshListener {
                schemaAdapter.refresh()
            }

            emptyLayout.retryButton.setOnClickListener {
                schemaAdapter.refresh()
            }

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            recyclerView.adapter = schemaAdapter

            contract = registerForActivityResult(SchemaContract()) { shouldRefresh ->
                if (shouldRefresh) {
                    schemaAdapter.refresh()
                }
            }
        }

        query(searchQuery())
    }

    override fun onDestroyView() {
        contract.unregister()
        super.onDestroyView()
    }

    override fun search(query: String?) {
        query(query)
//        schemaAdapter.refresh()
    }

    private fun query(query: String?) {
        viewModel.query(databasePath, query) {
            schemaAdapter.submitData(it)
        }
    }

    private fun show(name: String) =
        contract.launch(
            SchemaContract.Input(
                childView = childView(),
                databasePath = databasePath,
                databaseName = databaseName,
                schemaName = name
            )
        )

    private fun showError() {
        println("Some error")
    }
}

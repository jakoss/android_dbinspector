package im.dino.dbinspector.ui.pragma.indexes

import android.os.Bundle
import android.view.View
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorFragmentPragmaBinding
import im.dino.dbinspector.domain.pragma.models.IndexListColumns
import im.dino.dbinspector.ui.shared.base.BaseFragment
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.delegates.viewBinding
import im.dino.dbinspector.ui.pragma.PragmaAdapter

internal class IndexesFragment : BaseFragment(R.layout.dbinspector_fragment_pragma), SwipeRefreshLayout.OnRefreshListener {

    companion object {

        fun newInstance(databasePath: String, tableName: String): IndexesFragment =
            IndexesFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.Keys.DATABASE_PATH, databasePath)
                    putString(Constants.Keys.SCHEMA_NAME, tableName)
                }
            }
    }

    private lateinit var databasePath: String
    private lateinit var schemaName: String

    override val binding: DbinspectorFragmentPragmaBinding by viewBinding(
        DbinspectorFragmentPragmaBinding::bind
    )

    private val viewModel by viewModels<IndexViewModel>()

    private val adapter = PragmaAdapter(IndexListColumns.values().map { it.name.toLowerCase() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            databasePath = it.getString(Constants.Keys.DATABASE_PATH, "")
            schemaName = it.getString(Constants.Keys.SCHEMA_NAME, "")
        } ?: run {
            // TODO: Show error state
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.recyclerView) {
            updateLayoutParams {
                minimumWidth = resources.displayMetrics.widthPixels
            }
        }

        with(binding) {
            swipeRefresh.setOnRefreshListener(this@IndexesFragment)

            recyclerView.layoutManager = GridLayoutManager(recyclerView.context, IndexListColumns.values().size)
            recyclerView.adapter = adapter

            query()
        }
    }

    override fun onRefresh() {
        with(binding) {
            swipeRefresh.isRefreshing = false

            adapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())

            query()
        }
    }

    private fun query() =
        viewModel.query(databasePath, schemaName) {
            adapter.submitData(it)
        }
}
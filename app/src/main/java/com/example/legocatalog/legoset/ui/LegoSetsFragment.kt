package com.example.legocatalog.legoset.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.legocatalog.*
import com.example.legocatalog.databinding.FragmentLegosetsBinding
import com.example.legocatalog.extensions.GridSpacingItemDecoration
import com.example.legocatalog.extensions.VerticalItemDecoration
import com.example.legocatalog.extensions.hide
import com.example.legocatalog.extensions.setTitle
import com.example.legocatalog.legoset.data.LegoSetRepository
import com.example.legocatalog.util.ConnectivityUtil


class LegoSetsFragment : Fragment() {

    private lateinit var viewModel: LegoSetsViewModel

    private val args: LegoSetsFragmentArgs by navArgs()

    private lateinit var binding: FragmentLegosetsBinding
    private val adapter: LegoSetAdapter by lazy { LegoSetAdapter() }
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var gridLayoutManager: GridLayoutManager
    private val linearDecoration: RecyclerView.ItemDecoration by lazy {
        VerticalItemDecoration(
            resources.getDimension(R.dimen.margin_normal).toInt())
    }
    private val gridDecoration: RecyclerView.ItemDecoration by lazy {
        GridSpacingItemDecoration(
            SPAN_COUNT, resources.getDimension(R.dimen.margin_grid).toInt())
    }

    private var isLinearLayoutManager: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =  ViewModelProvider.AndroidViewModelFactory(
            requireActivity().application
        ).create(LegoSetsViewModel::class.java)
        viewModel.connectivityAvailable = ConnectivityUtil.isConnected(context!!)
        viewModel.themeId = if (args.themeId == -1) null else args.themeId

        binding = FragmentLegosetsBinding.inflate(inflater, container, false)
        context ?: return binding.root

        linearLayoutManager = LinearLayoutManager(activity)
        gridLayoutManager = GridLayoutManager(activity, SPAN_COUNT)
        setLayoutManager()
        binding.recyclerView.adapter = adapter

        args.themeName?.let {  setTitle(it) }
        subscribeUi(adapter)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_representation, menu)
        setDataRepresentationIcon(menu.findItem(R.id.list))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list -> {
                isLinearLayoutManager = !isLinearLayoutManager
                setDataRepresentationIcon(item)
                setLayoutManager()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeUi(adapter: LegoSetAdapter) {
        viewModel.legoSets.observe(viewLifecycleOwner) {
            binding.progressBar.hide()
            adapter.submitList(it)
        }
    }

    private fun setLayoutManager() {
        val recyclerView = binding.recyclerView

        var scrollPosition = 0
        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.layoutManager != null) {
            scrollPosition = (recyclerView.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        }

        if (isLinearLayoutManager) {
            recyclerView.removeItemDecoration(gridDecoration)
            recyclerView.addItemDecoration(linearDecoration)
            recyclerView.layoutManager = linearLayoutManager
        } else {
            recyclerView.removeItemDecoration(linearDecoration)
            recyclerView.addItemDecoration(gridDecoration)
            recyclerView.layoutManager = gridLayoutManager
        }

        recyclerView.scrollToPosition(scrollPosition)
    }

    private fun setDataRepresentationIcon(item: MenuItem) {
        item.setIcon(if (isLinearLayoutManager)
            R.drawable.ic_grid_list_24dp else R.drawable.ic_list_white_24dp)
    }

    companion object {
        const val SPAN_COUNT = 3
    }

}
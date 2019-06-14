package com.chrisa.cakeapp.presentation.cakes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.chrisa.cakeapp.R
import com.chrisa.cakeapp.data.api.cakes.Cake
import com.chrisa.cakeapp.presentation.utils.SlideUpItemAnimator
import com.chrisa.core.viewmodel.ViewModelFactory
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.jakewharton.rxrelay2.PublishRelay
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.cakes.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CakesFragment : Fragment() {

    private val viewDisposables = CompositeDisposable()
    private val nonViewDisposables = CompositeDisposable()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CakesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CakesViewModel::class.java)
    }
    private val cakesAdapter: CakesAdapter by lazy { CakesAdapter(requireContext(), CakeDiffItemCallback()) }
    private val intentProcessor: PublishRelay<CakesIntent> = PublishRelay.create()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        nonViewDisposables.add(viewModel.processIntents(intentProcessor))
        intentProcessor.accept(CakesIntent.Initialize)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.cakes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.item_divider_vertical
            )!!
        )

        cakes_recyclerview.addItemDecoration(dividerItemDecoration)
        cakes_recyclerview.itemAnimator = SlideUpItemAnimator()
        cakes_recyclerview.adapter = cakesAdapter
    }

    override fun onStart() {
        super.onStart()
        viewDisposables.addAll(
            viewModel.states()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { processViewState(it) },
            viewIntents().subscribe(intentProcessor)
        )
    }

    override fun onStop() {
        viewDisposables.clear()
        super.onStop()
    }

    override fun onDetach() {
        nonViewDisposables.clear()
        super.onDetach()
    }

    private fun viewIntents(): Observable<CakesIntent> {
        return Observable.mergeArray(
            cakes_swipe_refresh.refreshes().map { CakesIntent.Refresh },
            // Throttle to avoid double taps
            cakesAdapter.cakeClicked.map(CakesIntent::ShowCake)
                .throttleFirst(1, TimeUnit.SECONDS)
        )
    }

    private fun processViewState(viewState: CakesViewState) {
        cakes_swipe_refresh.isRefreshing = viewState.isLoadingCakes

        cakesAdapter.submitList(viewState.cakes)

        cakes_error_view.visibility = if (viewState.isErrorVisible) View.VISIBLE else View.GONE
        cakes_empty_view.visibility = if (viewState.isEmptyStateVisible) View.VISIBLE else View.GONE

        if (viewState.selectedCake != null) {
            showCake(viewState.selectedCake)
        }
    }

    private fun showCake(cake: Cake) {
        CakeDialogHelper.show(requireActivity(), cake)
        intentProcessor.accept(CakesIntent.CakeDisplayed)
    }
}

package com.chrisa.cakeapp.presentation.cakes

import androidx.annotation.VisibleForTesting
import com.chrisa.cakeapp.domain.cakes.CakesUseCase
import com.chrisa.core.mvi.MviViewModel
import io.reactivex.Observable
import javax.inject.Inject

internal class CakesViewModel @VisibleForTesting constructor(
    state: CakesViewState,
    private val useCase: CakesUseCase
) : MviViewModel<CakesIntent, CakesPartialState, CakesViewState>(state) {

    @Inject
    constructor(useCase: CakesUseCase) : this(CakesViewState.default(), useCase)

    /**
     * Only accept the first Initialize intent we recieve here
     */
    override fun filterIntents(intents: Observable<CakesIntent>): Observable<CakesIntent> = Observable.merge(
        intents.ofType(CakesIntent.Initialize::class.java).take(1),
        intents.filter { !CakesIntent.Initialize::class.java.isInstance(it) }
    )

    override fun dispatchIntent(intent: CakesIntent): Observable<CakesPartialState> {
        return when (intent) {
            CakesIntent.Initialize -> getCakes()
            CakesIntent.Refresh -> getCakes()
            CakesIntent.CakeDisplayed -> Observable.just(CakesPartialState.CakeDisplayed)
            is CakesIntent.ShowCake -> Observable.just(CakesPartialState.CakeClicked(intent.cake))
        }
    }

    private fun getCakes(): Observable<CakesPartialState> {
        return useCase.getCakes()
            .toObservable()
            .map<CakesPartialState>(CakesPartialState.LoadCakesPartialState::Success)
            .startWith(CakesPartialState.LoadCakesPartialState.InFlight)
            .onErrorReturn { mapError(it) }
    }

    companion object {
        fun mapError(error: Throwable): CakesPartialState {
            // TODO: We could look at the error here and define different error view states if required.
            return CakesPartialState.LoadCakesPartialState.Error
        }
    }
}

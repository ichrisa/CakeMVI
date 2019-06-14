package com.chrisa.core.mvi

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.disposables.Disposable

abstract class MviViewModel<I : MviIntent, PS : MviPartialState<VS>, VS : MviViewState>(initialState: VS) :
    ViewModel() {

    private val intentsSubject: PublishRelay<I> = PublishRelay.create()

    /** This is the state machine that drives the MVI state
     * compose is used to filter any intents we don't want to handle
     * flatmap dispatches the intent to the ViewModel which converts an Intent to a PartialState
     * scan performs the redux operation - we pass the previous state into the partial state and its reduced to a new state
     * distinctUntilChanged means we only emit a new state if its changed
     * */
    private val statesObservable: Observable<VS> = intentsSubject
        .compose { intents -> intents.publish { s -> filterIntents(s) } }
        .flatMap { intent -> dispatchIntent(intent) }
        .scan(initialState) { previousState: VS, partialState: PS -> partialState.reduce(previousState) }
        .distinctUntilChanged()
        .replay(1)
        .autoConnect(0)

    abstract fun dispatchIntent(intent: I): Observable<PS>

    /**
     *  Override if you want to filter intents - by default will return an unfiltered source
     */
    open fun filterIntents(intents: Observable<I>): ObservableSource<I> = intents

    /**
     * public method called to process a source of input intents
     */
    fun processIntents(intents: Observable<I>): Disposable {
        return intents.subscribe(intentsSubject::accept)
    }

    /**
     * public method called to observe the ViewState
     */
    fun states(): Observable<VS> = statesObservable
}

package com.chrisa.cakeapp.presentation.cakes

import com.chrisa.core.mvi.MviPartialState
import com.chrisa.cakeapp.data.api.cakes.Cake

internal sealed class CakesPartialState : MviPartialState<CakesViewState> {

    object CakeDisplayed : CakesPartialState()
    data class CakeClicked(val cake: Cake) : CakesPartialState()

    sealed class LoadCakesPartialState : CakesPartialState() {
        object InFlight : LoadCakesPartialState()
        object Error : LoadCakesPartialState()
        data class Success(val cakes: List<Cake>) : LoadCakesPartialState()
    }

    override fun reduce(previousState: CakesViewState): CakesViewState {
        return when (this) {
            is LoadCakesPartialState -> reduceLoadCakesPartialState(previousState, this)
            is CakeClicked -> previousState.copy(
                selectedCake = this.cake
            )
            CakeDisplayed -> previousState.copy(
                selectedCake = null
            )
        }
    }

    companion object {
        private val reduceLoadCakesPartialState = { previousState: CakesViewState, result: LoadCakesPartialState ->
            when (result) {
                LoadCakesPartialState.InFlight -> previousState.copy(
                    isLoadingCakes = true
                )
                // TODO: Error handling is pretty basic here, we could have create different error states
                //  depending on the cake list state if required, e.g. if we have data then we could display
                //  a snackbar/toast error instead of taking up the full screen. For simplicity this clears
                //  the list when any errors are reported.
                LoadCakesPartialState.Error -> previousState.copy(
                    isLoadingCakes = false,
                    isEmptyStateVisible = false,
                    isErrorVisible = true,
                    cakes = null
                )
                is LoadCakesPartialState.Success -> previousState.copy(
                    isLoadingCakes = false,
                    isErrorVisible = false,
                    cakes = result.cakes,
                    isEmptyStateVisible = result.cakes.isEmpty()
                )
            }
        }
    }
}

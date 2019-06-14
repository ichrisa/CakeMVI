package com.chrisa.cakeapp.presentation.cakes

import com.chrisa.cakeapp.data.api.cakes.Cake
import com.chrisa.core.mvi.MviViewState

internal data class CakesViewState(
    val isLoadingCakes: Boolean,
    val isEmptyStateVisible: Boolean,
    val isErrorVisible: Boolean,
    val selectedCake: Cake?,
    val cakes: List<Cake>?
) : MviViewState {

    companion object {
        fun default(): CakesViewState = CakesViewState(
            isLoadingCakes = false,
            isEmptyStateVisible = false,
            cakes = null,
            selectedCake = null,
            isErrorVisible = false
        )
    }
}

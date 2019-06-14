package com.chrisa.cakeapp.presentation.cakes

import com.chrisa.core.mvi.MviIntent
import com.chrisa.cakeapp.data.api.cakes.Cake

internal sealed class CakesIntent : MviIntent {
    object Initialize : CakesIntent()
    object Refresh : CakesIntent()
    object CakeDisplayed : CakesIntent()
    data class ShowCake(val cake: Cake) : CakesIntent()
}

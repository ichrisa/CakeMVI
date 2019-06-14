package com.chrisa.core.mvi

/**
 * A data class describing how the view should be rendered. It is typically implemented as a
 * `sealed class` that is used in conjunction with an exhaustive `when` statement in MivRender#renderer.
 */
interface MviViewState

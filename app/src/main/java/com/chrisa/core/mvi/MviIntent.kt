package com.chrisa.core.mvi

/**
 * A set of user interface interactions that are dispatched from Android Framework Components.
 * It is typically implemented as a `sealed class` that is used in conjunction with an exhaustive
 * `when` statement in MviViewModel#dispatchIntent.
 */
interface MviIntent

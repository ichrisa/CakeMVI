package com.chrisa.core.mvi

interface MviPartialState<VS> {
    fun reduce(previousState: VS): VS
}

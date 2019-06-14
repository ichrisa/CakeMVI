package com.chrisa.cakeapp.presentation.cakes

import com.chrisa.cakeapp.data.api.cakes.Cake
import com.chrisa.cakeapp.domain.cakes.CakesUseCase
import com.chrisa.core.rx.Rx2SchedulersJunitRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CakesViewModelTest {

    @Rule
    @JvmField
    val rx2SchedulersRule = Rx2SchedulersJunitRule()

    private val cakesUseCase = mock<CakesUseCase>()

    private lateinit var cakesViewModel: CakesViewModel

    @Before
    fun setup() {
        cakesViewModel = CakesViewModel(cakesUseCase)
    }

    @Test
    fun `when Initialize intent processed and getCakes fails the ViewModel emits the error`() {

        val state = cakesViewModel.states().test()

        val error = RuntimeException("Test error")

        whenever(cakesUseCase.getCakes()).thenReturn(Single.error(error))

        cakesViewModel.processIntents(Observable.just(CakesIntent.Initialize))

        val stateValues = state.values()

        assertThat(stateValues).hasSize(3)
        assertThat(stateValues[0]).isEqualTo(CakesViewState.default())
        assertThat(stateValues[1]).isEqualTo(CakesViewState.default().copy(isLoadingCakes = true))
        assertThat(stateValues[2]).isEqualTo(
            CakesViewState.default().copy(
                isLoadingCakes = false,
                isEmptyStateVisible = false,
                isErrorVisible = true,
                cakes = null
            )
        )
    }

    @Test
    fun `when Initialize intent processed and getCakes succeeds with a non-empty list the ViewModel emits the cakes list`() {

        val state = cakesViewModel.states().test()

        val cakes = listOf(
            Cake(
                title = "Cake",
                description = "Cake Description",
                imageUrl = "https://www.cake.com/cake.jpg"
            )
        )

        whenever(cakesUseCase.getCakes()).thenReturn(Single.just(cakes))

        cakesViewModel.processIntents(Observable.just(CakesIntent.Initialize))

        val stateValues = state.values()

        assertThat(stateValues).hasSize(3)
        assertThat(stateValues[0]).isEqualTo(CakesViewState.default())
        assertThat(stateValues[1]).isEqualTo(CakesViewState.default().copy(isLoadingCakes = true))
        assertThat(stateValues[2]).isEqualTo(
            CakesViewState.default().copy(
                isLoadingCakes = false,
                isEmptyStateVisible = false,
                cakes = cakes
            )
        )
    }

    @Test
    fun `when Initialize intent processed and getCakes succeeds with an empty list the ViewModel emits the cakes list`() {

        val state = cakesViewModel.states().test()

        val cakes = emptyList<Cake>()

        whenever(cakesUseCase.getCakes()).thenReturn(Single.just(cakes))

        cakesViewModel.processIntents(Observable.just(CakesIntent.Initialize))

        val stateValues = state.values()

        assertThat(stateValues).hasSize(3)
        assertThat(stateValues[0]).isEqualTo(CakesViewState.default())
        assertThat(stateValues[1]).isEqualTo(CakesViewState.default().copy(isLoadingCakes = true))
        assertThat(stateValues[2]).isEqualTo(
            CakesViewState.default().copy(
                isLoadingCakes = false,
                isEmptyStateVisible = true,
                cakes = cakes
            )
        )
    }

    @Test
    fun `when Refresh intent processed and getCakes fails the ViewModel emits the error`() {

        val state = cakesViewModel.states().test()

        val error = RuntimeException("Test error")

        whenever(cakesUseCase.getCakes()).thenReturn(Single.error(error))

        cakesViewModel.processIntents(Observable.just(CakesIntent.Refresh))

        val stateValues = state.values()

        assertThat(stateValues).hasSize(3)
        assertThat(stateValues[0]).isEqualTo(CakesViewState.default())
        assertThat(stateValues[1]).isEqualTo(CakesViewState.default().copy(isLoadingCakes = true))
        assertThat(stateValues[2]).isEqualTo(
            CakesViewState.default().copy(
                isLoadingCakes = false,
                isEmptyStateVisible = false,
                isErrorVisible = true,
                cakes = null
            )
        )
    }

    @Test
    fun `when Refresh intent processed and getCakes succeeds non-empty the ViewModel emits the cakes list`() {

        val state = cakesViewModel.states().test()

        val cakes = listOf(
            Cake(
                title = "Cake",
                description = "Cake Description",
                imageUrl = "https://www.cake.com/cake.jpg"
            )
        )

        whenever(cakesUseCase.getCakes()).thenReturn(Single.just(cakes))

        cakesViewModel.processIntents(Observable.just(CakesIntent.Refresh))

        val stateValues = state.values()

        assertThat(stateValues).hasSize(3)
        assertThat(stateValues[0]).isEqualTo(CakesViewState.default())
        assertThat(stateValues[1]).isEqualTo(CakesViewState.default().copy(isLoadingCakes = true))
        assertThat(stateValues[2]).isEqualTo(
            CakesViewState.default().copy(
                isLoadingCakes = false,
                isEmptyStateVisible = false,
                cakes = cakes
            )
        )
    }

    @Test
    fun `when Refresh intent processed and getCakes succeeds with an empty list the ViewModel emits the cakes list`() {

        val state = cakesViewModel.states().test()

        val cakes = emptyList<Cake>()

        whenever(cakesUseCase.getCakes()).thenReturn(Single.just(cakes))

        cakesViewModel.processIntents(Observable.just(CakesIntent.Refresh))

        val stateValues = state.values()

        assertThat(stateValues).hasSize(3)
        assertThat(stateValues[0]).isEqualTo(CakesViewState.default())
        assertThat(stateValues[1]).isEqualTo(CakesViewState.default().copy(isLoadingCakes = true))
        assertThat(stateValues[2]).isEqualTo(
            CakesViewState.default().copy(
                isLoadingCakes = false,
                isEmptyStateVisible = true,
                cakes = cakes
            )
        )
    }

    @Test
    fun `when ShowCake intent processed the ViewModel emits the selected cake`() {

        val state = cakesViewModel.states().test()

        val cake = Cake(
            title = "Cake",
            description = "Cake Description",
            imageUrl = "https://www.cake.com/cake.jpg"
        )

        cakesViewModel.processIntents(Observable.just(CakesIntent.ShowCake(cake)))

        val stateValues = state.values()

        assertThat(stateValues).hasSize(2)
        assertThat(stateValues[0]).isEqualTo(CakesViewState.default())
        assertThat(stateValues[1]).isEqualTo(CakesViewState.default().copy(selectedCake = cake))
    }

    @Test
    fun `when CakeDisplayed intent processed the ViewModel resets the selected cake`() {

        val cake = Cake(
            title = "Cake",
            description = "Cake Description",
            imageUrl = "https://www.cake.com/cake.jpg"
        )
        val startState = CakesViewState.default().copy(selectedCake = cake)
        val viewModelWithStartState = CakesViewModel(useCase = cakesUseCase, state = startState)

        val state = viewModelWithStartState.states().test()

        viewModelWithStartState.processIntents(Observable.just(CakesIntent.CakeDisplayed))

        val stateValues = state.values()

        assertThat(stateValues).hasSize(2)
        assertThat(stateValues[0]).isEqualTo(startState)
        assertThat(stateValues[1]).isEqualTo(startState.copy(selectedCake = null))
    }
}
package com.chrisa.cakeapp.domain.cakes

import com.chrisa.cakeapp.data.api.cakes.Cake
import com.chrisa.cakeapp.data.api.cakes.CakeApi
import com.chrisa.core.rx.Rx2SchedulersJunitRule
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CakesUseCaseImplTest {

    @Rule
    @JvmField
    val rule = Rx2SchedulersJunitRule()

    private val cakeApi = mock<CakeApi>()

    private lateinit var cakesUseCaseImpl: CakesUseCaseImpl

    @Before
    fun setup() {
        cakesUseCaseImpl = CakesUseCaseImpl(cakeApi)
    }

    @Test
    fun `when cakeApi succeeds with duplicates the UseCase emits a distinct list`() {

        val duplicateList = (0..2).map {
            Cake(
                title = "Cake",
                description = "A description",
                imageUrl = "https://www.cakes.com/cakea.jpg"
            )
        }

        whenever(cakeApi.getCakes()).thenReturn(Single.just(duplicateList))

        val emittedList = cakesUseCaseImpl.getCakes().test()

        emittedList.assertValue(duplicateList.distinct())
    }

    @Test
    fun `when cakeApi succeeds with an unorderedList the UseCase emits a list ordered by title`() {

        val unorderedList = listOf(
            Cake(
                title = "Z Cake",
                description = "",
                imageUrl = ""
            ),
            Cake(
                title = "A Cake",
                description = "",
                imageUrl = ""
            )
        )

        whenever(cakeApi.getCakes()).thenReturn(Single.just(unorderedList))

        val emittedList = cakesUseCaseImpl.getCakes().test()

        emittedList.assertValue(unorderedList.sortedBy { it.title })
    }

    @Test
    fun `when cakeApi succeeds with plain text image urls the UseCase emits secure image urls`() {

        val plainTextUrls = listOf(
            Cake(
                title = "A Cake",
                description = "A description",
                imageUrl = "http://www.cakes.com/cakea.jpg"
            )
        )

        whenever(cakeApi.getCakes()).thenReturn(Single.just(plainTextUrls))

        val emittedList = cakesUseCaseImpl.getCakes().test()

        emittedList.assertValue(plainTextUrls.map { cake ->
            cake.copy(
                imageUrl = cake.imageUrl.replace(
                    "http:",
                    "https:"
                )
            )
        })
    }

    @Test
    fun `when cakeApi errors the UseCase emits the error`() {

        val error = RuntimeException("Test error")

        whenever(cakeApi.getCakes()).thenReturn(Single.error(error))

        val emittedList = cakesUseCaseImpl.getCakes().test()

        emittedList.assertError(error)
    }
}
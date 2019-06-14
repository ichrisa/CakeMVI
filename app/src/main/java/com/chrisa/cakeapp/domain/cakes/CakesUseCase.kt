package com.chrisa.cakeapp.domain.cakes

import com.chrisa.cakeapp.data.api.cakes.Cake
import com.chrisa.cakeapp.data.api.cakes.CakeApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface CakesUseCase {
    fun getCakes(): Single<List<Cake>>
}

internal class CakesUseCaseImpl @Inject constructor(
    private val cakeApi: CakeApi
) : CakesUseCase {

    override fun getCakes(): Single<List<Cake>> {
        return cakeApi.getCakes()
            .map(mapCakeList)
            .map { cakeList -> cakeList.map(mapCake) }
            .subscribeOn(Schedulers.io())
    }

    companion object {

        private val mapCakeList = { cakes: List<Cake> ->
            cakes.distinct()
                .sortedBy { it.title }
        }

        private val mapCake = { cake: Cake ->
            cake.copy(
                // For Android Q cleartext is disabled. We need to use https for images.
                imageUrl = cake.imageUrl.replace("http:", "https:")
            )
        }
    }
}
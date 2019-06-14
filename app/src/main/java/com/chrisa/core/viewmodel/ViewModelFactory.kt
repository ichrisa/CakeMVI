package com.chrisa.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(viewModelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[viewModelClass]

        if (creator == null) {
            for ((key, value) in creators) {
                if (viewModelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }

        if (creator == null) {
            throw IllegalArgumentException("unknown ViewModel: $viewModelClass")
        }

        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException("Error creating ViewModel: $viewModelClass", e)
        }
    }
}

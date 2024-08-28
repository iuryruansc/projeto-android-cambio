package com.currencyview.common.util.testing

import com.currencyview.common.idling.ApiIdlingResource

class EspressoIdlingResourceHelper : IdlingResourceHelper {
    override fun increment() = ApiIdlingResource.increment()

    override fun decrement() = ApiIdlingResource.decrement()

}
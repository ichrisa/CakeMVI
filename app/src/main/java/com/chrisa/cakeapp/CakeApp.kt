package com.chrisa.cakeapp

import dagger.android.support.DaggerApplication

class CakeApp : DaggerApplication() {
    override fun applicationInjector(): ApplicationComponent {
        return Config.applicationInjector(this)
    }
}
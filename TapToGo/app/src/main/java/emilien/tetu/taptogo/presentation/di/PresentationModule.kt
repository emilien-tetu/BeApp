package emilien.tetu.taptogo.presentation.di

import emilien.tetu.taptogo.domain.di.DomainModule
import emilien.tetu.taptogo.domain.interactor.HomePresenter
import emilien.tetu.taptogo.presentation.controller.HomeController
import emilien.tetu.taptogo.presentation.presenter.HomePresenterImpl
import emilien.tetu.taptogo.presentation.viewmodel.HomeViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object PresentationModule {
    fun loadModules() {
        loadKoinModules(
            listOf(
                homePresentationModule,
            )
        )
        DomainModule.loadModules()
    }
}

val homePresentationModule = module {
    single { HomeViewModel() }
    factory { HomeController(get()) }
    factory<HomePresenter> { HomePresenterImpl(get()) }
}

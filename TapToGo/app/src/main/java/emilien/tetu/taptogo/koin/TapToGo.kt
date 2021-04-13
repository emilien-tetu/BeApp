package emilien.tetu.taptogo.koin

import android.app.Application
import org.koin.core.context.startKoin

class TapToGo : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            AppModule.loadModules()
        }
    }
}
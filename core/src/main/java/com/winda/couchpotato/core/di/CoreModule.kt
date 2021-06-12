package com.winda.couchpotato.core.di

import androidx.room.Room
import com.winda.couchpotato.core.BuildConfig
import com.winda.couchpotato.core.data.source.MovieDatabaseRepository
import com.winda.couchpotato.core.data.source.local.LocalDataSource
import com.winda.couchpotato.core.data.source.local.room.AppDatabase
import com.winda.couchpotato.core.data.source.remote.RemoteDataSource
import com.winda.couchpotato.core.data.source.remote.api.ApiService
import com.winda.couchpotato.core.domain.repository.IMovieDatabaseDataSource
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object CoreModule {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = BuildConfig.API_KEY_TMDB
    const val BASE_URL_POSTER = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2"
    const val BASE_URL_BACKDROP = "https://www.themoviedb.org/t/p/w1920_and_h800_multi_faces"

    val databaseModule = module {
        factory { get<AppDatabase>().movieDao() }
        single {
            val passphrase: ByteArray = SQLiteDatabase.getBytes("winda".toCharArray())
            val factory = SupportFactory(passphrase)

            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "couch_potato_db"
            ).fallbackToDestructiveMigration()
                .openHelperFactory(factory)
                .build()

//            Room.databaseBuilder(
//                androidContext(),
//                AppDatabase::class.java,
//                "couch_potato_db"
//            ).fallbackToDestructiveMigration()
//                .build()
        }
    }

    val networkModule = module {
        single {
            val hostname = "api.themoviedb.org"
            val certificatePin = CertificatePinner.Builder()
                .add(hostname, "sha256/+vqZVAzTqUP8BGkfl88yU7SQ3C8J2uNEa55B7RZjEg0=")
                .build()
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(certificatePinner = certificatePin)
                .build()
        }
        single {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get())
                .build()
            retrofit.create(ApiService::class.java)
        }
    }

    val repositoryModule = module {
        single { RemoteDataSource(get()) }
        single { LocalDataSource(get()) }
        single<IMovieDatabaseDataSource> {
            MovieDatabaseRepository(remoteDataSource = get(), localDataSource = get())
        }
    }
}
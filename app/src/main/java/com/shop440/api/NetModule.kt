package com.shop440.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shop440.Application
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * Created by mmumene on 03/09/2017.
 */

object NetModule {

    private fun providesGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        return gsonBuilder.create()
    }

    private fun provideOkhttpClient(): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024
        val cache = Cache(Application.getsInstance()!!.cacheDir, cacheSize.toLong())
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
        client.addInterceptor(interceptor)
        client.addInterceptor { chain ->
            val request = chain.request().newBuilder()
            request.addHeader("X-AUTH-TOKEN", Application.authToken)
            request.addHeader("Content-Type", "application/json")
            chain.proceed(request.build())
        }
        client.connectTimeout(30, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        client.writeTimeout(30, TimeUnit.SECONDS)
        client.cache(cache)
        return client.build()
    }

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(providesGson()))
                .baseUrl(Urls.BASE_URL)
                .client(provideOkhttpClient())
                .build()
    }

    private class NullOnEmptyConverterFactory : Converter.Factory() {

        override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, Any>? {
            val delegate = retrofit!!.nextResponseBodyConverter<Any>(this, type!!, annotations!!)
            return Converter{ body -> if (body.contentLength() == 0L) null else delegate.convert(body) }
        }
    }
}

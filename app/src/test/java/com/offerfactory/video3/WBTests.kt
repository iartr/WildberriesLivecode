package com.offerfactory.video3

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.Test

@OptIn(DelicateCoroutinesApi::class)
class WBTests {

    // Задача №1. Что будет выведено?
    @Test
    fun wildberriesStructurredConcurrency() {
        val coroutineContext = Job() + Dispatchers.Default
        val coroutineScope = CoroutineScope(coroutineContext)

        coroutineScope.launch { // корневая
            val request = launch { // request
                GlobalScope.launch { // GS, не наследует Job от корневого
                    delay(1000)
                    println("1") // точно будет выведено
                }

                launch { // Внутренняя
                    delay(100)
                    println("2")
                    delay(1000)
                    println("3")
                }
            }

            delay(500)
            request.cancel()
            delay(1000)
            println("4")
        }



        runBlocking {
            delay(3000L)
            coroutineScope.coroutineContext.job.cancelAndJoin()
        }
    }
}



interface BackendApi {
    suspend fun apiCall(): String // very long call
}

// Сделать код-ревью и рефакторинг по необходимости
class Repository(private val backendApi: BackendApi) {
    private var cache: String? = null
    private val mutex = Mutex()

    suspend fun apiCallOrCache(): String {
        cache?.let { return it }

        return mutex.withLock {
            cache ?: backendApi.apiCall().also {
                cache = it
            }
        }
    }
}
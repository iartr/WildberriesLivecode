# Livecode задачи в Wildberries

В январе 2024 года получил оффер в WB на 600к. В этом репозитории разбор трех лайвкод задач с собеса.   
   
**Смотреть видео с разбором**: https://youtu.be/vSHqR8W6-pY?si=l4FrJSWTT2nlDE8d   
   
**Менторство**: https://iartr.notion.site/mentor 
   
## Задача №1
Что будет выведено?
```kotlin
val coroutineContext = Job() + Dispatchers.Default
val coroutineScope = CoroutineScope(coroutineContext)

coroutineScope.launch {
    val request = launch {
        GlobalScope.launch {
            delay(1000)
            println("1")
        }

        launch {
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
```

[Решение](app/src/test/java/com/offerfactory/video3/WBTests.kt)

---

## Задача №2
Сделать код-ревью и провести рефакторинг по необходимости:
```kotlin
class Repository(private val retrofitApi: RetrofitApi) {
    private var cache: String? = null

    suspend fun apiCallOrCache(): String {
        if (cache != null) {
            return cache
        }

        val apiResponse = retrofitApi.apiCall() // very long call
        cache = apiResponse
        return apiResponse
    }
}
```

[Решение](app/src/test/java/com/offerfactory/video3/WBTests.kt)

---

## Задача №3
есть TextView. Требуется написать таймер — в TextView будет раз в секунду меняться текст от 5 до 1. При достижении нуля открыть SecondActivity
```kotlin
class CountdownActivity : AppCompatActivity() {

    private val countdownViewModel: CountdownViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        val textView = findViewById<TextView>(R.id.textview)
    }

    private fun launchSecondActivity() {
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
        finish()
    }
}
```

[Решение](app/src/main/java/com/offerfactory/video3/MainActivity.kt)
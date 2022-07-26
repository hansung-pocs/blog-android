
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PostApiTest {

    private val api = with(NetworkModule()) {
        providePostApiService(provideRetrofit(provideHttpClient()))
    }

    @Test
    fun getAllWorksSuccessfully() {
        runBlocking {
            val response = api.getAll()
            assertEquals(200, response.status)
        }
    }

    @Test
    fun getDetailWorksSuccessfully() {
        runBlocking {
            val response = api.getDetail(1)
            assertEquals(200, response.status)
        }
    }
}
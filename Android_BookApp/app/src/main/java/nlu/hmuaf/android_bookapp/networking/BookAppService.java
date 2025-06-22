package nlu.hmuaf.android_bookapp.networking;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class BookAppService {
    private static final String BASE_URL = "http://192.168.10.164:8888/";
    private static Retrofit retrofit;

    // Khởi tạo OkHttpClient với timeout 30 giây
    private static OkHttpClient getDefaultClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    // Khởi tạo OkHttpClient với token và timeout 30 giây
    private static OkHttpClient getClientWithToken(String token) {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }

    // Khởi tạo Retrofit singleton với client custom
    private static Retrofit getRetrofitInstance(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    // Khởi tạo Retrofit với token
    public static BookAppApi getClient(String token) {
        OkHttpClient client = getClientWithToken(token);
        return getRetrofitInstance(client).create(BookAppApi.class);
    }

    // Khởi tạo Retrofit mặc định (không có token)
    public static BookAppApi getClient() {
        OkHttpClient client = getDefaultClient();
        return getRetrofitInstance(client).create(BookAppApi.class);
    }
}

package ru.quiz.vnikolaev.geoquiz.network;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.quiz.vnikolaev.geoquiz.Question;
import ru.quiz.vnikolaev.geoquiz.bis.QuestionExt;
import ru.quiz.vnikolaev.geoquiz.bis.Quiz;

/**
 * Created by User on 20.07.2016.
 */
public class QuizService {

    private static QuizService sQuizService;


    private String mUrl; // stub
    private HostListener mHostListener;
    private QuizServer mQuizServer;


    public static QuizService getInstance() {
        if (sQuizService == null) {
            sQuizService = new QuizService();
        }
        return sQuizService;
    }

    private QuizService() {
        mUrl = "";

        HostInterceptor interceptor = new HostInterceptor();
        mHostListener = interceptor;

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mQuizServer = retrofit.create(QuizServer.class);
    }

    public void setUrl(String url) {
        mHostListener.setHost(url);
    }

    public List<Quiz> downloadQuizzes() throws IOException {
        return mQuizServer.quizList().execute().body();
    }

    public List<QuestionExt> downloadQuiestions(long quizId, int number) throws IOException {
        return mQuizServer.questionList(quizId, number).execute().body();
    }

    public interface QuizServer {
        @GET("...")
        Call<List<Quiz>> quizList();
        @GET("...")
        Call<List<QuestionExt>> questionList(@Query("id") long id, @Query("num") int number);
    }

    public interface HostListener {
        void setHost(String url);
    }

    public class HostInterceptor implements Interceptor, HostListener {

        private volatile String host;

        @Override
        public void setHost(String host) {
            this.host = host;
        }

        @Override
        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            String host = this.host;
            if (host != null) {
                HttpUrl newUrl = request.url().newBuilder()
                        .host(host)
                        .build();
                request = request.newBuilder()
                        .url(newUrl)
                        .build();
            }
            return chain.proceed(request);
        }
    }

}

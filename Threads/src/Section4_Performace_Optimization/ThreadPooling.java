package Section4_Performace_Optimization;


/***
 * Thread pooling is nothing but re-using
 * a predefined pool of threads.
 * We don't kill threads but re-use the same threads
 * from pool.
 * Tasks are assigned to the pool using a task queue.
 * If threads are busy, we keep the tasks in the queue.
 ****
 * Note: implementing thread pooling is not trivial.
 * JDK comes with a few implementations of thread pools.
 * We will use the fixed thread pool executor.
 ****
 * Fixed thread pool executor
 * It has fixed 4 number of threads and comes with a task queue
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * In this part I will build a HTTP server
 * to demonstrate the increasing in 'Throughput'
 * on using thread pool.
 */
public class ThreadPooling {

    private static final String PATH_TO_BOOK = "./resources/war_and_peace.txt";
    private static final int NUMBER_OR_THREADS = 1;
    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(PATH_TO_BOOK)));
        startServer(text);
    }

    public static void startServer(String text) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/search", new WordCountHandler(text));

        Executor executor = Executors.newFixedThreadPool(NUMBER_OR_THREADS);
        server.setExecutor(executor);
        server.start();
    }

    public static class WordCountHandler implements HttpHandler{

        private String text;
        private Map<String, Long> wordCountMap = new HashMap<>();


        public WordCountHandler(String text){
            this.text = text;
            createWordCountMap();
        }

        private void createWordCountMap(){
            String [] allWords = this.text
                    .replaceAll("\r"," ")
                    .replaceAll("\n", " ")
                    .replaceAll("\t", " ")
                    .split(" ");

            for(String word: allWords){
                if(word == "")
                    continue;
                if(this.wordCountMap.containsKey(word))
                {
                    this.wordCountMap.put(word, this.wordCountMap.get(word) + 1L);
                }
                else this.wordCountMap.put(word, 1L);
            }
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String query = httpExchange.getRequestURI().getQuery();
            String [] keyValue = query.split("=");
            String action = keyValue[0];
            String word = keyValue[1];
            if(!action.equals("word")){
                httpExchange.sendResponseHeaders(400, 0);
                return;
            }

            long count = countWord(word);

            byte [] response = Long.toString(count).getBytes();
            httpExchange.sendResponseHeaders(200, response.length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }

        private long countWord(String word){
            long count = 0;
            int index = 0;
            while (index >= 0){
                index = text.indexOf(word, index);

                if(index >= 0)
                {
                    count++;
                    index++;
                }
            }
            // return count; // Approach 2

            return this.wordCountMap.containsKey(word) ? this.wordCountMap.get(word) : 0;
        }
    }
}

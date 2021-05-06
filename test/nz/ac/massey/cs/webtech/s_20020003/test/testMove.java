/*
 * Copyright (C) 2021 Jay
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nz.ac.massey.cs.webtech.s_20020003.test;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Jay
 */
public class testMove {

    private static final String server_url = "http://localhost:8080/assignment2_server_20020003";

    public testMove() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    // Test when user hits
    @Test
    public void testHit() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();

        String[] uris = {"/jack/start", "/jack/move/hit"};

        for (String s : uris) {
            HttpPost post = new HttpPost(server_url + s);
            CloseableHttpResponse response = client.execute(post);
        }

    }

}

//        final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
//                .setSoTimeout(Timeout.ofSeconds(5))
//                .build();
//        
//        final CloseableHttpAsyncClient client = HttpAsyncClients.custom().setIOReactorConfig(ioReactorConfig).build();
//        
//        client.start();
//        
//        HttpHost target = new HttpHost(server_url);
//        String[] requestUris = new String[] {"/jack/start", "/jack/move/hit"};
//        
//        for (String requestUri: requestUris) {
//            SimpleHttpRequest httppost = SimpleHttpRequests.post(target, requestUri);
//            Future<SimpleHttpResponse> future = client.execute(
//                    httppost, new FutureCallback<SimpleHttpResponse>(){
//                        
//                        @Override
//                        public void completed(final SimpleHttpResponse response) {
//                            System.out.println(requestUri + "->" + response.getCode());
//                            System.out.println(response.getBody());
//                        }
//
//                        @Override
//                        public void failed(final Exception ex) {
//                            System.out.println(requestUri + "->" + ex);
//                        }
//
//                        @Override
//                        public void cancelled() {
//                            System.out.println(requestUri + " cancelled");
//                        }
//
//                        
//                    });
//            future.get();
//        }
//        
//        client.close(CloseMode.GRACEFUL);      
//    }
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost post1 = new HttpPost(server_url + "/hit");
//        CloseableHttpResponse response1 = client.execute(post1);
//        String hit1 = getResponseBody(response1.getEntity().getContent());
//        
//        HttpPost post2 = new HttpPost(server_url + "/hit");
//        CloseableHttpResponse response2 = client.execute(post2);
//        String hit2 = getResponseBody(response1.getEntity().getContent());
//        
//        assertNotEquals(hit1, hit2);
// test stand complete server's hand
// test 400 bad request if not user's turn or user already bust
// test 404 not found if no active game
// Following code by slal
//    private static String getResponseBody(final InputStream stream) throws UnsupportedOperationException, IOException {
//
//    StringBuilder builder = new StringBuilder();
//
//    BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
//    String line = "";
//    while ((line = rd.readLine()) != null) {
//        builder.append(line);
//    }
//    return builder.toString();
//    }


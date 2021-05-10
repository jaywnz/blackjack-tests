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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jay
 */
public class testWon {

    private static final String server_url = "http://localhost:8080/assignment2_server_20020003";

    public testWon() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // test winner is none on user's turn -- POST
    @Test
    public void testWinnerNone() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();

        String[] uris = {"/jack/start", "/jack/won"};
        List<String> content = new ArrayList<>();

        for (String s : uris) {
            HttpPost post = new HttpPost(server_url + s);
            CloseableHttpResponse response = client.execute(post);
            content.add(getResponseBody(response.getEntity().getContent()));
        }
        assertTrue(content.get(1).contains("none"));
    }

    // test winner when user attempts to lose -- POST
    @Test
    public void testWinnerComputer() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post1 = new HttpPost(server_url + "/jack/start");
        client.execute(post1);

        // blind hit 6x; servlet forces stand when hit makes user bust
        for (int j = 0; j < 6; j++) {
            HttpPost post2 = new HttpPost(server_url + "/jack/move/hit");
            CloseableHttpResponse response2 = client.execute(post2);
            if (response2.getCode() == HttpStatus.SC_BAD_REQUEST) {
                break;
            }
            TimeUnit.SECONDS.sleep(1);
        }

        HttpPost post3 = new HttpPost(server_url + "/jack/won");
        CloseableHttpResponse response4 = client.execute(post3);
        String content = getResponseBody(response4.getEntity().getContent());

        assertTrue(content.contains("computer"));
    }

    // test 404 Not Found when no active game on won -- POST
    @Test
    public void testInactiveWon() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(server_url + "/jack/won");
        CloseableHttpResponse response = client.execute(post);
        if (response.getCode() == HttpStatus.SC_NOT_FOUND) {
        } else {
            throw new Exception("Issue getting a 404 response from won.");
        }
    }

    // getResponseBody() Author: slal
    private static String getResponseBody(final InputStream stream) throws UnsupportedOperationException, IOException {

        StringBuilder builder = new StringBuilder();

        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        String line = "";
        while ((line = rd.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }
}

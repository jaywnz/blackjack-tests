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

    // test winner is none on user's turn
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

    // test proportion of wins when user tries to lose -- POST
    @Test
    public void testWinChance() throws Exception {
        // FIXME -- hangs on subsequent games
        int userWins = 0;
        int dealerWins = 0;

        CloseableHttpClient client = HttpClients.createDefault();

        // play 20 games
        for (int i = 0; i < 10; i++) {
            HttpPost post1 = new HttpPost(server_url + "/jack/start");
            client.execute(post1);
            // blind hit 10x, servlet forces stand when hit makes user bust
            for (int j = 0; j < 10; j++) {
                HttpPost post2 = new HttpPost(server_url + "/jack/move/hit");
                CloseableHttpResponse response2 = client.execute(post2);
                if (response2.getCode() == HttpStatus.SC_BAD_REQUEST) {
                    break;
                }
            }
            HttpPost post4 = new HttpPost(server_url + "/jack/won");
            CloseableHttpResponse response = client.execute(post4);
            String content = getResponseBody(response.getEntity().getContent());

            if ("user".equals(content)) {
                userWins++;
            } else if ("computer".equals(content)) {
                dealerWins++;
            }
        }
        
        // calculate win proportion
        double prop = dealerWins / userWins;
        if (prop < 0.5) {
        } else {
            throw new Exception("Dealer won more than 5% of games.");
        }
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

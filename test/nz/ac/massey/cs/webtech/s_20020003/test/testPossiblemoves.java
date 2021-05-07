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
import org.apache.hc.client5.http.classic.methods.HttpGet;
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
public class testPossiblemoves {

    private static final String server_url = "http://localhost:8080/assignment2_server_20020003";

    public testPossiblemoves() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // test for hit and stand when < 21 -- GET
    @Test
    public void testHitStandMoves() throws Exception {
        // start new game
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(server_url + "/jack/start");
        client.execute(post);
        // should always show hit and stand
        HttpGet get = new HttpGet(server_url + "/jack/possiblemoves");
        CloseableHttpResponse response = client.execute(get);
        String content = getResponseBody(response.getEntity().getContent());

        assertTrue(content.contains("stand") && content.contains("hit"));
    }

    // test for stand when > 21 -- GET
    @Test
    public void testStandMove() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        // hits twice; rerun test if user bust before second hit
        String[] uris = {"/jack/start", "/jack/move/hit", "/jack/move/hit"};

        for (String s : uris) {
            HttpPost post = new HttpPost(server_url + s);
            CloseableHttpResponse response = client.execute(post);
            if (response.getCode() == HttpStatus.SC_BAD_REQUEST) {
                throw new Exception("400 Bad Request: User already bust. Run test again.");
            }
        }
        HttpGet get = new HttpGet(server_url + "/jack/possiblemoves");
        CloseableHttpResponse response = client.execute(get);
        String content = getResponseBody(response.getEntity().getContent());

        assertFalse(content.contains("hit"));
    }

    // test 404 Not Found when no active game on possiblemoves -- GET
    @Test
    public void testInactivePossibleMoves() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(server_url + "/jack/possiblemoves");
        CloseableHttpResponse response = client.execute(get);
        if (response.getCode() == HttpStatus.SC_NOT_FOUND) {
        } else {
            throw new Exception("Issue getting a 404 response from possiblemoves.");
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

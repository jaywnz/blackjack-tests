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
import static org.junit.Assert.*;
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

    // test when user hits and 400 Bad Request when bust -- POST
    @Test
    public void testHit() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();

        String[] uris = {"/jack/start", "/jack/move/hit", "/jack/move/hit"};
        List<String> content = new ArrayList<>();

        for (String s : uris) {
            HttpPost post = new HttpPost(server_url + s);
            CloseableHttpResponse response = client.execute(post);
            if (response.getCode() == HttpStatus.SC_BAD_REQUEST) {
                throw new Exception("400 Bad Request: User already bust. Run test again.");
            }
            content.add(getResponseBody(response.getEntity().getContent()));
        }

        // if second output is longer, then card has been added to user's hand
        assertTrue(content.get(2).length() > content.get(1).length());
    }

    // test 404 Not Found when no active game on hit -- POST
    @Test
    public void testInactiveHit() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(server_url + "/jack/move/hit");
        CloseableHttpResponse response = client.execute(post);
        if (response.getCode() == HttpStatus.SC_NOT_FOUND) {
        } else {
            throw new Exception("Issue getting a 404 response from hit.");
        }
    }

    // test stand completes dealer's hand -- POST
    @Test
    public void testStand() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();

        String[] uris = {"/jack/start", "/jack/move/stand"};
        List<String> content = new ArrayList<>();

        for (String s : uris) {
            HttpPost post = new HttpPost(server_url + s);
            CloseableHttpResponse response = client.execute(post);
            content.add(getResponseBody(response.getEntity().getContent()));
        }

        assertTrue(content.get(1).contains("dealerHand"));
    }

    // test 404 Not Found when no active game on stand -- POST
    @Test
    public void testInactiveStand() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(server_url + "/jack/move/stand");
        CloseableHttpResponse response = client.execute(post);
        if (response.getCode() == HttpStatus.SC_NOT_FOUND) {
        } else {
            throw new Exception("Issue getting a 404 response from stand.");
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

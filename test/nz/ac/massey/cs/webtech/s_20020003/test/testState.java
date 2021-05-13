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
import java.util.Arrays;
import java.util.List;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jay
 */
public class testState {

    private static final String server_url = "http://localhost:8080/assignment2_server_20020003";

    public testState() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // test user and dealer hands are given -- GET
    @Test
    public void testStateHands() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();

        String[] uris = {"/jack/start", "/jack/state"};
        List<String> content = new ArrayList<>();

        for (String s : uris) {
            HttpGet get = new HttpGet(server_url + s);
            CloseableHttpResponse response = client.execute(get);
            content.add(getResponseBody(response.getEntity().getContent()));
        }
        assertTrue(content.get(1).contains("userHand")
                && content.get(1).contains("dealerHand"));
    }

    // test response is JSON MIME type -- GET
    @Test
    public void testStateContentType() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get1 = new HttpGet(server_url + "/jack/start");
        client.execute(get1);

        HttpGet get2 = new HttpGet(server_url + "/jack/state");
        CloseableHttpResponse response = client.execute(get2);

        Header[] mime = response.getHeaders("Content-Type");
        String content = Arrays.toString(mime);

        assertTrue(content.contains("application/json"));
    }

    // test 404 Not Found when no active game on state -- GET
    @Test
    public void testInactiveState() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet get2 = new HttpGet(server_url + "/jack/state");
        CloseableHttpResponse response = client.execute(get2);
        if (response.getCode() == HttpStatus.SC_NOT_FOUND) {
        } else {
            throw new Exception("Issue getting a 404 response from state.");
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

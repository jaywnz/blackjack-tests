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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jay
 */
public class testStats {

    private static final String server_url = "http://localhost:8080/assignment2_server_20020003";

    public testStats() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // test stats are displayed -- GET
    @Test
    public void testStatsDisplay() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        String[] uris = {"/jack/start", "/jack/move/stand", "/jack/won"};

        // complete a game to generate stats
        for (String s : uris) {
            HttpPost post = new HttpPost(server_url + s);
            client.execute(post);
        }

        HttpGet get = new HttpGet(server_url + "/jack/stats");
        CloseableHttpResponse response = client.execute(get);
        String body = getResponseBody(response.getEntity().getContent());

        assertTrue(body.contains("Total games played")
                && body.contains("Total games won")
                && body.contains("Win percentage"));
    }

    // test error displayed if no games played and text/plain encoding -- GET
    @Test
    public void testNoStatsAvailable() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(server_url + "/jack/start");
        client.execute(post);

        HttpGet get = new HttpGet(server_url + "/jack/stats");
        CloseableHttpResponse response = client.execute(get);
        String body = getResponseBody(response.getEntity().getContent());

        assertTrue(body.contains("No games played this session.")
                && (response.getHeader("Content-Type").toString()).contains("text/plain"));
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

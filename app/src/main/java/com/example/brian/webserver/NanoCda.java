package com.example.brian.webserver;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by brian on 25/11/2017.
 */

public class NanoCda extends NanoHTTPD {
    public NanoCda(int port) {
        super(port);
    }

    public NanoCda(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("username") == null) {
            msg += "<form action='?' method='get'>\n";
            msg += "<p>Your name: <input type='text' name='username'></p>\n";
            msg += "</form>\n";
        } else {
            msg += "<p>Hello, " + parms.get("username") + "!</p>";
        }
        return newFixedLengthResponse( msg + "</body></html>\n" );
    }
}

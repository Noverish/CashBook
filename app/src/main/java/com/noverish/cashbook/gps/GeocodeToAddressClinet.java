package com.noverish.cashbook.gps;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Noverish on 2016-08-13.
 */
public class GeocodeToAddressClinet {
    private final String API_KEY = "efee521a13262c009d3a794e4d532461";
    private final String API_URL = "https://apis.daum.net/local/geo/coord2detailaddr";

    private static GeocodeToAddressClinet instance;
    public static GeocodeToAddressClinet getInstance() {
        if(instance == null)
            instance = new GeocodeToAddressClinet();

        return instance;
    }

    private GeocodeToAddressClinet() {}

    public Address geoCodeToAddress(double latitude, double longitude) throws ConvertException{
        String urlStr = API_URL;
        urlStr += "?apikey=" + API_KEY;
        urlStr += "&x=" + longitude;
        urlStr += "&y=" + latitude;
        urlStr += "&output=json";
        urlStr += "&inputCoordSystem=WGS84";

        ConnectThread thread = new ConnectThread(urlStr);

        JSONObject result = thread.getResult();

        try {
            String newAddress = result.getJSONObject("new").getString("name");
            String oldAddress = result.getJSONObject("old").getString("name");
            double longitudeJSON = result.getDouble("x");
            double latitudeJSON = result.getDouble("y");

            return new Address(newAddress, oldAddress, longitudeJSON, latitudeJSON);
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new ConvertException(result.toString());
        }
    }


    private class ConnectThread extends Thread {
        private JSONObject result;
        private String urlStr = "";

        private ConnectThread(String urlStr) {
            this.urlStr = urlStr;

            start();
        }

        @Override
        public void run() {
            try {
                URL url = new URL(urlStr);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    inputLine = in.readLine();
                    while (inputLine != null) {
                        response.append(inputLine);
                        inputLine = in.readLine();
                    }
                    in.close();

                    result = new JSONObject(response.toString());
                } else {
                    Log.e("ERROR","http code : " + responseCode);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                result = null;
            }
        }

        public JSONObject getResult() {
            try {
                join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return result;
        }
    }

    public class Address {
        private String newAddress;
        private String oldAddress;
        private double longitude;
        private double latitude;

        private Address(String newAddress, String oldAddress, double longitude, double latitude) {
            this.newAddress = newAddress;
            this.oldAddress = oldAddress;
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public String getNewAddress() {
            return newAddress;
        }

        public String getOldAddress() {
            return oldAddress;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "newAddress='" + newAddress + '\'' +
                    ", oldAddress='" + oldAddress + '\'' +
                    ", longitude=" + longitude +
                    ", latitude=" + latitude +
                    '}';
        }
    }

    public class ConvertException extends Exception {
        public String json = "EMPTY";

        private ConvertException() {}

        private ConvertException(String json) {
            this.json = json;
        }
    }
}

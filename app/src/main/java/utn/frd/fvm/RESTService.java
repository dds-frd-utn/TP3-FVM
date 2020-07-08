package utn.frd.fvm;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class RESTService {

    public static String apiUrl() {
        return "http://192.168.100.11:8080/TP1-FVM/";
    }

    public static String restCall(String restUrl, String method, JSONObject params) {
        String result = "";
        URL url;
        HttpURLConnection con = null;
        try {
            url = new URL(restUrl);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setFixedLengthStreamingMode(params.toString().getBytes().length);
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            con.connect();

            OutputStream os = new BufferedOutputStream(con.getOutputStream());
            os.write(params.toString().getBytes());
            os.flush();

            InputStream inputStream = con.getInputStream();
            BufferedReader bReader = new BufferedReader(
                    new InputStreamReader(inputStream, "utf-8"),8);
            StringBuilder sBuilder = new StringBuilder();
            String line = null;
            while((line = bReader.readLine()) != null) {
                sBuilder.append(line + " \n ");
            }
            inputStream.close();
            result = sBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        } finally {
            if(con != null) {
                con.disconnect();
            }
        }

        return result;
    }

    public static String makeGetRequest(String restURL) {
        String result  = "";
        URL url;
        HttpURLConnection con = null;
        try {
            url = new URL(restURL);
            con = (HttpURLConnection) url.openConnection();
            InputStream inputStream = con.getInputStream();

            BufferedReader bReader = new BufferedReader(
                    new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder sBuilder = new StringBuilder();

            String line = null;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }
            inputStream.close();

            result = sBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return result;
    }

}

package me.shinsunyoung.springbootdeveloper.dto.SecurityFacilityApiCaller;

import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecurityFacilityApiCaller {

    public static void main(String[] args) {
        try {
            String serviceKey = "OJNIDPWM-OJNI-OJNI-OJNI-OJNIDPWMX8";
            int page = 1;
            int numOfRows = 100;
            int totalCount = 0;

            do {
                String urlStr = "http://safemap.go.kr/openApiService/data/getSecurityFacilityData.do"
                        + "?serviceKey=" + serviceKey
                        + "&pageNo=" + page
                        + "&numOfRows=" + numOfRows
                        + "&type=xml";

                URL url = new URL(urlStr);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/xml");

                BufferedReader br;
                if (con.getResponseCode() >= 200 && con.getResponseCode() <= 300) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                con.disconnect();

                // XML → JSON 변환
                JSONObject jsonObject = XML.toJSONObject(response.toString());
                JSONObject body = jsonObject.getJSONObject("response").getJSONObject("body");

                if (totalCount == 0) {
                    totalCount = body.getInt("totalCount");
                    System.out.println("전체 데이터 수: " + totalCount);
                }

                System.out.println("Page " + page + " 결과:\n");
                System.out.println(body.toString(2)); // 보기좋게 출력

                page++;
            } while ((page - 1) * numOfRows < totalCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

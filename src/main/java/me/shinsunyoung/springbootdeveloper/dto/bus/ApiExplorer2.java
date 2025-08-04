package me.shinsunyoung.springbootdeveloper.dto.bus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.shinsunyoung.springbootdeveloper.dto.bus.dto.BusStopDTO;
import me.shinsunyoung.springbootdeveloper.dto.bus.dto.RootResponse;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class ApiExplorer2 {
    public static void main(String[] args) {
        try {
            String serviceKey = "+M0Mc+8bm5NyWIL4Em4cylHuiTq3Vzx/N+oiN1m72FHdV1pvpe6waN+Iy7y8M1R87QBUGWyzsDyhRRyag1Jljw==";
            int page = 1;
            int totalCount = 0;
            int rowsPerPage = 10;

            do {
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6260000/BusanBIMS/busStopList");
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(page), "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(rowsPerPage), "UTF-8"));

                // 'resultType'은 이 API에서 지원 안 함 (그래서 xml로 옴)

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/xml"); // 응답이 xml이기 때문

                BufferedReader rd;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }

                rd.close();
                conn.disconnect();

                // ⛔ XML → ✅ JSON 변환
                JSONObject jsonObj = XML.toJSONObject(sb.toString());
                String jsonString = jsonObj.toString();  // JSON 문자열

                // JSON → DTO 매핑
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                RootResponse root = objectMapper.readValue(jsonString, RootResponse.class);

                if (totalCount == 0) {
                    totalCount = root.getResponse().getBody().getTotalCount();
                }

                List<BusStopDTO> list = root.getResponse().getBody().getItems().getItem();
                for (BusStopDTO dto : list) {
                    System.out.println("▶ 정류소ID: " + dto.getBstopid());
                    System.out.println("   정류소명: " + dto.getBstopnm());
                    System.out.println("   정류소번호: " + dto.getArsno());
                    System.out.println("   GPS X: " + dto.getGpsx());
                    System.out.println("   GPS Y: " + dto.getGpsy());
                    System.out.println("   정류소타입: " + dto.getStoptype());
                    System.out.println("----------------------------------");
                }

                page++;
            } while ((page - 1) * rowsPerPage < totalCount);

        } catch (Exception e) {
            System.out.println("❌ 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

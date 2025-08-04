package me.shinsunyoung.springbootdeveloper.dto;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class ApiExplorer {
    public static void main(String[] args) {
        try {
            String serviceKey = "+M0Mc+8bm5NyWIL4Em4cylHuiTq3Vzx/N+oiN1m72FHdV1pvpe6waN+Iy7y8M1R87QBUGWyzsDyhRRyag1Jljw=="; // 디코딩되지 않은 원래 키 사용 (예: ABCDE... 형식)
            int page = 1;
            int totalCount = 0;

            do {
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6260000/MedicInstitService/MedicalInstitInfo");
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(page), "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); // 최대 100
                urlBuilder.append("&" + URLEncoder.encode("resultType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

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

                // JSON → 객체 변환
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 알 수 없는 필드 무시

                RootResponse root = objectMapper.readValue(sb.toString(), RootResponse.class);

                if (totalCount == 0) {
                    totalCount = Integer.parseInt(root.getResponse().getBody().getTotalCount());
                }

                List<MedicalInstitutionDTO> list = root.getResponse().getBody().getItems().getItem();
                for (MedicalInstitutionDTO dto : list) {
                    System.out.println("▶ 병원명: " + dto.getInstit_nm());
                    System.out.println("   주소: " + dto.getStreet_nm_addr());
                    /*System.out.println("   전화번호: " + dto.getTel());
                    System.out.println("   진료과목: " + dto.getExam_part());
                    System.out.println("   월요일: " + dto.getMonday());
                    System.out.println("   화요일: " + dto.getTuesday());
                    System.out.println("   수요일: " + dto.getWednesday());
                    System.out.println("   목요일: " + dto.getThursday());
                    System.out.println("   금요일: " + dto.getFriday());
                    System.out.println("   토요일: " + dto.getSaturday());
                    System.out.println("   일요일: " + dto.getSunday());
                    System.out.println("   공휴일: " + dto.getHoliday());*/
                    System.out.println("   위도: " + dto.getLat() + " / 경도: " + dto.getLng());
                    System.out.println("----------------------------------");
                }

                page++;
            } while ((page - 1) * 100 < totalCount);  // 다음 페이지 있으면 반복

        } catch (Exception e) {
            System.out.println("❌ 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

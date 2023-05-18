package com.example.beeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
class CityGroup {
    private String country;
    private List<String> cities;
}

@Data
@AllArgsConstructor
class CityCount {
    private String country;
    private int citiesCount;
}

public class Main {
    private static final String GET_URL = "https://procodeday-01.herokuapp.com/meet-up/get-country-list";
    private static final String POST_URL = "https://procodeday-01.herokuapp.com/meet-up/post-request";

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        Request getRequest = new Request.Builder()
                .url(GET_URL)
                .build();

        try {
            Response getResponse = client.newCall(getRequest).execute();
            if (getResponse.isSuccessful() && getResponse.body() != null) {
                String responseBody = getResponse.body().string();

                ObjectMapper objectMapper = new ObjectMapper();
                List<City> cities = objectMapper.readValue(responseBody, objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, City.class));

                List<CityGroup> cityGroups = cities.stream()
                        .collect(Collectors.groupingBy(City::getCountry))
                        .entrySet()
                        .stream()
                        .map(entry -> new CityGroup(entry.getKey(), entry.getValue().stream()
                                .map(City::getCity)
                                .sorted()
                                .collect(Collectors.toList())))
                        .collect(Collectors.toList());

                List<CityCount> cityCounts = cityGroups.stream()
                        .map(cityGroup -> new CityCount(cityGroup.getCountry(), cityGroup.getCities().size()))
                        .collect(Collectors.toList());

                System.out.println(cityGroups);
                System.out.println(cityCounts);

                Student student = new Student();
                student.setName("Арсен Бекбоев");
                student.setPhone("+996 504 77 88 57");
                student.setGithubUrl("https://github.com/arsproger/beeline");

                Result result = new Result(cityCounts);

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                        objectMapper.writeValueAsString(new PostRequest(student, result)));

                Request postRequest = new Request.Builder()
                        .url(POST_URL)
                        .post(requestBody)
                        .build();

                Response postResponse = client.newCall(postRequest).execute();
//                if (postResponse.isSuccessful()) {
//                    System.out.println("POST request successful");
//                } else {
//                    System.out.println("Error: " + postResponse.code());
//                }
            } else {
                System.out.println("Error: " + getResponse.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
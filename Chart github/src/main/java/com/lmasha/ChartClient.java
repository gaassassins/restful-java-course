package com.lmasha;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by Vadim on 2017-03-01.
 */
public class ChartClient extends Application {

    private HttpClient client = HttpClientBuilder.create().build();
    private final int WIDTH = 800;
    private final String HOST = "localhost";
    private final String PORT = "8080";
    private final String REST_URL = "/repo";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Currency chart");
        LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());
        ChoiceBox<String> cb = new ChoiceBox<>(FXCollections.observableArrayList(getCurrencyList()));
        Button showChart = new Button("Show Chart");
        showChart.setOnAction(e -> {
            if (cb.getValue() != null) {
                updateChart(lineChart, cb.getValue());
            }
        });
        stage.setScene(new Scene(new VBox(new HBox(cb, showChart), lineChart)));
        stage.setWidth(WIDTH);
        stage.show();
    }

    private Set<String> getCurrencyList() {
        Set<String> set = new HashSet<>();
        HttpGet request = new HttpGet("http://localhost:8080/repos?user=nikitabelonogov" );
        String jsonResult = "[]";
        try {
            HttpResponse response = client.execute(request);
            jsonResult = (EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (IOException e) {
            jsonResult = "[]";
        }
        JSONArray jsonObjectResult = new JSONArray(jsonResult);
        for (int i = 0; i < jsonObjectResult.length(); i++) {
            JSONObject currentCurrency = jsonObjectResult.getJSONObject(i);
            set.add(currentCurrency.getString("name"));
        }
        return set;
    }

    private void updateChart(LineChart<String, Number> lineChart, String repo) {

        lineChart.getData().clear();
        Map<String, Double> channelsChartData = getChartData(repo);
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (Map.Entry<String, Double> entry : channelsChartData.entrySet()) {
            if (entry.getValue() > max) {
                max = (int) entry.getValue().doubleValue();
            }
            if (entry.getValue() < min) {
                min = (int) entry.getValue().doubleValue();
            }
        }
        NumberAxis s = (NumberAxis) lineChart.getYAxis();
        s.setAutoRanging(false);
        s.setLowerBound(min - 5);
        s.setUpperBound(max + 5);
        s.setTickUnit(1);

        lineChart.setCreateSymbols(false);
        lineChart.setTitle(repo + " chart");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(repo);
        channelsChartData.forEach((k, v) -> series.getData().add(new XYChart.Data<>(k, v)));
        lineChart.getData().add(series);
    }

    private Map<String, Double> getChartData(String repo) {
        Map<String,Double> chartData = new TreeMap<>();
        HttpGet request = new HttpGet("http://localhost:8080/commits?user=nikitabelonogov&repo" + repo);
        String jsonResult = "[]";
        try {
            HttpResponse response = client.execute(request);
            jsonResult = (EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (IOException e) {
            jsonResult = "[]";
        }
        JSONArray jsonObjectResult = new JSONArray(jsonResult);
        for (int i = 0; i < jsonObjectResult.length(); i++) {
            JSONObject currentRepoInfo = jsonObjectResult.getJSONObject(i);
            String dateKey = currentRepoInfo.getJSONObject("commit").getJSONObject("author").getString("date");
            dateKey = dateKey.substring(0, 10);
            chartData.put(dateKey, chartData.getOrDefault(dateKey, 0.0) + 1.0);
        }
        return chartData;
    }
}

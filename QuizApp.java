import java.net.*;
import java.io.*;
import java.util.*;
import org.json.*;

public class QuizApp {

    static String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";

    public static void main(String[] args) throws Exception {

        String regNo = "2024CS101"; // 🔥 CHANGE THIS

        Set<String> unique = new HashSet<>();
        Map<String, Integer> scores = new HashMap<>();

        for (int i = 0; i < 10; i++) {

            System.out.println("Polling: " + i);

            String urlStr = BASE_URL + "/quiz/messages?regNo=" + regNo + "&poll=" + i;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            JSONObject obj = new JSONObject(response.toString());
            JSONArray events = obj.getJSONArray("events");

            for (int j = 0; j < events.length(); j++) {
                JSONObject event = events.getJSONObject(j);

                String round = event.getString("roundId");
                String participant = event.getString("participant");
                int score = event.getInt("score");

                String key = round + "_" + participant;

                if (unique.add(key)) {
                    scores.put(participant, scores.getOrDefault(participant, 0) + score);
                }
            }

            Thread.sleep(5000);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(scores.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());

        int total = 0;

        for (Map.Entry<String, Integer> e : list) {
            System.out.println(e.getKey() + " : " + e.getValue());
            total += e.getValue();
        }

        System.out.println("Total Score: " + total);

        // ✅ CALL METHOD HERE
        submitLeaderboard(regNo, list);
    }

    // ✅ METHOD OUTSIDE MAIN
    public static void submitLeaderboard(String regNo, List<Map.Entry<String, Integer>> list) throws Exception {

        URL url = new URL(BASE_URL + "/quiz/submit");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject body = new JSONObject();
        body.put("regNo", regNo);

        JSONArray leaderboard = new JSONArray();

        for (Map.Entry<String, Integer> e : list) {
            JSONObject obj = new JSONObject();
            obj.put("participant", e.getKey());
            obj.put("totalScore", e.getValue());
            leaderboard.put(obj);
        }

        body.put("leaderboard", leaderboard);

        OutputStream os = conn.getOutputStream();
        os.write(body.toString().getBytes());
        os.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;

        System.out.println("\nSubmission Response:");
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleGoldBot {

    public static void main(String[] args) {
        try {
            // 1. define the API URL (Using Twelve Data for XAU/USD)
            // NOTE: You must replace 'apiKey' with your own free API key from twelvedata.com if this stops working.
            String apiKey = "your_api_key"; 
            String urlString = "https://api.twelvedata.com/price?symbol=XAU/USD&apikey=" + apiKey;

            // 2. Create the connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 3. Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // 4. "Poor man's" JSON parsing (Extracting the number without a library)
            // The data comes back looking like: {"price":"2034.50", ...}
            String jsonResponse = content.toString();
            
            // Find where the price value starts and ends
            String searchStr = "\"price\":\"";
            int startIndex = jsonResponse.indexOf(searchStr);
            
            if (startIndex != -1) {
                startIndex += searchStr.length();
                int endIndex = jsonResponse.indexOf("\"", startIndex);
                String price = jsonResponse.substring(startIndex, endIndex);

                // 5. Display the result
                System.out.println("-------------------------");
                System.out.println("GOLD PRICE (XAU/USD): $" + price);
                System.out.println("-------------------------");
            } else {
                System.out.println("Could not find price in response: " + jsonResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

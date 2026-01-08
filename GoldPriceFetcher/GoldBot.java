import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleGoldBot {
    // Custom values for calculation
    private static final double CUSTOM_USD_TO_INR = 83.50; // Current approx rate
    private static final double GRAMS_IN_OUNCE = 31.1035;
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

                // 1. Get the price from your fetch method
            String priceString = fetchPrice("XAU/USD");
            double priceInUSD = Double.parseDouble(priceString);

    // 2. Custom Calculation for 10 grams in Rupees
    // (USD Price / 31.1035) = Price per 1 gram USD
    // (Price per gram * 10) = Price per 10 grams USD
    // (Price per 10g * exchange rate) = Price in INR
            double priceInINR = (priceInUSD / GRAMS_IN_OUNCE) * 10 * CUSTOM_USD_TO_INR;

    // 3. Print the result
            System.out.println("Live Gold Price (USD): $" + priceInUSD);
            System.out.println("Using Custom Exchange Rate: ₹" + CUSTOM_USD_TO_INR);
            System.out.println("------------------------------------");
            System.out.printf("GOLD PRICE IN INDIA: ₹%.2f per 10g%n", priceInINR);
                    } else {
                        System.out.println("Could not find price in response: " + jsonResponse);
                    }    

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

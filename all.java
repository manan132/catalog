import java.math.BigInteger;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;
import java.io.InputStream;

public class SecretSharing {

    // Method to decode a number from a specific base to base 10
    public static BigInteger decode(String value, int base) {
        return new BigInteger(value, base);
    }

    // Method to compute Lagrange Interpolation to find the constant term 'c'
    public static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points, int k) {
        BigInteger result = BigInteger.ZERO;

        List<Map.Entry<Integer, BigInteger>> pointList = new ArrayList<>(points.entrySet());
        int size = pointList.size();

        for (int i = 0; i < size; i++) {
            Map.Entry<Integer, BigInteger> p1 = pointList.get(i);
            int x1 = p1.getKey();
            BigInteger y1 = p1.getValue();

            BigInteger term = y1;
            for (int j = 0; j < size; j++) {
                if (i == j) continue;
                Map.Entry<Integer, BigInteger> p2 = pointList.get(j);
                int x2 = p2.getKey();
                term = term.multiply(BigInteger.valueOf(-x2))
                           .divide(BigInteger.valueOf(x1 - x2));
            }
            result = result.add(term);
        }

        return result;
    }

    // Method to process a single JSON test case
    public static void processTestCase(JSONObject testCase) {
        JSONObject keys = testCase.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        Map<Integer, BigInteger> points = new HashMap<>();
        for (String key : testCase.keySet()) {
            if (key.equals("keys")) continue; // Skip the "keys" object
            JSONObject point = testCase.getJSONObject(key);
            int base = point.getInt("base");
            String value = point.getString("value");

            BigInteger y = decode(value, base);
            int x = Integer.parseInt(key);

            points.put(x, y);
        }

        // Find the constant term 'c'
        BigInteger constantTerm = lagrangeInterpolation(points, k);
        System.out.println("The constant term is: " + constantTerm);
    }

    public static void main(String[] args) {
        try {
            // Read input from the file (or other sources)
            InputStream inputStream = SecretSharing.class.getResourceAsStream("/testcase.json");
            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject json = new JSONObject(tokener);

            // Process each test case
            for (String key : json.keySet()) {
                if (key.equals("keys")) continue; // Skip the "keys" object
                JSONObject testCase = json.getJSONObject(key);
                System.out.println("Processing Test Case: " + key);
                processTestCase(testCase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package lib;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {
    public static String getRandomEmail() {
    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
    return "learnqa" + timestamp + "@example.com";
}

public static Map<String , String> getRegistrationData(){
        Map <String, String> dataDef = new HashMap<>();
        dataDef.put("email", DataGenerator.getRandomEmail());
        dataDef.put("password", "123");
        dataDef.put("username", "learnqa");
        dataDef.put("firstName", "learnqa");
        dataDef.put("lastName", "learnqa");

        return dataDef;
    } //Это если вызвать метод без параметров данных - вернутся дефолтные

    public static Map<String , String> getRegistrationData(Map <String, String> nonDefaultValues) {
        Map <String, String> defaultValues = DataGenerator.getRegistrationData();
        Map <String, String> data = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key: keys) {
            if (nonDefaultValues.containsKey(key)) {
                data.put(key, nonDefaultValues.get(key));
            } else {
                data.put(key, defaultValues.get(key)); //задать данные самому
            }
        }
        return data;
    }
}
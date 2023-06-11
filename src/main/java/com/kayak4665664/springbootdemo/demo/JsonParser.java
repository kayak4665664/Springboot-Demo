package com.kayak4665664.springbootdemo.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class JsonParser {

    /*
     * Get the size of the json file.
     */
    public static int getJsonSize() {
        int size = 0;
        try {
            InputStream in = JsonParser.class.getClassLoader()
                    .getResourceAsStream("static/gdp-per-capita-by-country.json");
            String jsonContent = IOUtils.toString(in, StandardCharsets.UTF_8);
            // Use fastjson to parse the json file.
            JSONArray jsonArray = JSON.parseArray(jsonContent);
            for (int i = 0; i < jsonArray.size(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Integer value = jsonObject.getInteger("value");
                if (value != null)
                    ++size;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    /*
     * Check if the left and right are valid.
     */
    public static boolean isValid(int left, int right) {
        int max_size = getJsonSize();
        if (max_size == 0)
            return false;
        else if (left <= right && left >= 1 && right <= max_size)
            return true;
        else if (left > right && left <= max_size && right >= 1)
            return true;
        else
            return false;
    }

    /*
     * The class to store the data.
     */
    public static class Data {
        private String[] categories;
        private int[] data;
        private List<Map.Entry<String, Integer>> list;

        public Data(String[] categories, int[] data, List<Map.Entry<String, Integer>> list) {
            this.categories = categories;
            this.data = data;
            this.list = list;
        }

        public String[] getCategories() {
            return categories;
        }

        public int[] getData() {
            return data;
        }

        public List<Map.Entry<String, Integer>> getList() {
            return list;
        }

        public void setCategories(String[] categories) {
            this.categories = categories;
        }

        public void setData(int[] data) {
            this.data = data;
        }

        public void setList(List<Map.Entry<String, Integer>> list) {
            this.list = list;
        }
    }

    /*
     * Get the data from the json file for Front-end.
     */
    public static Data getData(int left, int right, boolean isRandom) {
        String[] categories = new String[0];
        int[] data = new int[0];
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        Data result = new Data(categories, data, list);

        try {
            InputStream in = JsonParser.class.getClassLoader()
                    .getResourceAsStream("static/gdp-per-capita-by-country.json");
            String jsonContent = IOUtils.toString(in, StandardCharsets.UTF_8);
            // Use fastjson to parse the json file.
            JSONArray jsonArray = JSON.parseArray(jsonContent);
            // Use HashMap to store the data.
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < jsonArray.size(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                Integer value = jsonObject.getInteger("value");
                if (value != null)
                    map.put(name, value);
            }
            // Sort the data by value.
            list = new ArrayList<>(map.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                    if (left <= right)
                        return b.getValue().compareTo(a.getValue());
                    else
                        return a.getValue().compareTo(b.getValue());
                }
            });
            // Get the data from the list.
            int begin, end;
            if (isRandom) {
                int max_size = getJsonSize(), size = Math.min(max_size, 15);
                categories = new String[size];
                data = new int[size];
                Collections.shuffle(list);
                begin = 1;
                end = size;
            } else {
                categories = new String[Math.abs(right - left) + 1];
                data = new int[Math.abs(right - left) + 1];
                begin = Math.min(left, right);
                end = Math.max(left, right);

            }
            for (int i = begin; i <= end; ++i) {
                categories[i - begin] = list.get(i - 1).getKey();
                data[i - begin] = list.get(i - 1).getValue();
            }
            result.setCategories(categories);
            result.setData(data);
            result.setList(list);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }
}

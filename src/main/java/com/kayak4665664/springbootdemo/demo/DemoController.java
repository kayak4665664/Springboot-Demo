package com.kayak4665664.springbootdemo.demo;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kayak4665664.springbootdemo.demo.JsonParser.Data;

@Controller
public class DemoController {

    @GetMapping("/")
    public String home(@RequestParam(name = "page", required = false, defaultValue = "World Map") String page,
            @RequestParam(name = "refresh", required = false, defaultValue = "false") String refresh,
            @RequestParam(name = "left", required = false, defaultValue = "1") int left,
            @RequestParam(name = "right", required = false, defaultValue = "20") int right,
            @RequestParam(name = "filter", required = false, defaultValue = "false") String filter,
            @RequestParam(name = "randomise", required = false, defaultValue = "false") String randomise,
            Map<String, Object> model) {
        // World Map is the default page.
        if (refresh.equals("true"))
            page = "World Map";
        else if (filter.equals("true"))
            page = "Bar Chart";
        else if (randomise.equals("true"))
            page = "3D";
        model.put("page", page);
        // Bar Chart.
        if (page.equals("Bar Chart")) {
            String isValid = JsonParser.isValid(left, right) ? "true" : "false";
            model.put("isValid", isValid);
            if (isValid.equals("true")) {
                Data result = JsonParser.getData(left, right, false);
                int[] data = result.getData();
                model.put("data", data);
                String[] categories = result.getCategories();
                model.put("categories", categories);
                model.put("height", data.length * 50 + 100);
            }
        // 3D.
        } else if (page.equals("3D")) {
            Data result = JsonParser.getData(left, right, true);
            int[] data = result.getData();
            model.put("data", data);
            String[] categories = result.getCategories();
            model.put("categories", categories);
        // Sheet.
        } else if (page.equals("Sheet")) {
            Data result = JsonParser.getData(1, JsonParser.getJsonSize(), false);
            List<Map.Entry<String, Integer>> list = result.getList();
            model.put("list", list);
        }
        return "home";
    }
}

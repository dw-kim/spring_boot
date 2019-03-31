package com.spring_boot.spring_boot.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class HomeController {

    private int upper;
    private int lower;
    private boolean strType;

    @RequestMapping(value="/tagInfo", method = {RequestMethod.GET,RequestMethod.POST})
    public String getURLInfo(String type, String url) throws Exception {
        upper = 65;
        lower = 97;
        strType = true;

        ArrayList<String> replaceStr = new ArrayList<>();
        HashMap<String, Integer> hm = new HashMap<>();

        Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).execute();
        Document document = response.parse();

        String str = !type.equals("noneHTML") ? document.html() : document.text();

        Pattern regex = Pattern.compile("[a-zA-Z0-9]");
        Matcher regexMather = regex.matcher(str);

        while(regexMather.find()) {
            replaceStr.add(regexMather.group());
            hm.put(regexMather.group(), hm.getOrDefault(regexMather.group(), 0) + 1);
        }

        str = "";

        while(!hm.isEmpty()) {
            str += getData(hm);
        }

        return str;
    }

    private String getData(HashMap<String, Integer> map) {
        String str = "";
        if(strType) {
            if(map.containsKey(String.valueOf((char) upper))) {
                str = String.valueOf((char) upper);
                map.put(str, map.get(str) - 1);

                if(map.get(str) == 0) {
                    map.remove(str);
                }
            }else if(map.containsKey(String.valueOf((char) lower))) {
                str = String.valueOf((char) lower);
                map.put(str, map.get(str) - 1);

                if(map.get(str) == 0){
                    lower++;
                    upper++;
                    map.remove(str);
                }
            } else {
                lower++;
                upper++;
            }
        } else {
            for(int i=48; i <= 57; i++) {
                if(map.containsKey(String.valueOf((char) i))) {
                    str = String.valueOf((char) i);
                    map.put(str, map.get(str) - 1);

                    if(map.get(str) == 0) {
                        map.remove(str);
                    }

                    break;
                }
            }
        }
        strType = !strType;
        return str;
    }

}

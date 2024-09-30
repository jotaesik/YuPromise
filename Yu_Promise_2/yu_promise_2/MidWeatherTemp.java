package com.example.yupromise;

import android.util.Log;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MidWeatherTemp {
    private ArrayList<MidTmp> midTmpArray;
    private String date;
    private String local;

    MidWeatherTemp(String date, String local, ArrayList<MidTmp> midTmpArray) {
        this.midTmpArray = midTmpArray;
        this.date = date;
        this.local = local;
    }

    public void getMidTmpArray() {
        parsing(getWeatherReBody());

    }

    private InputStream getWeatherReBody() {
        try {
            StringBuilder urlBuilder = new StringBuilder(""); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "="); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("regId", "UTF-8") + "=" + URLEncoder.encode(local, "UTF-8")); /*11B10101 서울, 11B20201 인천 등 (별첨 파일 참조)*/
            urlBuilder.append("&" + URLEncoder.encode("tmFc", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); /*-일 2회(06:00,18:00)회 생성 되며 발표시각을 입력- YYYYMMDD0600(1800) 최근 24시간 자료만 제공*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            //Log.d("로그", "conn: " + conn);
            return conn.getInputStream();

        } catch (ProtocolException protocolException) {
            protocolException.printStackTrace();
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return null;
    }

    private void parsing(InputStream reBody) {
        //Log.d("로그", "mid weather temp parsing started.\n" + reBody.toString());
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = null;
            parser = factory.newPullParser();
            parser.setInput(reBody, null);

            boolean min3 = false, min4 = false, min5 = false, min6 = false, min7 = false, min8 = false, min9 = false, min10 = false,
                    max3 = false, max4 = false, max5 = false, max6 = false, max7 = false, max8 = false, max9 = false, max10 = false,
                    resultMsg = false;
            int eventType = 0;
            ArrayList minArray = new ArrayList<String>();
            ArrayList maxArray = new ArrayList<String>();
            eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("resultMsg")) {
                            resultMsg = true;
                        }
                        if (parser.getName().equals("taMin3")) {
                            min3 = true;
                        }
                        if (parser.getName().equals("taMax3")) {
                            max3 = true;
                        }
                        if (parser.getName().equals("taMin4")) {
                            min4 = true;
                        }
                        if (parser.getName().equals("taMax4")) {
                            max4 = true;
                        }
                        if (parser.getName().equals("taMin5")) {
                            min5 = true;
                        }
                        if (parser.getName().equals("taMax5")) {
                            max5 = true;
                        }
                        if (parser.getName().equals("taMin6")) {
                            min6 = true;
                        }
                        if (parser.getName().equals("taMax6")) {
                            max6 = true;
                        }
                        if (parser.getName().equals("taMin7")) {
                            min7 = true;
                        }
                        if (parser.getName().equals("taMax7")) {
                            max7 = true;
                        }
                        if (parser.getName().equals("taMin8")) {
                            min8 = true;
                        }
                        if (parser.getName().equals("taMax8")) {
                            max8 = true;
                        }
                        if (parser.getName().equals("taMin9")) {
                            min9 = true;
                        }
                        if (parser.getName().equals("taMax9")) {
                            max9 = true;
                        }
                        if (parser.getName().equals("taMin10")) {
                            min10 = true;
                        }
                        if (parser.getName().equals("taMax10")) {
                            max10 = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if(resultMsg) {
                            if(parser.getText() == "NO_DATA") {
                                midTmpArray.add(null);
                                Log.d("로그", "no data");
                                resultMsg = false;
                                break;
                            }
                        }
                        if (min3) {
                            minArray.add(parser.getText());
                            min3 = false;
                        }
                        if (max3) {
                            maxArray.add(parser.getText());
                            max3 = false;
                        }
                        if (min4) {
                            minArray.add(parser.getText());
                            min4 = false;
                        }
                        if (max4) {
                            maxArray.add(parser.getText());
                            max4 = false;
                        }
                        if (min5) {
                            minArray.add(parser.getText());
                            min5 = false;
                        }
                        if (max5) {
                            maxArray.add(parser.getText());
                            max5 = false;
                        }
                        if (min6) {
                            minArray.add(parser.getText());
                            min6 = false;
                        }
                        if (max6) {
                            maxArray.add(parser.getText());
                            max6 = false;
                        }
                        if (min7) {
                            minArray.add(parser.getText());
                            min7 = false;
                        }
                        if (max7) {
                            maxArray.add(parser.getText());
                            max7 = false;
                        }
                        if (min8) {
                            minArray.add(parser.getText());
                            min8 = false;
                        }
                        if (max8) {
                            maxArray.add(parser.getText());
                            max8 = false;
                        }
                        if (min9) {
                            minArray.add(parser.getText());
                            min9 = false;
                        }
                        if (max9) {
                            maxArray.add(parser.getText());
                            max9 = false;
                        }
                        if (min10) {
                            minArray.add(parser.getText());
                            min10 = false;
                        }
                        if (max10) {
                            maxArray.add(parser.getText());
                            max10 = false;
                        }
                        break;
                }

                eventType = parser.next();
            }

                for (int j = 0; j < minArray.size(); j++) {
                    midTmpArray.add(new MidTmp(minArray.get(j).toString(), maxArray.get(j).toString()));
                }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            Log.d("로그", e.getMessage());
        }
    }
}

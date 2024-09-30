package com.example.yupromise;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MidWeatherSkyNRainfall {
    private ArrayList<MidSkyNRainfall> midSkyNRainfallArray;
    private String date;
    private String local;

    MidWeatherSkyNRainfall(String date, String local, ArrayList<MidSkyNRainfall> midSkyNRainfallArray) {
        this.midSkyNRainfallArray = midSkyNRainfallArray;
        this.date = date;
        this.local = local;
    }

    public void getMidSkyNRainfall() {
        parsing(getWeatherReBody());
    }

    private InputStream getWeatherReBody() {
        try {
            StringBuilder urlBuilder = new StringBuilder(""); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + ""); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("regId", "UTF-8") + "=" + URLEncoder.encode(local, "UTF-8")); /*11B0000 서울, 인천, 경기도 11D10000 등 (활용가이드 하단 참고자료 참조)*/
            urlBuilder.append("&" + URLEncoder.encode("tmFc", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); /*-일 2회(06:00,18:00)회 생성 되며 발표시각을 입력 YYYYMMDD0600(1800)-최근 24시간 자료만 제공*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            Log.d("로그", "conn: " + conn);

            return conn.getInputStream();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("로그", e.getMessage() + " 0");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("로그", e.getMessage() + " 2");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("로그", e.getMessage() + " 3");
       }
        return null;
    }

    private void parsing(InputStream reBody) {
        Log.d("로그", "sky n rainfall parsign started.");
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = null;
            parser = factory.newPullParser();
            parser.setInput(reBody, null);

            boolean resultMsg = false;
            boolean rnSt3Am = false, rnSt3Pm = false, rnSt4Am = false, rnSt4Pm = false,
                    rnSt5Am = false, rnSt5Pm = false, rnSt6Am = false, rnSt6Pm = false, rnSt7Am = false, rnSt7Pm = false,
                    rnSt8 = false, rnSt9 = false, rnSt10 = false;
            boolean wf3Am = false, wf3Pm = false, wf4Am = false, wf4Pm = false,
                    wf5Am = false, wf5Pm = false, wf6Am = false, wf6Pm = false, wf7Am = false, wf7Pm = false,
                    wf8 = false, wf9 = false, wf10 = false;
            ArrayList skyAmArray = new ArrayList<String>();
            ArrayList skyPmArray = new ArrayList<String>();
            ArrayList rainfallAmArray = new ArrayList<String>();
            ArrayList rainfallPmArray = new ArrayList<String>();

            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("resultMsg")) {
                            resultMsg = true;
                        }
                        if(parser.getName().equals("rnSt3Am")) {
                            rnSt3Am = true;
                        }
                        if(parser.getName().equals("rnSt3Pm")) {
                            rnSt3Pm = true;
                        }
                        if(parser.getName().equals("rnSt4Am")) {
                            rnSt4Am = true;
                        }
                        if(parser.getName().equals("rnSt4Pm")) {
                            rnSt4Pm = true;
                        }
                        if(parser.getName().equals("rnSt5Am")) {
                            rnSt5Am = true;
                        }
                        if(parser.getName().equals("rnSt5Pm")) {
                            rnSt5Pm = true;
                        }
                        if(parser.getName().equals("rnSt6Am")) {
                            rnSt6Am = true;
                        }
                        if(parser.getName().equals("rnSt6Pm")) {
                            rnSt6Pm = true;
                        }
                        if(parser.getName().equals("rnSt7Am")) {
                            rnSt7Am = true;
                        }
                        if(parser.getName().equals("rnSt7Pm")) {
                            rnSt7Pm = true;
                        }
                        if(parser.getName().equals("rnSt8")) {
                            rnSt8 = true;
                        }
                        if(parser.getName().equals("rnSt9")) {
                            rnSt9 = true;
                        }
                        if(parser.getName().equals("rnSt10")) {
                            rnSt10 = true;
                        }
                        if(parser.getName().equals("wf3Am")) {
                            wf3Am = true;
                        }
                        if(parser.getName().equals("wf3Pm")) {
                            wf3Pm = true;
                        }
                        if(parser.getName().equals("wf4Am")) {
                            wf4Am = true;
                        }
                        if(parser.getName().equals("wf4Pm")) {
                            wf4Pm = true;
                        }
                        if(parser.getName().equals("wf5Am")) {
                            wf5Am = true;
                        }
                        if(parser.getName().equals("wf5Pm")) {
                            wf5Pm = true;
                        }
                        if(parser.getName().equals("wf6Am")) {
                            wf6Am = true;
                        }
                        if(parser.getName().equals("wf6Pm")) {
                            wf6Pm = true;
                        }
                        if(parser.getName().equals("wf7Am")) {
                            wf7Am = true;
                        }
                        if(parser.getName().equals("wf7Pm")) {
                            wf7Pm = true;
                        }
                        if(parser.getName().equals("wf8")) {
                            wf8 = true;
                        }
                        if(parser.getName().equals("wf9")) {
                            wf9 = true;
                        }
                        if(parser.getName().equals("wf10")) {
                            wf10 = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if(resultMsg) {
                            if(parser.getText() == "NO_DATA") {
                                midSkyNRainfallArray.add(null);
                                resultMsg = false;
                                break;
                            }
                            if(rnSt3Am) {
                                rainfallAmArray.add(parser.getText());
                                rnSt3Am = false;
                            }
                            if(rnSt3Pm) {

                                rainfallPmArray.add(parser.getText());
                                rnSt3Pm = false;
                            }
                            if(rnSt4Am) {
                                rainfallAmArray.add(parser.getText());
                                rnSt4Am = false;
                            }
                            if(rnSt4Pm) {
                                rainfallPmArray.add(parser.getText());
                                rnSt4Pm = false;
                            }
                            if(rnSt5Am) {
                                rainfallAmArray.add(parser.getText());
                                rnSt5Am = false;
                            }
                            if(rnSt5Pm) {
                                rainfallPmArray.add(parser.getText());
                                rnSt5Pm = false;
                            }
                            if(rnSt6Am) {
                                rainfallAmArray.add(parser.getText());
                                rnSt6Am = false;
                            }
                            if(rnSt6Pm) {
                                rainfallPmArray.add(parser.getText());
                                rnSt6Pm = false;
                            }
                            if(rnSt7Am) {
                                rainfallAmArray.add(parser.getText());
                                rnSt7Am = false;
                            }
                            if(rnSt7Pm) {
                                rainfallPmArray.add(parser.getText());
                                rnSt7Pm = false;
                            }
                            if(rnSt8) {
                                rainfallAmArray.add(parser.getText());
                                rainfallPmArray.add(parser.getText());
                                rnSt8 = false;
                            }
                            if(rnSt9) {
                                rainfallAmArray.add(parser.getText());
                                rainfallPmArray.add(parser.getText());
                                rnSt9 = false;
                            }
                            if(rnSt10) {
                                rainfallAmArray.add(parser.getText());
                                rainfallPmArray.add(parser.getText());
                                rnSt10 = false;
                            }
                            if(wf3Am) {
                                skyAmArray.add(parser.getText());
                                wf3Am = false;
                            }
                            if(wf3Pm) {
                                skyPmArray.add(parser.getText());
                                wf3Pm = false;
                            }
                            if(wf4Am) {
                                skyAmArray.add(parser.getText());
                                wf4Am = false;
                            }
                            if(wf4Pm) {
                                skyPmArray.add(parser.getText());
                                wf4Pm = false;
                            }
                            if(wf5Am) {
                                skyAmArray.add(parser.getText());
                                wf5Am = false;
                            }
                            if(wf5Pm) {
                                skyPmArray.add(parser.getText());
                                wf5Pm = false;
                            }
                            if(wf6Am) {
                                skyAmArray.add(parser.getText());
                                wf6Am = false;
                            }
                            if(wf6Pm) {
                                skyPmArray.add(parser.getText());
                                wf6Pm = false;
                            }
                            if(wf7Am) {
                                skyAmArray.add(parser.getText());
                                wf7Am = false;
                            }
                            if(wf7Pm) {
                                skyPmArray.add(parser.getText());
                                wf7Pm = false;
                            }
                            if(wf8) {
                                skyAmArray.add(parser.getText());
                                skyPmArray.add(parser.getText());
                                wf8 = false;
                            }
                            if(wf9) {
                                skyAmArray.add(parser.getText());
                                skyPmArray.add(parser.getText());
                                wf9 = false;
                            }
                            if(wf10) {
                                skyAmArray.add(parser.getText());
                                skyPmArray.add(parser.getText());
                                wf10 = false;
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
            for(int i = 0; i < skyAmArray.size(); i++) {
                midSkyNRainfallArray.add(new MidSkyNRainfall(skyAmArray.get(i).toString(), skyPmArray.get(i).toString(),
                        rainfallAmArray.get(i).toString(), rainfallPmArray.get(i).toString()));
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            Log.d("로그", e.getMessage()+"4");
        }
    }
}

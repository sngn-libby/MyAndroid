package edu.jaen.android.storage.notepad3;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class NewsLoadingActivity {

    private static NewsLoadingActivity instance = null;
    ArrayList<ArrayList<News>> NewsList;
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    Document doc;

    static final String [] newsUrl = new String[] {
            "https://www.sisain.co.kr/rss/allArticle.xml",
            "http://www.chosun.com/site/data/rss/rss.xml",
            "http://www.mediatoday.co.kr/rss/allArticle.xml",
            "https://news.google.com/rss?pz=1&cf=all&hl=ko&gl=KR&ceid=KR:ko"};
    static final String [] newsName = new String [] {"시사인", "조선일보", "미디어오늘", null};

    private NewsLoadingActivity() {
        NewsList = new ArrayList<ArrayList<News>>();

        for (int i = 0; i <4; i++) {
            NewsList.add((new ArrayList<News>()));
        }
    }

    public static NewsLoadingActivity getInstance() {
        if (instance == null) {
            instance = new NewsLoadingActivity();
        }
        return instance;
    }

    public ArrayList<ArrayList<News>> getNewsList() throws Exception {
        new NewsLoading().execute(0, 0, 0).get();
        return NewsList;
    }

    public void setNewsList(ArrayList<ArrayList<News>> newsList) {
        NewsList = newsList;
    }


    class NewsLoading extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... integers) {

            try {
                factory = DocumentBuilderFactory.newInstance();
                builder = factory.newDocumentBuilder();

                for (int i = 0; i < 4; i++) {

                    doc = builder.parse(newsUrl[i]);
                    NodeList titleList = doc.getElementsByTagName("title");
                    NodeList linkList = doc.getElementsByTagName("link");

                    for(int j=2;j<titleList.getLength() && j < 25;j++)
                    {
                        News tmp = new News();
                        if (i == 3) {
                            // 따로 처리 해줘야 함
                            String[] titleT = titleList.item(j).getTextContent().trim().split(" - ");
                            String nName = titleT[titleT.length-1];
                            if (nName.equals(newsName[0]) || nName.equals(newsName[1]) || nName.equals(newsName[2])) continue;
                            tmp.setTitle(titleT[0]);
                            tmp.setName(nName);
                        } else {
                            tmp.setName(newsName[i]);
                            tmp.setTitle(titleList.item(j).getTextContent().trim());

                        }
                        tmp.setLink(linkList.item(j).getTextContent().trim());
                        NewsList.get(i).add(tmp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

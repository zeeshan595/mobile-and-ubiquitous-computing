package com.traffic.zeeshan.scotlandtraffic;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RssReader extends AsyncTask<Void, Void, Void> {

    MainActivity context;
    String incidents = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    String roadworks = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    String future_roadworks = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    ProgressDialog progressDialog;
    URL url;

    public RssReader(MainActivity context){
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        context.UpdateListView();
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... params) {

        Resources.rssItemList.clear();
        Document doc_incidents = GetData(incidents);
        Document doc_roadworks = GetData(roadworks);
        Document doc_future_roadworks = GetData(future_roadworks);

        ProcessXML(doc_incidents, RssData.RssDataType.INCIDENTS);
        ProcessXML(doc_roadworks, RssData.RssDataType.ROADWORKS);
        ProcessXML(doc_future_roadworks, RssData.RssDataType.FUTURE_ROADWORKS);

        //Sort List
        Collections.sort(Resources.rssItemList, new Comparator<RssData>() {
            @Override
            public int compare(RssData item1, RssData item2)
            {

                return  item1.title.compareTo(item2.title);
            }
        });
        //Update id's
        for (int i = 0; i < Resources.rssItemList.size(); i++)
        {
            Resources.rssItemList.get(i).id = i;
        }

        return null;
    }

    private void ProcessXML(Document data, RssData.RssDataType type) {
        if (data != null)
        {
            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++)
            {
                Node currentChild = items.item(i);
                if (currentChild.getNodeName().equalsIgnoreCase("item"))
                {
                    NodeList itemChild = currentChild.getChildNodes();
                    String title = "", description = "", link = "", date = "";
                    float[] geo = new float[2];
                    for (int j = 0; j < itemChild.getLength(); j++)
                    {
                        Node current = itemChild.item(j);
                        switch (current.getNodeName())
                        {
                            case "title":
                                title = current.getTextContent();
                                break;
                            case "description":
                                description = current.getTextContent();
                                break;
                            case "link":
                                link = current.getTextContent();
                                break;
                            case "georss:point":
                                String[] geoString = current.getTextContent().split(" ");
                                if (geo.length == 2) {
                                    geo[0] = Float.parseFloat(geoString[0]);
                                    geo[1] = Float.parseFloat(geoString[1]);
                                }
                                break;
                            case "pubDate":
                                date = current.getTextContent();
                                break;
                        }
                    }
                    RssData rssDataItem = new RssData(title, type);
                    rssDataItem.id = Resources.rssItemList.size();
                    //Replace all <br /> with \n
                    rssDataItem.description = description.replace("<br/>", "\n").replace("<br />", "\n").replace("<br>", "\n");
                    rssDataItem.link = link;
                    rssDataItem.date = date;
                    rssDataItem.geo = geo;
                    Resources.rssItemList.add(rssDataItem);
                }
            }
        }
    }

    public Document GetData(String address)
    {
        try{
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(inputStream);
            return xmlDocument;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

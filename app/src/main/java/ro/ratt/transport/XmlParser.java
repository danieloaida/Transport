package ro.ratt.transport;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baby on 4/5/2016.
 */
public class XmlParser {
    private static final String ns = null;

    // We don't use namespaces

    public List<Station> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Station> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Station> markerList = new ArrayList<Station>();

        parser.require(XmlPullParser.START_TAG, ns, "resources");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            // Starts by looking for the station tag
            if (tagName.equals("marker")) {
                markerList.add(readStation(parser));
            } else {
                skip(parser);
            }
        }
        return markerList;
    }

    // Parses the contents of an Station. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private Station readStation(XmlPullParser parser) throws XmlPullParserException, IOException, NumberFormatException {
        parser.require(XmlPullParser.START_TAG, ns, "marker");
        String name;
        String tmpString;
        double lat;
        double lng;
        int id_st;

        name = parser.getAttributeValue(null, "station");
        tmpString = parser.getAttributeValue(null, "lat");
        lat = Double.parseDouble(tmpString);
        tmpString = parser.getAttributeValue(null, "lng");
        lng = Double.parseDouble(tmpString);
        tmpString = parser.getAttributeValue(null, "id_st");
        id_st = Integer.parseInt(tmpString);

        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "marker");
        return new Station(name, lat, lng, id_st,0,"");
    }


    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

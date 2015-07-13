/* FullName: Rohit Pankaj Ruparel*/

package com.example.hw5;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
import android.util.Xml;

public class AppUtil {

	static public class AppsSAXParser extends DefaultHandler {
		ArrayList < App > appList;
		App app = null;
		StringBuilder xmlInnerText;

		static public ArrayList < App > parseApps(InputStream in ) throws IOException, SAXException {
			AppsSAXParser parser = new AppsSAXParser();
			Xml.parse( in , Xml.Encoding.UTF_8, parser);
			return parser.getPersonList();
		}

		public ArrayList < App > getPersonList() {
			return appList;
		}

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.startDocument();
			appList = new ArrayList < App > ();
			xmlInnerText = new StringBuilder();
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
		Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			super.startElement(uri, localName, qName, attributes);
			if (localName.equals("entry")) {
				app = new App();
			} else if (app != null && localName.equals("id")) {
				app.setId(attributes.getValue("im:id"));
			} else if (app != null && localName.equals("link")) {
				app.setUrl(attributes.getValue("href"));
			} else if (app != null && qName.equals("im:price")) {
				app.setAppPrice(attributes.getValue("amount"));
			} else if (app != null && qName.equals("im:releaseDate")) {
				app.setReleaseDate(attributes.getValue("label"));
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName)
		throws SAXException {
			// TODO Auto-generated method stub
			super.endElement(uri, localName, qName);
			if (app != null && localName.equals("entry")) {
				appList.add(app);
			} else if (app != null && localName.equals("title")) {
				app.setTitle(xmlInnerText.toString().trim());
			} else if (app != null && qName.equals("im:artist")) {
				app.setDeveloperName(xmlInnerText.toString().trim());
			} else if (app != null && qName.equals("im:image")) {
				app.setSmallPhoto(xmlInnerText.toString().trim());
				app.setLargePhoto(xmlInnerText.toString().trim());
			}
			xmlInnerText.setLength(0);
		}

		@Override
		public void characters(char[] ch, int start, int length)
		throws SAXException {
			// TODO Auto-generated method stub
			super.characters(ch, start, length);
			xmlInnerText.append(ch, start, length);
		}

	}

}
package record;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class RecordList {
	public ArrayList<RecordItem> list;

	public RecordList() {
		list = new ArrayList<>();
	}

	public void add(RecordItem record) {
		list.add(record);
	}

	public void remove(int index) {
		list.remove(index);
	}

	public void remove(RecordItem record) {
		list.remove(record);
	}

	public RecordItem get(int index) {
		return list.get(index);
	}

	public int size() {
		return list.size();
	}

	@Override
	public String toString() {
		String result = "";
		for (RecordItem record : list) {
			result += record.toString() + "\n";
		}
		return result;
	}

	public void clear() {
		list.clear();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public RecordItem search(String word) {
		for (RecordItem record : list) {
			if (record.getWord().equals(word)) {
				return record;
			}
		}
		return null;
	}

	public ArrayList<RecordItem> getRecordList() {
		return list;
	}

	public ArrayList<Word> getWordList() {
		ArrayList<Word> wordList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Word word = new Word(list.get(i).getWord(), i);
			wordList.add(word);
		}
		return wordList;
	}

	public void parseXML(File inputFile) throws Exception {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("record");
			System.out.println(nList.getLength());
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String word = eElement.getElementsByTagName("word").item(0).getTextContent();
					String meaning = eElement.getElementsByTagName("meaning").item(0).getTextContent();
					RecordItem record = new RecordItem(word, meaning);
					add(record);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		System.out.println("Done");
	}

	public void writeXML(File file) throws Exception {// Create a new XML output factory
         // Create a new DocumentBuilderFactory and DocumentBuilder object
		 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		 
		 // Create a new Document object and set the XML encoding to UTF-8
		 Document document = documentBuilder.newDocument();
		 document.setXmlStandalone(true);
		 document.setXmlVersion("1.0");
		//  document.setXmlEncoding("UTF-8");
		 
		 // Create a new root element and append it to the Document object
		 Element rootElement = document.createElement("dictionary");
		 document.appendChild(rootElement);
		 
		 // Loop through the record list and create a new record element for each RecordItem object
		 for (RecordItem recordItem : list) {
			 // Create a new record element
			 Element recordElement = document.createElement("record");
			 rootElement.appendChild(recordElement);
			 
			 // Create a new word element and set its text content to the word value of the RecordItem object
			 Element wordElement = document.createElement("word");
			 wordElement.appendChild(document.createTextNode(recordItem.getWord()));
			 recordElement.appendChild(wordElement);
			 
			 // Create a new meaning element and set its text content to the meaning value of the RecordItem object
			 Element meaningElement = document.createElement("meaning");
			 meaningElement.appendChild(document.createTextNode(recordItem.getMeaning()));
			 recordElement.appendChild(meaningElement);
		 }
		 
		 // Write the Document object to the XML file using UTF-8 encoding
		 Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8); 
		 // Use a TransformerFactory to create a Transformer object
		 javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
		 javax.xml.transform.Transformer transformer = tf.newTransformer();
		 transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "utf-8");
		 transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
		 
		 // Use a DOMSource to specify the source tree to be transformed
		 javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
		 
		 // Use a StreamResult to specify the output file and write the transformed result to it
		 javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
		 transformer.transform(source, result);
	}
}

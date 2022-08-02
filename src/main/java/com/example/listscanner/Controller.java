package com.example.listscanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Controller {
    @FXML
    public ListView<Hyperlink> outputList;
    private File myObj;
    private void openBrowser(String url){
        javafx.application.Application application = new Application();
        application.getHostServices().showDocument(url);
    }
    public void isValid(String url){
        StringBuilder messageBox = new StringBuilder();
        try{
            Connection.Response response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).execute();
            if(response.statusCode() < 400){
                messageBox.append(url).append("\n");
                Hyperlink hyperlink = new Hyperlink(url);
                outputList.getItems().add(hyperlink);
                hyperlink.setOnAction((ActionEvent event) -> {
                    openBrowser(url);
                    event.consume();
                });
            }
        } catch (Exception ignored) {}
        try {
            FileWriter myWriter = new FileWriter(myObj, true);
            BufferedWriter output = new BufferedWriter(myWriter);
            output.write(String.valueOf(messageBox));
            output.close();
        } catch (IOException ignored) {}
    }
    private static Set<String> findLinks(String filePath) throws IOException {
        Set<String> links = new HashSet<>();
        File file = new File(filePath);
        Document doc= Jsoup.parse(file, "UTF-8");
        Elements elements = doc.select("a[href]");
        for (Element element : elements) {
            links.add(element.attr("href"));
        }
        return links;
    }
    @FXML
    protected void onStartScan() throws IOException {
        outputList.getItems().clear();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML file", "*.html"));
        File f = fc.showOpenDialog(null);
        if(f != null){
            try {
                int i = 0;
                while (true){
                    File file = new File("output\\");
                    if(file.mkdir()) System.out.println();
                    myObj = new File("output\\list"+ i + ".txt");
                    if (myObj.createNewFile()) {
                        break;
                    } else {
                        i++;
                    }
                }
            } catch (IOException ignored) {}
            for (String link : findLinks(f.getAbsolutePath())) {
                isValid(link);
            }
        }
    }
}
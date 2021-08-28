package com.company;

import com.company.image.ColorSchema;
import com.company.image.Converter;
import com.company.image.TextColorSchema;
import com.company.image.TextGraphicsConverter;
import com.company.server.GServer;

import java.io.File;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) throws Exception {
	TextGraphicsConverter converter = new Converter();
	TextColorSchema schema = new ColorSchema();
	converter.setMaxHeight(300);
	converter.setMaxWidth(300);
	converter.setMaxRatio(4);
	converter.setTextColorSchema(schema);
	converter.convert("https://avatanplus.com/files/resources/original/5817a69acd9591581c62ccda.png");
	GServer server = new GServer(converter);
	server.start();
	PrintWriter fileWriter = new PrintWriter(new File("converted-image.txt"));
	fileWriter.write(converter.convert("https://avatanplus.com/files/resources/original/5817a69acd9591581c62ccda.png"));
	fileWriter.close();
    }
}

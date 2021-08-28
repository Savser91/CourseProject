package com.company;

import com.company.image.ColorSchema;
import com.company.image.Converter;
import com.company.image.TextGraphicsConverter;
import com.company.server.GServer;

public class Main {

    public static void main(String[] args) throws Exception {
	TextGraphicsConverter converter = new Converter();
	ColorSchema schema = new ColorSchema();
	converter.setMaxHeight(300);
	converter.setMaxWidth(300);
	converter.setTextColorSchema(schema);

	converter.convert("http://abali.ru/wp-content/uploads/2010/12/znak-radiaciya.png");
	GServer server = new GServer(converter);
	server.start();
	//PrintWriter fileWriter = new PrintWriter(new File("converted-image.txt"));
	//fileWriter.write(converter.convert("http://abali.ru/wp-content/uploads/2010/12/znak-radiaciya.png"));
	//fileWriter.close();
    }
}

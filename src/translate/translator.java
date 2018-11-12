package translate;

//Imports the Google Cloud client library
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class translator {
	public static String translate(String text) throws Exception {
	 // Instantiates a client
	 Translate translate = TranslateOptions.getDefaultInstance().getService();
	
	
	 // Translates some text into Russian
	 Translation translation =
	     translate.translate(
	         text,
	         TranslateOption.sourceLanguage("en"),
	         TranslateOption.targetLanguage("es"));
	
	
	 System.out.printf("Text: %s%n", text);
	 System.out.printf("Translation: %s%n", translation.getTranslatedText());
	 
	 return translation.getTranslatedText();
	}
}
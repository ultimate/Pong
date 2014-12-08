package ultimate.pong;

import java.awt.Color;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TestSomething
{
	abstract class ColorMixIn
	{
		public ColorMixIn(@JsonProperty("r") int r, @JsonProperty("g") int g, @JsonProperty("b") int b)
		{
		}

		@JsonProperty("r")
		abstract int getRed();

		@JsonProperty("g")
		abstract int getGreen();

		@JsonProperty("b")
		abstract int getBlue();

		@JsonIgnore
		abstract int getAlpha();
		
		@JsonIgnore
		abstract int getTransparency();
		
		@JsonIgnore
		abstract int getColorSpace();

		@JsonIgnore
		abstract int getRGB();
	};

	public static void main(String[] args) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.addMixInAnnotations(Color.class, ColorMixIn.class);
		ObjectWriter writer = mapper.writer();
		ObjectReader reader = mapper.reader(Color.class);

		Color[] colors = { Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.BLACK };

		String s;
		Color c2;
		for(Color c : colors)
		{
			System.out.println(c); 					// java.awt.Color[r=255,g=255,b=255]
			s = writer.writeValueAsString(c);		
			System.out.println(s);					// {"b":255,"g":255,"r":255}
			c2 = reader.readValue(s);				// --> fails
			System.out.println(c2);
			System.out.println("equal ? " + c2.equals(c));
		}
	}
}

package ultimate.pong.data.model;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.objects.Ball;
import ultimate.pong.data.model.objects.Slider;
import ultimate.pong.data.model.objects.Wall;
import ultimate.pong.math.Vector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class MapObjectTest extends TestCase
{
	protected transient final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void testListDeserialization() throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer();
		ObjectReader reader = mapper.reader().withType(new TypeReference<List<MapObject>>() {});
		
		Ball ball = new Ball();
		ball.setPosition(new Vector(1, 2));

		Wall wall = new Wall();
		wall.setStart(new Vector(3, 4));
		wall.setEnd(new Vector(5, 6));

		Slider slider = new Slider();
		slider.setStart(new Vector(7, 8));
		slider.setEnd(new Vector(9, 0));

		List<MapObject> in = Arrays.asList(new MapObject[] { ball, wall, slider });
		
		String serialization = writer.writeValueAsString(in);
		
		logger.info(serialization);
		
		List<MapObject> out = reader.readValue(serialization);
		
		assertEquals(in, out);
		assertEquals(in.get(0), out.get(0));
		assertEquals(in.get(1), out.get(1));
		assertEquals(in.get(2), out.get(2));
	}
}

package pl.kurs.task1.customserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import pl.kurs.task1.models.Circle;
import pl.kurs.task1.models.Rectangle;
import pl.kurs.task1.models.Shape;
import pl.kurs.task1.models.Square;

import java.io.IOException;

public class ShapeDeserializer extends StdDeserializer<Shape> {

    public ShapeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Shape deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String type = node.get("type").asText();

        return switch (type) {
            case "square" -> new Square(node.get("side").asDouble());
            case "circle" -> new Circle(node.get("radius").asDouble());
            case "rectangle" -> new Rectangle(
                    node.get("width").asDouble(),
                    node.get("height").asDouble()
            );
            default -> throw new IllegalArgumentException("Shape type unknown!" + type);
        };

    }
}

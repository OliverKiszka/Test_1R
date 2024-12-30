package pl.kurs.task1.customserializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.kurs.task1.models.Circle;
import pl.kurs.task1.models.Rectangle;
import pl.kurs.task1.models.Shape;
import pl.kurs.task1.models.Square;

import java.io.IOException;

public class ShapeSerializer extends StdSerializer<Shape> {

    public ShapeSerializer(Class<Shape> t) {
        super(t);
    }


    @Override
    public void serialize(Shape value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        if (value instanceof Square){
            gen.writeStringField("type", "square");
            gen.writeNumberField("side", ((Square) value).getSide());
        } else if (value instanceof Circle){
            gen.writeStringField("type", "circle");
            gen.writeNumberField("radius", ((Circle) value).getRadius());
        } else if (value instanceof Rectangle){
            gen.writeStringField("type", "rectangle");
            gen.writeNumberField("width", ((Rectangle) value).getWidth());
            gen.writeNumberField("height", ((Rectangle) value).getHeight());
        }
        gen.writeEndObject();
    }
}

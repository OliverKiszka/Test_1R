package pl.kurs.task1.customserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import pl.kurs.task1.models.Shape;

public enum ObjectMapperHolder {
    INSTANCE;
    private final ObjectMapper objectMapper;

    ObjectMapperHolder() {
        this.objectMapper = create();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    private static ObjectMapper create(){
        ObjectMapper mapper = new ObjectMapper();

        //serializer
        ShapeSerializer shapeSerializer = new ShapeSerializer(Shape.class);
        SimpleModule sm1 = new SimpleModule("ShapeSerializer");
        sm1.addSerializer(Shape.class, shapeSerializer);
        //deserializer
        ShapeDeserializer shapeDeserializer = new ShapeDeserializer(Shape.class);
        SimpleModule sm2 = new SimpleModule("ShapeDeserializer");
        sm2.addDeserializer(Shape.class, shapeDeserializer);


        mapper.registerModules(sm1, sm2);
        return mapper;
    }
}

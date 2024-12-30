package pl.kurs.task1.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kurs.task1.customserializers.ObjectMapperHolder;
import pl.kurs.task1.exceptions.NoShapeFoundException;
import pl.kurs.task1.models.Shape;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ShapeService {

    private final ObjectMapper objectMapper;


    public ShapeService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Shape findShapeWithLargestArea(List<Shape> shapes) throws NoShapeFoundException {
        return Optional.ofNullable(shapes)
                .orElseGet(Collections::emptyList)
                .stream()
                .max(Comparator.comparingDouble(Shape::calculateArea))
                .orElseThrow(()-> new NoShapeFoundException("No shape found!"));
    }

    public Shape findShapeWithLargestPerimeterByType(List<Shape> shapes, Class<? extends Shape> type) throws NoShapeFoundException {
        return Optional.ofNullable(shapes)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(type::isInstance)
                .max(Comparator.comparingDouble(Shape::calculatePerimeter))
                .orElseThrow(()-> new NoShapeFoundException("No shape found by given type!"));
    }

    public void exportShapesToJson(List<Shape> shapes, String path) throws IOException {
        objectMapper.writeValue(new File(path), shapes);
    }

    public List<Shape> importShapesFromJson(String path) throws IOException {
        return objectMapper.readValue(new File(path), new TypeReference<>() {
        });
    }


}

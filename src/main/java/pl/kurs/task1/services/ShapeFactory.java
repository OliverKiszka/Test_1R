package pl.kurs.task1.services;

import pl.kurs.task1.exceptions.InvalidShapeParameterException;
import pl.kurs.task1.models.Circle;
import pl.kurs.task1.models.Rectangle;
import pl.kurs.task1.models.Shape;
import pl.kurs.task1.models.Square;

import java.util.HashMap;
import java.util.Map;

public final class ShapeFactory {

    private static final Map<String, Shape> cache = new HashMap<>();

    private ShapeFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Square createSquare(double side) throws InvalidShapeParameterException {
        if (side <= 0){
            throw new InvalidShapeParameterException("Side length must be greater than 0!");
        }
        String key = String.format("square:%.5f",side);
        return (Square) cache.computeIfAbsent(key, x -> new Square(side));
    }

    public static Circle createCircle(double radius) throws InvalidShapeParameterException {
        if (radius <= 0){
            throw new InvalidShapeParameterException("Radius must be greater than 0!");
        }
        String key = String.format("circle:%.5f", radius);
        return (Circle) cache.computeIfAbsent(key, x -> new Circle(radius));
    }

    public static Rectangle createRectangle(double width, double height) throws InvalidShapeParameterException {
        if (width <= 0 || height <=0){
            throw new InvalidShapeParameterException("Width and Height must be greater than 0!");
        }
        String key = String.format("rectangle:%.5f:%.5f", width, height);
        return (Rectangle) cache.computeIfAbsent(key, x -> new Rectangle(width, height));
    }
    static void clearCache(){
        cache.clear();
    }
    static int getCacheSize(){
        return cache.size();
    }


}

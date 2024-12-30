package pl.kurs.task1.services;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import pl.kurs.task1.exceptions.InvalidShapeParameterException;
import pl.kurs.task1.models.Circle;
import pl.kurs.task1.models.Rectangle;
import pl.kurs.task1.models.Square;

import java.lang.reflect.Constructor;


public class ShapeFactoryTest {
    @Before
    public void setUp(){
        ShapeFactory.clearCache();
    }


    @Test
    public void shouldCreateSquareWithCorrectSide() throws InvalidShapeParameterException {
        Square square = ShapeFactory.createSquare(5);
        Assertions.assertThat(square.getSide())
                .as("Square side should match the provided parameter")
                .isEqualTo(5);
    }

    @Test
    public void shouldCreateCircleWithCorrectRadius() throws InvalidShapeParameterException {
        Circle circle = ShapeFactory.createCircle(5);
        Assertions.assertThat(circle.getRadius())
                .as("Circle radius should match the provided parameter")
                .isEqualTo(5);
    }

    @Test
    public void shouldCreateRectangleWithCorrectDimensions() throws InvalidShapeParameterException {
        Rectangle rectangle = ShapeFactory.createRectangle(5, 10);
        Assertions.assertThat(rectangle.getWidth())
                .as("Rectangle width should match the provided parameter")
                .isEqualTo(5);
        Assertions.assertThat(rectangle.getHeight())
                .as("Rectangle height should match the provided parameter")
                .isEqualTo(10);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingSquareWithNegativeSide() throws InvalidShapeParameterException {
        ShapeFactory.createSquare(-5);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingSquareWithSideEqual0() throws InvalidShapeParameterException {
        ShapeFactory.createSquare(0);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingCircleWithNegativeRadius() throws InvalidShapeParameterException {
        ShapeFactory.createCircle(-5);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingRadiusWithRadiusEqual0() throws InvalidShapeParameterException {
        ShapeFactory.createCircle(0);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingSquareWithNegativeWidthAndPositiveHeight() throws InvalidShapeParameterException {
        ShapeFactory.createRectangle(-5, 10);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingSquareWithPositiveWidthAndNegativeHeight() throws InvalidShapeParameterException {
        ShapeFactory.createRectangle(5, -10);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingSquareWithNegativeDimensions() throws InvalidShapeParameterException {
        ShapeFactory.createRectangle(-5, -10);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingSquareWithWidthEqual0AndPositiveHeight() throws InvalidShapeParameterException {
        ShapeFactory.createRectangle(0, 10);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingSquareWithPositiveWidthAndHeightEqual0() throws InvalidShapeParameterException {
        ShapeFactory.createRectangle(5, 0);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingSquareWithDimensionsEqual0() throws InvalidShapeParameterException {
        ShapeFactory.createRectangle(0, 0);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingSquareWithWidthEqual0AndNegativeHeight() throws InvalidShapeParameterException {
        ShapeFactory.createRectangle(0, -10);
    }

    @Test(expected = InvalidShapeParameterException.class)
    public void shouldThrowInvalidShapeParameterExceptionWhenCreatingSquareWithNegativeWidthAndHeightEqual0() throws InvalidShapeParameterException {
        ShapeFactory.createRectangle(-5, 0);
    }

    @Test
    public void shouldReturnSameInstanceOfSquareWithSameParameters() throws InvalidShapeParameterException {
        Square sq1 = ShapeFactory.createSquare(5);
        Square sq2 = ShapeFactory.createSquare(5);
        Assertions.assertThat(sq1)
                .as("Square with side 5 should return the same instance from cache")
                .isSameAs(sq2);
    }

    @Test
    public void shouldReturnDifferentInstanceOfSquareWithDifferentParameters() throws InvalidShapeParameterException {
        Square sq1 = ShapeFactory.createSquare(5);
        Square sq2 = ShapeFactory.createSquare(10);
        Assertions.assertThat(sq1)
                .as("Squares with different sides should return different instances")
                .isNotSameAs(sq2);
    }

    @Test
    public void shouldReturnSameInstanceOfCircleWithSameParameters() throws InvalidShapeParameterException {
        Circle c1 = ShapeFactory.createCircle(5);
        Circle c2 = ShapeFactory.createCircle(5);
        Assertions.assertThat(c1)
                .as("Circle with radius 5 should return the same instance from cache")
                .isSameAs(c2);
    }

    @Test
    public void shouldReturnDifferentInstanceOfCircleWithDifferentParameters() throws InvalidShapeParameterException {
        Circle c1 = ShapeFactory.createCircle(5);
        Circle c2 = ShapeFactory.createCircle(10);
        Assertions.assertThat(c1)
                .as("Circles with different raidiuses should return different instances")
                .isNotSameAs(c2);
    }

    @Test
    public void shouldReturnSameInstanceOfRectangleWithSameParameters() throws InvalidShapeParameterException {
        Rectangle r1 = ShapeFactory.createRectangle(5, 10);
        Rectangle r2 = ShapeFactory.createRectangle(5, 10);
        Assertions.assertThat(r1)
                .as("Rectangle with width 5 and height 10 should return the same instance from cache")
                .isSameAs(r2);
    }

    @Test
    public void shouldReturnDifferentInstanceOfRectangleWithDifferentParameters() throws InvalidShapeParameterException {
        Rectangle r1 = ShapeFactory.createRectangle(5, 5);
        Rectangle r2 = ShapeFactory.createRectangle(10, 10);
        Assertions.assertThat(r1)
                .as("Rectangle with different dimensions should return different instances")
                .isNotSameAs(r2);
    }

    @Test
    public void shouldNotIncreaseCacheSizeWhenCreatingSquareWithSameParameters() throws InvalidShapeParameterException {
        ShapeFactory.createSquare(5);
        ShapeFactory.createSquare(5);
        ShapeFactory.createSquare(5);

        Assertions.assertThat(ShapeFactory.getCacheSize())
                .as("Cache size should not increase for same parameters")
                .isEqualTo(1);
    }
    @Test
    public void shouldNotIncreaseCacheSizeWhenCreatingCircleWithSameParameters() throws InvalidShapeParameterException {
        ShapeFactory.createCircle(5);
        ShapeFactory.createCircle(5);
        ShapeFactory.createCircle(5);

        Assertions.assertThat(ShapeFactory.getCacheSize())
                .as("Cache size should not increase for same parameters")
                .isEqualTo(1);
    }
    @Test
    public void shouldNotIncreaseCacheSizeWhenCreatingRectangleWithSameParameters() throws InvalidShapeParameterException {
        ShapeFactory.createRectangle(5, 10);
        ShapeFactory.createRectangle(5, 10);
        ShapeFactory.createRectangle(5, 10);

        Assertions.assertThat(ShapeFactory.getCacheSize())
                .as("Cache size should not increase for same parameters")
                .isEqualTo(1);
    }



}
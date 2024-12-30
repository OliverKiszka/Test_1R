package pl.kurs.task1.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import pl.kurs.task1.exceptions.InvalidShapeParameterException;
import pl.kurs.task1.exceptions.NoShapeFoundException;
import pl.kurs.task1.models.Circle;
import pl.kurs.task1.models.Rectangle;
import pl.kurs.task1.models.Shape;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;


public class ShapeServiceTest {

    private ShapeService shapeService;
    private List<Shape> testShapes;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void init() throws InvalidShapeParameterException {
        MockitoAnnotations.openMocks(this);
        shapeService = new ShapeService(objectMapper);
        testShapes = createTestShapes();
    }
    private List<Shape> createTestShapes() throws InvalidShapeParameterException {
        return List.of(
                ShapeFactory.createSquare(5),
                ShapeFactory.createCircle(3),
                ShapeFactory.createRectangle(12, 15),
                ShapeFactory.createCircle(7)
        );
    }

    //testy metody findShapeWithLargestArea()

    @Test
    public void shouldReturnRectangleInFindShapeWithLargestAreaMethod() throws NoShapeFoundException{
        Shape shapeWithLargestArea = shapeService.findShapeWithLargestArea(testShapes);

        assertThat(shapeWithLargestArea)
                .as("Rectangle  should have the largest area")
                .isInstanceOf(Rectangle.class)
                .extracting(Shape::calculateArea)
                .isEqualTo(180.0);
    }

    @Test
    public void shouldThrowNoShapeFoundExceptionWhenListIsEmptyInFindShapeWithLargestAreaMethod() {
        assertThrows(NoShapeFoundException.class, () ->
                shapeService.findShapeWithLargestArea(Collections.emptyList()));
    }

    @Test
    public void shouldThrowNoShapeFoundExceptionWhenListIsnullInFindShapeWithLargestAreaMethod() {
        assertThrows(NoShapeFoundException.class, () ->
                shapeService.findShapeWithLargestArea(null));
    }

    //testy metody findShapeWithLargestPerimeterByType()

    @Test
    public void shouldReturnCircleWithRadius7InFindShapeWithLargestPerimeterByTypeMethod() throws NoShapeFoundException {
        Shape circleWithLargestPerimeter = shapeService.findShapeWithLargestPerimeterByType(testShapes, Circle.class);

        assertThat(circleWithLargestPerimeter)
                .as("Circle with radius 7 should have the largest perimeter")
                .isInstanceOf(Circle.class)
                .extracting("radius")
                .isEqualTo(7.0);
    }

    @Test
    public void shouldThrowNoShapeFoundExceptionWhenNoShapesOfTypeInList() throws InvalidShapeParameterException {
        List<Shape> noCirclesList = List.of(
                ShapeFactory.createSquare(5),
                ShapeFactory.createRectangle(3, 8)
        );
        assertThrows(NoShapeFoundException.class, () ->
                shapeService.findShapeWithLargestPerimeterByType(noCirclesList, Circle.class));

    }

    @Test
    public void shouldThrowNoShapeFoundExceptionWhenListIsEmptyAndTypeIsCorrectInFindShapeWithLargestPerimeterByTypeMethod() {
        assertThrows(NoShapeFoundException.class, () -> shapeService.findShapeWithLargestPerimeterByType(new ArrayList<>(), Circle.class));
    }


    @Test
    public void shouldThrowNoShapeFoundExceptionWhenListIsNullAndTypeIsCorrectInFindShapeWithLargestPerimeterByTypeMethod() {
        assertThrows(NoShapeFoundException.class, () -> shapeService.findShapeWithLargestPerimeterByType(null, Circle.class));
    }

    //testy metody exportShapesToJson()

    @Test
    public void shouldExportShapesToJson() throws IOException {
        String path = "test_shapes.json";
        Mockito.doNothing().when(objectMapper).writeValue(Mockito.any(File.class), Mockito.eq(testShapes));

        shapeService.exportShapesToJson(testShapes, path);

        Mockito.verify(objectMapper).writeValue(Mockito.any(File.class), Mockito.eq(testShapes));

    }
    @Test
    public void shouldExportEmptyListToJson() throws IOException {
        List<Shape> emptyShapeList = Collections.emptyList();
        String path = "empty_shapes.json";

        Mockito.doNothing().when(objectMapper).writeValue(Mockito.any(File.class), Mockito.eq(emptyShapeList));

        shapeService.exportShapesToJson(emptyShapeList,path);

        Mockito.verify(objectMapper).writeValue(Mockito.any(File.class), Mockito.eq(emptyShapeList));

    }

    @Test(expected = IOException.class)
    public void shouldThrowIOExceptionWhenExportingToInvalidPath() throws IOException {
        String invalidPath = "/invalid/path/shapes.json";

        Mockito.doThrow(new IOException("Invalid path")).when(objectMapper).writeValue(Mockito.any(File.class), Mockito.eq(testShapes));

        shapeService.exportShapesToJson(testShapes, invalidPath);
    }

    //testy metody importShapesFromJson()

    @Test
    public void shouldImportShapesFromJson() throws IOException, InvalidShapeParameterException {
        List<Shape> expectedShapes = List.of(
                ShapeFactory.createSquare(5),
                ShapeFactory.createCircle(3)
        );
        Mockito.when(objectMapper.readValue(Mockito.any(File.class), Mockito.<TypeReference<List<Shape>>>any()))
                .thenReturn(expectedShapes);

        List<Shape> importedShapes = shapeService.importShapesFromJson("shapes.json");
        assertThat(importedShapes)
                .isEqualTo(expectedShapes);

        Mockito.verify(objectMapper).readValue(Mockito.any(File.class), Mockito.<TypeReference<List<Shape>>>any());
    }

    @Test
    public void shouldImportEmptyListFromJson() throws IOException {
        List<Shape> emptyShapeList = Collections.emptyList();

        Mockito.when(objectMapper.readValue(Mockito.any(File.class),Mockito.<TypeReference<List<Shape>>>any()))
                .thenReturn(emptyShapeList);

        List<Shape> importedShapes = shapeService.importShapesFromJson("empty_shapes.json");

        assertThat(importedShapes).isEmpty();

        Mockito.verify(objectMapper).readValue(Mockito.any(File.class), Mockito.<TypeReference<List<Shape>>>any());


    }

    @Test(expected = IOException.class)
    public void shouldThrowIOExceptionWhenImportingFromNonExistentFile() throws IOException {
        String ghostFile = "ghostfile.json";
        Mockito.when(objectMapper.readValue(Mockito.any(File.class), Mockito.<TypeReference<List<Shape>>>any()))
                .thenThrow(new IOException("File not found"));
        shapeService.importShapesFromJson(ghostFile);

    }


}
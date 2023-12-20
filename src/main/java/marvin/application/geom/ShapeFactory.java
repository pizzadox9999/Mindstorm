package marvin.application.geom;

public class ShapeFactory {
    private ShapeFactory() {
    }

    public static Shape createShape(String id, double[] data) {
        Shape shape = null;
        switch (id) {
        case Line.ID:
            shape = new Line(data[0], data[1], data[2], data[3]);
            break;
        case Rectangle.ID:
            shape = new Rectangle(data[0], data[1], data[2], data[3]);
            break;
        }

        return shape;
    }
}

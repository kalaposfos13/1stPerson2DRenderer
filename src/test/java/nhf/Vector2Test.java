package nhf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

class Vector2Test {

    @Test
    void testDefaultConstructor() {
        Vector2 v = new Vector2();
        assertEquals(0, v.x);
        assertEquals(0, v.y);
    }

    @Test
    void testConstructorWithValues() {
        Vector2 v = new Vector2(2.0, 3.0);
        assertEquals(2.0, v.x);
        assertEquals(3.0, v.y);
    }

    @Test
    void testConstructorWithVector() {
        Vector2 v1 = new Vector2(2.0, 3.0);
        Vector2 v2 = new Vector2(v1);
        assertEquals(2.0, v2.x);
        assertEquals(3.0, v2.y);
    }

    @Test
    void testConstructorWithPoint() {
        Point p = new Point(4, 5);
        Vector2 v = new Vector2(p);
        assertEquals(4, v.x);
        assertEquals(5, v.y);
    }

    @Test
    void testAdd() {
        Vector2 v1 = new Vector2(2.0, 3.0);
        Vector2 v2 = new Vector2(1.0, 1.0);
        Vector2 result = v1.add(v2);
        assertEquals(3.0, result.x);
        assertEquals(4.0, result.y);
    }

    @Test
    void testSub() {
        Vector2 v1 = new Vector2(5.0, 5.0);
        Vector2 v2 = new Vector2(3.0, 1.0);
        Vector2 result = v1.sub(v2);
        assertEquals(2.0, result.x);
        assertEquals(4.0, result.y);
    }

    @Test
    void testMul() {
        Vector2 v = new Vector2(2.0, 3.0);
        Vector2 result = v.mul(2.0);
        assertEquals(4.0, result.x);
        assertEquals(6.0, result.y);
    }
}

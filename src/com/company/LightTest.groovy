package com.company

import java.awt.Color
import java.awt.Point

class LightTest extends groovy.util.GroovyTestCase {
    //kinda know the issue?

    void testDistanceFromEdge() {
        def expected = 1
        assertEquals(expected, Light.distanceFromLine(new Point(0,999995), new Point(0,5), new Point(1, 11)))
    }
    void testDistanceFromEdge2() {
        def expected = 1
        assertEquals(expected, Light.distanceFromLine(new Point(0,5), new Point(0,999995), new Point(1, 11)))
    }

    void testDistanceFromEdge3() {
        def expected = 0
        assertEquals(expected, Light.distanceFromLine(new Point(-100000,900000), new Point(0,5), new Point(0, 1000)))
    }

    /**
    void testDistanceFromShadowEdge() {
        def shadow = new Light.Shadow()
        shadow.addPoint(-1000000,999995)
        shadow.addPoint(0,5)
        shadow.addPoint(10, 5)
        shadow.addPoint(1000010, 999995)

        def expected = 4
        assertEquals(expected, Light.distanceFromShadowEdge(shadow, new Point(10, 11)))
    }


    void testNewShadowCreation() {
        Light light = new Light(5, 0, 20, new Color(255,255,255))
        ArrayList<Point> points = new ArrayList<>()
        points.add(new Point(0, 5))
        points.add(new Point(10, 5))
        Light.Shadow shadow = light.newShadow(points)

        def expected = 4
        assertEquals(expected, Light.distanceFromShadowEdge(shadow, new Point(10,11)))
    }

    void testShadowMap() {
        Light light = new Light(0.1, 0, 20, new Color(255,255,255))
        Obj[] objects = new Obj[1]
        objects[0] = new Obj("src\\sprites\\test.png",0, 5, 1, 2)
        light.shadowMap(objects)//String sprite, double x, double y, double scale, int collision
        ArrayList<Point> points = light.shadows[0].points
        System.out.println(points.get(0))
        System.out.println(points.get(1))
        System.out.println(points.get(2))
        System.out.println(points.get(3))
        def expected = 0
        assertEquals(expected, Light.distanceFromShadowEdge(light.shadows[0], new Point(0, 1000)))
    }*/
}

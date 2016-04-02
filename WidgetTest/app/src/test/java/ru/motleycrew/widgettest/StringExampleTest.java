package ru.motleycrew.widgettest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by vas on 19.03.16.
 */
public class StringExampleTest {

    @Test
    public void testGetEquals() {
        StringsExample se = new StringsExample();
        assertTrue(se.getS1() != se.getS2());
    }
}

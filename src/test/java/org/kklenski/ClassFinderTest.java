package org.kklenski;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import org.junit.Test;

public class ClassFinderTest {
    
    @Test
    public void testBasicCases() {
        String[] classNames = new String[] {"a.b.FooBarBaz", "c.d.FooBar"};
        assertTrue(Arrays.equals(classNames, findMatching(classNames, "FB")));
        assertTrue(Arrays.equals(classNames, findMatching(classNames, "FoBa")));
        assertTrue(Arrays.equals(classNames, findMatching(classNames, "FBar")));
    }
    
    @Test
    public void testSpace() throws Exception {
        String[] classNames = new String[] {"a.b.FooBarBaz", "c.d.FooBar"};
        String[] expected = new String[] {"c.d.FooBar"};
        assertTrue(Arrays.equals(expected, findMatching(classNames, "FBar ")));
        
        //FIXME: test space in other pattern places
    }
    
    @Test
    public void testWildcard() throws Exception {
        String[] classNames = new String[] {"a.b.FooBarBaz", "c.d.FooBar"};
        String[] expected = new String[] {"a.b.FooBarBaz"};
        assertTrue(Arrays.equals(expected, findMatching(classNames, "F*Baz")));
        assertTrue(Arrays.equals(expected, findMatching(classNames, "*BB")));
        
        //FIXME: test plain wildcard / wildcard and space combinations
    }
    
    @Test
    public void testSorting() throws Exception {
        String[] classNames = new String[] {"a.b.FooBarDaz", "c.d.FooBarBaz", "e.f.FooBarCaz"};
        String[] expected = new String[] {"c.d.FooBarBaz", "e.f.FooBarCaz", "a.b.FooBarDaz"};
        assertTrue(Arrays.equals(expected, findMatching(classNames, "FB")));
    }
    
    //FIXME: test executing findMatching more than one time
    
    private String[] findMatching(String[] classNames, String pattern) {
        InputStream in = createStream(classNames);
        try {
            return new ClassFinder(in).findMatching(pattern).toArray(new String[] {});
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    private InputStream createStream(String... classNames) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PrintWriter writer = new PrintWriter(out);
            for (String className : classNames) {
                writer.write(className);
            }
            return new ByteArrayInputStream(out.toByteArray());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            }
        }
    }

}

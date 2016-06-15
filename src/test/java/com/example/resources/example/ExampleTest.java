package com.example.resources.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ExampleTest {

  private static final int CONSISTENCY_RUNS = 100;

  private static final long[] EXAMPLE_IDS = { //@formatter:off
      1,
      1,
      2,
      3,
      5,
      8,
      13,
      21,
      Long.MAX_VALUE
  }; //@formatter:on

  private static final String[] EXAMPLE_NAMES = { //@formatter:off
      null,
      "Sterling Archer",
      "Lana Kane",
      "Malory Archer",
      "Pam Poovey",
      "Cheryl Tunt",
      "Cyril Figgis",
      "Doctor Krieger",
      "Ray Gillette",
      "Woodhouse"
  };
  
  private ExampleBuilder testExamples;
  
  @Before
  public void setUp() {
    testExamples = new ExampleBuilder();
  }
  
  @Test
  public void toString_shouldReturnDynamicStringBasedOnIdAndName() throws Exception {
    final String messageTemplate = "An example with id %d should get, for toString: %s";
    
    while (testExamples.hasNext()) {
      final Example example = testExamples.getNext();
      final String expected = String.format("Example: { id: %d, name: %s }", example.getId(),
          example.getName());
      final String message = String.format(messageTemplate, example.getId(), expected);
      final String actual = example.toString();
      assertEquals(message, expected, actual);
    }
  }
  
  @Test
  public void equals_shouldReturnFalseIfRhsIsNull() throws Exception {
    while (testExamples.hasNext()) {
      final Example lhs = testExamples.getNext();
      assertFalse(lhs.equals(null));
    }
  }

  @Test
  public void equals_shouldReturnTrueIfAndOnlyIfEqualIdAndNameProperties() throws Exception {
    while (testExamples.hasNext()) {
      final Example lhs = testExamples.getNext();
      
      final ExampleBuilder rhsBuilder = new ExampleBuilder();
      
      while (rhsBuilder.hasNext()) {
        final Example rhs = rhsBuilder.getNext();
      
        if (lhs.getId() == rhs.getId()) {
          if (lhs.getName() == null && rhs.getName() == null) {
            assertTrue(lhs.equals(rhs));
          }
          else if (lhs.getName() == null) {
            assertFalse(lhs.equals(rhs));
          }
          else if (lhs.getName().equals(rhs.getName())) {
            assertTrue(lhs.equals(rhs));
          }
        } else {
          assertFalse(lhs.equals(rhs));
        }
      }
    }
  }
  
  @Test
  public void equals_shouldBeReflexive() throws Exception {
    while (testExamples.hasNext()) {
      final Example example = testExamples.getNext();
      assertTrue(example.equals(example));
    }
  }
  
  @Test
  public void equals_shouldBeSymetric() throws Exception {
    while (testExamples.hasNext()) {
      final Example lhs = testExamples.getNext();
      
      final ExampleBuilder rhsBuilder = new ExampleBuilder();
      
      while (rhsBuilder.hasNext()) {
        final Example rhs = rhsBuilder.getNext();
      
        if (lhs.equals(rhs)) {
          assertTrue(rhs.equals(lhs));
        } else {
          assertFalse(rhs.equals(lhs));
        }
      }
    }
  }
  
  @Test
  public void equals_shouldBeTransitive() throws Exception {
    
    while (testExamples.hasNext()) {
      final Example x = testExamples.getNext();
      
      final ExampleBuilder yBuilder = new ExampleBuilder();
      
      while (yBuilder.hasNext()) {
        final Example y = yBuilder.getNext();
        
        final ExampleBuilder zBuilder = new ExampleBuilder();
        
        while (zBuilder.hasNext()) {
          final Example z = zBuilder.getNext();
          
          if (x.equals(y) && y.equals(z)) {
            final String message = String.format(
                "Since %s equals %s, and %s equals %s, then $s should equal %s", 
                x.toString(), y.toString(), y.toString(), z.toString(), x.toString(), z.toString());
            assertTrue(message, x.equals(z));
          } else if (x.equals(y)) {
            final String message = String.format(
                "Since %s equals %s, but %s does not equal %s, then $s should not equal %s", 
                x.toString(), y.toString(), y.toString(), z.toString(), x.toString(), z.toString());
            assertFalse(message, x.equals(z));
          } else if (y.equals(z)){
            final String message = String.format(
                "Since %s does not equal %s, but %s equals %s, then $s should not equal %s", 
                x.toString(), y.toString(), y.toString(), z.toString(), x.toString(), z.toString());
            assertFalse(message, x.equals(z));
          }
        }
      }
    }
  }
  
  @Test
  public void equals_shouldBeConsistent() throws Exception {
  
    while (testExamples.hasNext()) {
      final Example lhs = testExamples.getNext();
      
      final ExampleBuilder rhsBuilder = new ExampleBuilder();
      
      while (rhsBuilder.hasNext()) {
        final Example rhs = rhsBuilder.getNext();
        
        if (lhs.equals(rhs)) {
          for (int i = 0; i < CONSISTENCY_RUNS; ++i) {
            assertTrue(lhs.equals(rhs));
          }
        } else {
          for (int i = 0; i < CONSISTENCY_RUNS; ++i) {
            assertFalse(lhs.equals(rhs));
          }
        }
      }
    }
  }
  
  @Test
  public void hashCode_shouldBeEqualIfAndOnlyIfObjectsAreEqual() throws Exception {
    while (testExamples.hasNext()) {
      final Example lhs = testExamples.getNext();
      
      final ExampleBuilder rhsBuilder = new ExampleBuilder();
      
      while (rhsBuilder.hasNext()) {
        final Example rhs = rhsBuilder.getNext();
      
        if (lhs.equals(rhs)) {
          assertEquals(lhs.hashCode(), rhs.hashCode());
        } else {
          assertNotEquals(lhs.hashCode(), rhs.hashCode());
        }
      }
    }
  }
  
  private class ExampleBuilder {
    
    private int currentId = 0;
    private int currentName = 0;
    
    private boolean hasNext() {
      if (currentId +1 >= EXAMPLE_IDS.length && currentName +1 >= EXAMPLE_NAMES.length) {
        return false;
      }
      return true;
    }
    
    private Example getNext() throws Exception {
      if (hasNext()) {
        final Example example = new Example();
        
        if (currentId + 1 >= EXAMPLE_IDS.length) {
          currentId = 0;
          ++currentName;
        } else {
          ++currentId;
        }
        
        example.setId(EXAMPLE_IDS[currentId]);
        example.setName(EXAMPLE_NAMES[currentName]);
        
        return example;
      } else {
        throw new Exception("Trying to get the next example, but there is none");
      }
    }
  }
}

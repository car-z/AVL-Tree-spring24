package hw6;

import hw6.bst.TreapMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Setup;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to Treap.
 */
@SuppressWarnings("All")
public class TreapMapTest extends BinarySearchTreeMapTest {

  protected Map<String, String> createMap(int seed) {
    return new TreapMap<>(seed);
  }

  //INSERT TESTS

  @Test
  @DisplayName("Insert one element and tree should rotate right to maintain min-heap")
  public void insertRightRotation() {
    Map<String,String> map = createMap(50);
    map.insert("1","a");
    map.insert("2","b");
    String[] expected = new String[]{
            "2:b:-1727040520",
            "1:a:-1160871061 null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert one element and tree should rotate left to maintain min-heap")
  public void insertLeftRotation() {
    Map<String, String> map = createMap(60);
    map.insert("1","a");
    map.insert("2","b");
    map.insert("3","c");
    String[] expected = new String[]{
            "1:a:-1166257546",
            "null 3:c:-52486579",
            "null null 2:b:1564841655 null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("When min heap property maintained, structure does not change upon insertion")
  public void insertStructureUnchanged(){
    Map<String,String> map = createMap(60);
    map.insert("2","b");
    map.insert("1","a");
    String[] expected1 = new String[] {
      "2:b:-1166257546",
      "1:a:1564841655 null"
    };
    assertEquals((String.join("\n", expected1) + "\n"), map.toString());
    map.insert("3","c");
    String[] expected2 = new String[] {
            "2:b:-1166257546",
            "1:a:1564841655 3:c:-52486579"
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert multiple elements and tree keeps min-heap property - test 1")
  public void insertMultipleTest1() {
    Map<String, String> map = createMap(50);
    map.insert("1","a");
    map.insert("2","b");
    map.insert("3","c");
    map.insert("4","d");
    map.insert("5","e");
    String[] expected = new String[]{
            "2:b:-1727040520",
            "1:a:-1160871061 3:c:-1657178909",
            "null null null 5:e:-1625295794",
            "null null null null null null 4:d:-765924271 null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert multiple elements and tree keeps min-heap property - test 2")
  public void insertMultipleTest2() {
    Map<String, String> map = createMap(60);
    map.insert("1","a");
    map.insert("2","b");
    map.insert("3","c");
    map.insert("4","d");
    map.insert("5","e");
    String[] expected = new String[]{
            "5:e:-1693311323",
            "1:a:-1166257546 null",
            "null 3:c:-52486579 null null",
            "null null 2:b:1564841655 4:d:158885860 null null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("size updates each time a new key/value pair is added")
  public void sizeUpdatesWhenInserting() {
    map.insert("1","a");
    map.insert("2","b");
    map.insert("3","c");
    map.insert("4","d");
    map.insert("5","e");
    assertEquals(5,map.size());
  }

  //REMOVE TESTS

  @Test
  @DisplayName("Remove one element and tree should rotate left to sink down")
  public void removeLeftRotation() {
    Map<String,String> map = createMap(50);
    map.insert("1","a");
    map.insert("2","b");
    map.remove("2");
    String[] expected = new String[]{
            "1:a:-1160871061"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove one element and tree should rotate right to sink down")
  public void removeRightRotation() {
    Map<String, String> map = createMap(60);
    map.insert("1","a");
    map.insert("2","b");
    map.insert("3","c");
    map.remove("3");
    String[] expected = new String[] {
            "1:a:-1166257546",
            "null 2:b:1564841655"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Removing root updates root and keeps tree in order")
  public void removeRoot() {
    Map<String, String> map = createMap(50);
    map.insert("1","a");
    map.insert("2","b");
    map.insert("3","c");
    map.insert("4","d");
    map.insert("5","e");
    map.remove("2");
    String[] expected = new String[]{
            "3:c:-1657178909",
            "1:a:-1160871061 5:e:-1625295794",
            "null null 4:d:-765924271 null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("removing one element reduces size of treap")
  public void removeUpdatesSize() {
    map.insert("1","a");
    map.insert("2","b");
    map.insert("3","c");
    map.insert("4","d");
    map.insert("5","e");
    map.remove("2");
    assertEquals(4,map.size());
  }

  @Test
  @DisplayName("Remove key from Treap of only one key (a leaf) works")
  public void removeFromTreapSizeOfOne() {
    map.insert("1","a");
    map.remove("1");
    assertEquals(0,map.size());
  }

  @Test
  @DisplayName("Remove target with one child works")
  public void removeTargetWithOneChild() {
    Map<String, String> map = createMap(50);
    map.insert("1","a");
    map.insert("2","b");
    map.insert("3","c");
    map.insert("4","d");
    map.insert("5","e");
    map.remove("5");
    String[] expected = new String[]{
            "2:b:-1727040520",
            "1:a:-1160871061 3:c:-1657178909",
            "null null null 4:d:-765924271",
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove target with two children works")
  public void removeTargetWithTwoChildren() {
    Map<String, String> map = createMap(60);
    map.insert("1","a");
    map.insert("2","b");
    map.insert("3","c");
    map.insert("4","d");
    map.insert("5","e");
    map.remove("3");
    String[] expected = new String[]{
            "5:e:-1693311323",
            "1:a:-1166257546 null",
            "null 4:d:158885860 null null",
            "null null 2:b:1564841655 null null null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Removing key from empty Treap throws error")
  public void removeFromEmptyTreapTreeThrowsError() {
    try {
      map.remove("1");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      return;
    }
  }

  @Test
  @DisplayName("When min heap property maintained, structure does not change upon removal")
  public void removeStructureUnchanged(){
    Map<String,String> map = createMap(60);
    map.insert("2","b");
    map.insert("1","a");
    map.insert("3","c");
    map.insert("4","d");
    String[] expected1 = new String[] {
            "2:b:-1166257546",
            "1:a:1564841655 3:c:-52486579",
            "null null null 4:d:158885860"
    };
    assertEquals((String.join("\n", expected1) + "\n"), map.toString());
    map.remove("3");
    String[] expected2 = new String[] {
            "2:b:-1166257546",
            "1:a:1564841655 4:d:158885860"
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());
  }

  @Test
  @DisplayName("inserting and removing many elements maintains properties of treap - test 1")
  public void insertAndRemoveMultipleElementsTest1() {
    Map<String, String> map = createMap(40);
    map.insert("5","e");
    map.insert("3","c");
    map.insert("2","b");
    map.insert("4","d");
    map.insert("7","g");
    map.remove("4");
    map.insert("6","f");
    map.remove("3");
    String[] expected = new String[]{
            "5:e:-1170874532",
            "2:b:95830475 6:f:1702710456",
            "null null null 7:g:1929790192"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("inserting and removing many elements maintains properties of treap - test 2")
  public void insertAndRemoveMultipleElementsTest2() {
    Map<String, String> map = createMap(60);
    map.insert("1","a");
    map.insert("2","b");
    map.insert("3","c");
    map.insert("4","d");
    map.insert("5","e");
    String[] expected = new String[]{
            "5:e:-1693311323",
            "1:a:-1166257546 null",
            "null 3:c:-52486579 null null",
            "null null 2:b:1564841655 4:d:158885860 null null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
    map.insert("8","h");
    map.insert("7","g");
    map.remove("1");
    map.remove("3");
    map.remove("5");
    String[] expected2 = new String[]{
            "8:h:-1416371156",
            "4:d:158885860 null",
            "2:b:1564841655 7:g:1951109561 null null",
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());
  }

  //PUT TESTS

  @Test
  @DisplayName("Put does not change structure of treap")
  public void putDoesNotChangeTreapStructure() {
    Map<String,String> map = createMap(50);
    map.insert("2","b");
    map.insert("1","a");
    map.insert("3","c");
    String[] expected1 = new String[] {
            "1:a:-1727040520",
            "null 3:c:-1657178909",
            "null null 2:b:-1160871061 null"
    };
    assertEquals((String.join("\n", expected1) + "\n"), map.toString());
    map.put("3","aa");
    map.put("2","dd");
    String[] expected2 = new String[] {
            "1:a:-1727040520",
            "null 3:aa:-1657178909",
            "null null 2:dd:-1160871061 null"
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());
    assertEquals(3,map.size());
  }

  //GET TESTS
  @Test
  @DisplayName("Get throws error when trying to get value of key not in map")
  public void getKeyNotMapped() {
    try {
      map.get("1");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Get does not change structure of treap")
  public void getDoesNotChangeTreapStructure() {
    Map<String,String> map = createMap(50);
    map.insert("2","b");
    map.insert("1","a");
    map.insert("3","c");
    String[] expected1 = new String[] {
            "1:a:-1727040520",
            "null 3:c:-1657178909",
            "null null 2:b:-1160871061 null"
    };
    assertEquals((String.join("\n", expected1) + "\n"), map.toString());
    assertEquals("c",map.get("3"));
    assertEquals("b",map.get("2"));
    String[] expected2 = new String[] {
            "1:a:-1727040520",
            "null 3:c:-1657178909",
            "null null 2:b:-1160871061 null"
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());
    assertEquals(3,map.size());
  }

  //HAS (find) TESTS

  @Test
  @DisplayName("has returns true when key is mapped")
  public void hasTrueWhenKeyMapped() {
    map.insert("2","b");
    map.insert("1","a");
    map.insert("3","c");
    assertTrue(map.has("1"));
    assertTrue(map.has("2"));
    assertTrue(map.has("3"));
  }

  @Test
  @DisplayName("has returns false for empty tree")
  public void hasFalseForEmptyTree() {
    assertFalse(map.has("1"));
  }

  @Test
  @DisplayName("has returns false when key is not mapped")
  public void hasFalseWhenKeyNotMapped() {
    map.insert("2","b");
    assertFalse(map.has("3"));
    assertFalse(map.has("1"));
  }

  //iterator test
  @Test
  @DisplayName("iterator returns values in order")
  public void iteratorPerformsInOrderTraversal() {
    map.insert("4","d");
    map.insert("2", "b");
    map.insert("5","e");
    map.insert("1","a");
    map.insert("3","c");
    int count = 1;
    for (String key : map) {
      String expected = "" + count;
      assertEquals(expected,key);
      count++;
    }
  }
}
package hw6;

import hw6.bst.AvlTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to AVL Tree.
 */
@SuppressWarnings("All")
public class AvlTreeMapTest extends BinarySearchTreeMapTest {

  @Override
  protected Map<String, String> createMap() {
    return new AvlTreeMap<>();
  }

  //INSERT TESTS
  @Test
  @DisplayName("Insert one element and tree should rotate left to keep balance.")
  public void insertLeftRotation() {
    map.insert("1", "a");
    //System.out.println(avl.toString());
    // must print
    /*
        1:a
     */

    map.insert("2", "b");
    //System.out.println(avl.toString());
    // must print
    /*
        1:a,
        null 2:b
     */

    map.insert("3", "c"); // it must do a left rotation here!
    //System.out.println(avl.toString());
    // must print
    /*
        2:b,
        1:a 3:c
     */

    String[] expected = new String[]{
        "2:b",
        "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  // TODO Add more tests

  @Test
  @DisplayName("Insert one element and tree should rotate right to keep balance.")
  public void insertRightRotation() {
    map.insert("3","c");
    map.insert("2","b");
    map.insert("1","a");
    String[] expected = new String[] {
     "2:b",
     "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert one element and tree should rotate double right-left to keep balance.")
  public void insertRightLeftRotation() {
    map.insert("2","b");
    map.insert("4","d");
    map.insert("3","c");
    String[] expected = new String[] {
      "3:c",
      "2:b 4:d"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert one element and tree should rotate double left-right to keep balance.")
  public void insertLeftRightRotation() {
    map.insert("3","c");
    map.insert("1","a");
    map.insert("2","b");
    String[] expected = new String[] {
      "2:b",
      "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("After insertion tree should not change structure as it is still balanced after")
  public void insertStructureUnchanged() {
    map.insert("4","d");
    map.insert("2", "b");
    map.insert("6","f");
    map.insert("1","a");
    String[] expected1 = new String[] {
            "4:d",
            "2:b 6:f",
            "1:a null null null"
    };
    assertEquals((String.join("\n", expected1) + "\n"), map.toString());
    map.insert("3","c");
    String[] expected2 = new String[] {
            "4:d",
            "2:b 6:f",
            "1:a 3:c null null"
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert many elements and tree should maintain height balance property and BST order property.")
  public void insertManyTimesStillOrdered() {
    map.insert("3","c");
    map.insert("1","a");
    map.insert("2","b");
    map.insert("7", "g");
    map.insert("4","d");
    map.insert("9","i");
    map.insert("6","f");
    String[] expected = new String[] {
            "4:d",
            "2:b 7:g",
            "1:a 3:c 6:f 9:i"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Size updates each time a new key-value pair is inserted.")
  public void sizeUpdatesWhenInserting() {
    map.insert("3","c");
    map.insert("1","a");
    map.insert("2","b");
    map.insert("7", "g");
    map.insert("4","d");
    map.insert("9","i");
    map.insert("6","f");
    assertEquals(7,map.size());
  }

  //REMOVE TESTS

  @Test
  @DisplayName("Remove one element and tree should rotate right to keep balance.")
  public void removeRightRotation() {
    map.insert("3","c");
    map.insert("2","b");
    map.insert("4","d");
    map.insert("1","a");
    map.remove("4");
    String[] expected = new String[] {
      "2:b",
      "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove one element and tree should rotate left to keep balance")
  public void removeLeftRotation() {
    map.insert("2","b");
    map.insert("3","c");
    map.insert("1","a");
    map.insert("4","d");
    map.remove("1");
    String[] expected = new String[] {
            "3:c",
            "2:b 4:d"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove one element and tree should rotate double right-left to keep balance")
  public void removeRightLeft() {
    map.insert("4","d");
    map.insert("2","b");
    map.insert("7","g");
    map.insert("6","f");
    map.remove("2");
    String[] expected = new String[] {
            "6:f",
            "4:d 7:g"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove one element and tree should rotate double left-right to keep balance")
  public void removeLeftRight() {
    map.insert("6","f");
    map.insert("4","d");
    map.insert("8","h");
    map.insert("5","e");
    map.remove("8");
    String[] expected = new String[] {
            "5:e",
            "4:d 6:f"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove one element and tree should not change structure since it is still balanced")
  public void removeStructureUnchanged() {
    map.insert("4","d");
    map.insert("2", "b");
    map.insert("6","f");
    map.insert("1","a");
    map.insert("3","c");
    String[] expected1 = new String[] {
            "4:d",
            "2:b 6:f",
            "1:a 3:c null null"
    };
    assertEquals((String.join("\n", expected1) + "\n"), map.toString());
    map.remove("2");
    String[] expected2 = new String[] {
            "4:d",
            "1:a 6:f",
            "null 3:c null null"
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Size of AVL tree is updated each time remove is called")
  public void removeReducesSize() {
    map.insert("4","d");
    map.insert("2", "b");
    map.insert("6","f");
    map.insert("1","a");
    map.insert("3","c");
    map.remove("3");
    assertEquals(4,map.size());
    map.remove("1");
    assertEquals(3,map.size());
  }

  @Test
  @DisplayName("Remove key from Avl tree of only that key (a leaf) works")
  public void removeFromAVLSizeOfOne() {
    map.insert("1","a");
    map.remove("1");
    assertEquals(0,map.size());
  }

  @Test
  @DisplayName("Remove target with one child works")
  public void removeTargetWithOneChild() {
    map.insert("4","d");
    map.insert("5","e");
    map.insert("7","g");
    map.insert("9","i");
    map.remove("7");
    String[] expected = new String[] {
            "5:e",
            "4:d 9:i",
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove target with two children works")
  public void removeTargetWithTwoChildren() {
    map.insert("4","d");
    map.insert("5","e");
    map.insert("7","g");
    map.insert("9","i");
    map.insert("6","f");
    map.remove("7");
    String[] expected = new String[] {
            "5:e",
            "4:d 6:f",
            "null null null 9:i"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Removing key from empty AVL Tree throws error")
  public void removeFromEmptyAVLTreeThrowsError() {
    try {
      map.remove("1");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Removing root updates root while keeping tree in order")
  public void removeRoot() {
    map.insert("3","c");
    map.insert("2","b");
    map.insert("4","d");
    map.remove("3");
    assertEquals(2,map.size());
    String[] expected = new String[] {
            "2:b",
            "null 4:d",
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("inserting and removing many elements maintains properties of AVL - test 1")
  public void insertAndRemoveMultipleElementsTest1() {
    map.insert("5","e");
    map.insert("1","a");
    map.insert("2","b");
    map.insert("4","d");
    map.insert("6","f");
    map.insert("8","h");
    map.insert("3","c");
    map.remove("2");
    map.remove("5");
    assertEquals(5,map.size());
    String[] expected = new String[] {
            "4:d",
            "3:c 6:f",
            "1:a null null 8:h"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("inserting and removing many elemenents maintains properties of AVL - test 2")
  public void insertAndRemoveMultipleElementsTest2() {
    map.insert("7","g");
    map.insert("4","d");
    map.insert("2","b");
    map.insert("9","i");
    map.insert("8","h");
    map.insert("6","f");
    String[] expected1 = new String[] {
            "7:g",
            "4:d 8:h",
            "2:b 6:f null 9:i"
    };
    assertEquals((String.join("\n", expected1) + "\n"), map.toString());
    map.remove("8");
    map.insert("3","c");
    map.remove("7");
    map.remove("2");
    map.insert("8","h");
    map.insert("7","g");
    map.insert("5","e");
    map.remove("8");
    map.remove("9");
    String[] expected2 = new String[] {
            "6:f",
            "4:d 7:g",
            "3:c 5:e null null"
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());
  }

  //PUT TESTS

  @Test
  @DisplayName("Put does not change structure of AVL tree")
  public void putDoesNotChangeAVLTreeStructure() {
    map.insert("2","b");
    map.insert("1","a");
    map.insert("3","c");
    String[] expected1 = new String[] {
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected1) + "\n"), map.toString());
    map.put("3","aa");
    map.put("2","dd");
    String[] expected2 = new String[] {
            "2:dd",
            "1:a 3:aa"
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
  @DisplayName("Get does not change structure of AVL tree")
  public void getDoesNotChangeAVLTreeStructure() {
    map.insert("2","b");
    map.insert("1","a");
    map.insert("3","c");
    String[] expected1 = new String[] {
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected1) + "\n"), map.toString());
    assertEquals("c",map.get("3"));
    assertEquals("b",map.get("2"));
    String[] expected2 = new String[] {
            "2:b",
            "1:a 3:c"
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
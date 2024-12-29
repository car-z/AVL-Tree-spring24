# Discussion

## Unit testing TreapMap
The main difficulty in testing treap maps was the need to create different test cases with different random seeds to be able to test multiple
different potential scenarios for the treap. I would need to sort through different seeds and choose seeds which had orders of priorities which would 
let me test what I needed to test, which meant that I had to build many treaps to understand which seeds achieved what I wanted them to achieve. 
For example, I used seed 60 to test removeRightRotation as with this seed, as with seed 60 I was able to build a simple tree where I could delete an element with just one right rotation, which I was unit testing
for. This is what the treap looks like for the test case in removeRightRotation:

1    -- insert(2) -->    1     -- insert(3) -->     1     -- remove(3) --> 1
                           \                         \                      \           
                            2                          3                     2                                
                                                      /                           
                                                     2                                 
                                                                                                       
                                                                                                           
As shown, if I remove 3 from the tree of 1,2,3 (inserted in order), the tree only needs to rotate right once to remove 3 and maintain the min heap property, which is what I am testing.

Another test case that I used was with random seed 50 in insertMultipleTest1. I inserted the keys in order to also test that the tree still became balanced in the average case (and not linear, like it would with a BST).
It was difficult figuring out what the expected output should be for each of my test cases as I would have to draw the ASCII trees like so each time to trace out each step and rotation. Here is the process for insertMultipleTest1:

1    -- insert(2) -->   2   -- insert(3) -->    2     -- insert(4) --> 2       -- insert(5) -->    2
                       /                       / \                    / \                         / \
                      1                       1   3                  1   3                       1   3
                                                                          \                           \
                                                                           4                           5
                                                                                                      /
                                                                                                     4

Particular challenges also came up when designing test cases as not only did I have to pay attention to what seeds created which priorities in which order, but also which elements to insert in what order (so they corresponded with the desired priorities) 
to create trees that I could test on. For example, another test case that I created was removeStructureUnchanged, where I had to create a tree which I could remove an element from while preserving its structure because the removal did not affect the min heap ordering property. This
proved to be particularly challenging as I had to design a test case where the node to be removed had children (because removing a leaf would never affect the structure, thus defeating the purpose of the test) while maintaining the min heap order property before and after removal without 
performing structural rotation. I ultimately devised the following test case with seed 60 as shown by this ASCII tree:

start by inserting 2
2    -- insert(1) -->   2   -- insert(3) -->    2     -- insert(4) -->      2       -- remove(3) -->    2
                       /                       / \                         / \                         / \
                      1                       1   3                       1   3                       1   4
                                                                               \
                                                                                4

## Benchmarking
When analyzing the longer files - "Pride and Prejudice" and "Moby Dick" - the differences between the different
implementations of a map was most apparent. The array map implementation performed significantly worse than the other three
implementations for both of these texts. For example, for Moby Dick the array map had an average of 2561.278 ms/op whereas the other
three operations were all within the 95 - 130 ms/op range. This result makes sense as the experiment involves lots of search operations: the map needs to be searched to see if a
certain word already exists in the map, the frequency of the word needs to be updated if it does exist (which involves locating the 
word in the map again), and the word needs to be added to the map if it doesn't already exist in it (which may involve finding the correct location of the word, again).
The time complexity for linear structures like arrays is O(n), and thus it will take the arrayMap implementation a longer time per operation compared to the other implementations when the input gets very large. 
The other three implementations are non-linear and thus have the potential to have time complexities which are better than O(n), and thus their average time per operation is significantly less than arrayMap's for large inputs.
The treap implementation tends to perform slightly worse than the BST and AVL Tree implementations. For example, for Pride and Prejudice the treap took an average of 64.924 ms/op whereas the avl tree and BST took 53.867 ms/op and 52.418 ms/op respectively. 
This is because the treap does not guarantee a balanced BST, and thus a treap does not guarantee that the maximum height is O(lg n) like an AVL tree does. For the worst case of a treap, its height can be linear (like an array). 
Although a BST does not guarantee O(lg n) height either, it tends to be faster than treap because a BST does not spend time during its operations rotating the structure. In all four
text files, the AVL tree was one of the fastest operations. This is because the AVL tree guarantees O(lg n) height and therefore the worst case time complexity for an AVL tree is upper-bounded at O(lg n), and not O(n) like the treap, BST, and array. However,
the BST tree has a score (ms/op) that is comparable to AVL tree and in many trials even outperformed AVL tree by a marginal amount. For example, for Hotel California the AVL tree and BST implementations were the two fastest implementations but BST was 0.001 ms faster 
than the AVL tree per operation. This is again due to the fact that the AVL tree operations must rotate the tree, which uses time that the BST does not need to expend. 
These relative relationships between the four implementations hold true for all four provided texts, but the differences are less drastic in shorter text files as there's not enough words (not a large enough input) to greatly exacerbate the differences in 
search time for each implementation. For example for the Federalist, the array map implementation was still the slowest of all four implementations, but only by one millisecond (1.562 ms/op for the array map vs 0.563 ms/op for the BST).

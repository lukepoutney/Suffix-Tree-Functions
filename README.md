# Suffix-Tree-Functions

In this project string and text algorithms were devleoped to:
- check for the presence of a given string in a text
- check for the number of occurrences of a string in a given text
- check for the longest repeated substring between 2 texts
- check for the longest common substring between 2 texts

Below is the implementation report that I created to show I have an understanding of the algorithms which i coded, as this project was an assessed exercise for an algorithmics course:

## Implementation Report:

##Task 1:
To check for the presence of a given string in a text, my implementation moves along matching edges until the entire string is found or a given character does not match.
I do this by repeatedly searching for an edge which has a left label that refers to the same character as the next one to be found in my string (to begin this would be the first character). If unsuccessful in finding a match the method terminates stating the string is not present; if successful and all the strings have been matched the method terminates successfully; otherwise we now check that the rest of the edge matches before moving along it (in this program edge labels are stored at the child node which the edge leads to). 
So, if the edge label has multiple characters, we now check each of these sequentially. As with matching the left edge label, we terminate the program unsuccessfully if the edge label produces a mismatch at any point, and we terminate successfully if we have matched all the characters. Should we successfully match the right edge label with more of the string still to be found, we repeat the process, so trying to find an edge with a left edge label representing the next character to be found and so on.
This implementation works because if the string is in the text the suffix tree there will be a set of edges (and potentially part of an edge at the end) that can be combined together to return the given string, as every suffix of the text as a whole is added to the tree. From the node below the last edge used to find the string, all leaf nodes are occurrences of the string, which we look at in task 2. To aid us in this we store the node that the final edge arrives at as our match node.

##Task 2:
To check for the number of occurrences of a string in a given text, we make use of the function from task 1 to find a given string in a text, and begin searching for the total number of occurrences from the node beneath the edge at which we found our successful match. This means all leaf nodes below this node are occurrences of the string itself, with the suffix value they store indicating the position in the text each occurrence began.
To implement this, I make use of recursion to traverse the tree from left to right. During this traversal it notes the suffix of leaf nodes when they occur, and goes by the rule that it must first go down where possible until there are no nodes left, at which point it  will attempt to go across by checking for a possible sibling. A few things must be done to make this possible however, as we cannot start this recursion from the match node because its sibling’s and the sibling’s subsequent children are not nodes that we wish for the algorithm to check. Instead we check the initial node individually to see if it is a suffix indicating a single occurrence, or if it is null indicating the string is not present at all. Otherwise we make use of the recursive function, using the match node’s child as our starting point. Our return value is a list of all the starting points of the string, which we can get the length of to know how many times our string appears. 

##Task 3:
To check for the longest repeated substring we must search for the deepest branch node in the tree with at least 2 children (deepest node being defined as the one with the largest sum of branch node weights to reach it from the root). The node must be a branch and have greater than 2 children as this means the node is repeated. Each leaf node has a suffix which relates to the position in the text that each string began, with more than 1 of these meaning the string begins in 2 different locations and as such making it repeated (the same reasons as explained in task 2).
To implement this we also use recursion, to traverse the tree in a similar manner as done in task 2, however this time we are covering the entire suffix tree and not just a section. A depth variable is used to keep track of the depth of a given node. When we reach a branch node with multiple children, we check its depth against the current maximum and if the new depth is larger, we make it the new maximum. Every time we update the maximum depth we also record the positions of 2 of its children, as the program provides the user with positions of the repetition. We can use either of these occurrences and the length of the repeated string to print the longest repeated string to the user.

##Task 4:
To check for the longest common substring, we must first build a generalised suffix tree given 2 texts. This is done by concatenating the 2 texts into a single string with a # symbol between them (or any character which does not appear in either of the texts or match our termination character $). From this new combined string we build our suffix tree in the same way as done in the other 3 tasks.
Checking for the longest common substring is similar to checking for the longest repeated substring, however this time a branch node is only a valid candidate if a node has children from both texts. The implementation is nearly identical to finding the longest repeated substring, with the only difference being additional validation to check for what we just mentioned. This validation is done through use of 2 Boolean variables that tell us if each text has a certain substring. We also distinguish between what is from text 1 and what is from text 2 using a split point (the point at which # occurs in the combined string of texts) along with the left edge labels of the edges leading to a node’s children. If a left edge label is before or equal to the split point, we know the string is in text1, and after means it is in text 2. When we detect these cases, we set their respective Boolean values to true. If both values return true for a node, we can consider this parent node to represent the end of a common substring, meaning we can compare its depth with the maximum. If our new depth is bigger we can replace the maximum with it, along with occurrences at which the common substring occurs in each text.

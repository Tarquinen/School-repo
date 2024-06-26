/**
 * Daniel Smolsky
 * Programming Project 3 - Binary Search Tree
 * March 15, 2024
 * The Tree class represents a binary tree data structure. It provides methods for creating a balanced binary search tree from a list of integers, 
 * constructing a tree from a string representation, and performing various operations on the tree such as checking if it is a binary search tree, 
 * checking if it is balanced, and printing the tree.
 */

import java.util.Stack;
import java.util.ArrayList;

public class Tree {
   protected ArrayList<Integer> inorderList = new ArrayList<>(); // used to check if the tree is a binary search tree
   protected TreeNode root;
   protected int size = 0;

   public static class TreeNode {
      protected int element;
      protected int branch; // used for printing the tree
      protected TreeNode left;
      protected TreeNode right;

      public TreeNode(int e) {
         element = e;
      }
   }

   // constructor for creating a balanced binary search tree from a list of integers
   public Tree(ArrayList<Integer> list) {
      list.sort(null);
      createTreeFromList(list, 0, list.size() - 1, 0);
   }
   
   // constructor for creating a tree from a string
   public Tree (String s) throws InvalidInputSyntax {
      validateString(s); // check the string isn't missing parenthesis and only has integers
      
      // convert the string to an array of tokens
      String[] tokens = parseStringToArray(s);
      
      // create a list of branches in the tree with each branch containing a list of integers
      int branch = 0;
      ArrayList<ArrayList<Integer>> branchList = new ArrayList<>();
      
      for (String token : tokens) {
         // increase the tree branch for each left parenthesis and * symbol
         if (token.equals("(")|| token.equals("*")) {
            branch++;
         }
         else if (token.equals(")")) {
            branch--;
         }
         
         // if there are not enough branches in the list, add a new branch
         if (branchList.size() < branch) {
            branchList.add(new ArrayList<Integer>());
         }
         
         // add the token to the list at the current branch 
         if (token.equals("*")) {
            branchList.get(branch - 1).add(null);
            branch--; // decrease back down to the parent branch 
         }
         else if (toNumber(token) != null) {
            branchList.get(branch - 1).add(toNumber(token));
         }
      }

      validateList(branchList); // check the list has the correct number of nodes in each branch
      createTreeFromList(branchList, 0, 0); // create the tree from the list starting at the root
      inorder(root); // create the inorder list used to check if the tree is a binary search tree
   }
   
   // create the tree from the list of integers recursively, used in the integer list constructor
   private TreeNode createTreeFromList(ArrayList<Integer> list, int start, int end, int branch) {
      if (start > end) return null;
      int mid = (start + end) / 2;
      TreeNode node = createNewNode(list.get(mid));
      node.branch = branch;
      if (root == null) root = node;
      
      // recursively create a left subtree 
      node.left = createTreeFromList(list, start, mid - 1, branch + 1);
      // recursively create a right subtree
      node.right = createTreeFromList(list, mid + 1, end, branch + 1);

      return node;
   }

   // create the tree from the list of branches representation recursively, used in the string constructor
   private TreeNode createTreeFromList(ArrayList<ArrayList<Integer>> list, int currentBranch, int currentIndex) {
      // create a new node with the current branch and index
      TreeNode node = createNewNode(list.get(currentBranch).get(currentIndex));
      node.branch = currentBranch;
      if (root == null) root = node;

      // count the number of nulls before current in the branch 
      int nullCount = 0;
      for (int i = 0; i < currentIndex; i++)
         if (list.get(currentBranch).get(i) == null) nullCount ++; 

      currentIndex -= nullCount; // ignore the nulls in the current branch when calculating the child indexs because null nodes don't have children
      int childBranch = currentBranch + 1;
      int leftChildIndex = currentIndex * 2;
      int rightChildIndex = leftChildIndex + 1;

      // check if the left child exists and is not null
      if (hasChild(list, childBranch, leftChildIndex)) 
         // recursively create a left subtree 
         node.left = createTreeFromList(list, childBranch, leftChildIndex);
      
      // check if the right child exists and is not null
      if (hasChild(list, childBranch, rightChildIndex))
         // recursively create a right subtree
         node.right = createTreeFromList(list, childBranch, rightChildIndex);

      return node;
   }

   // check if an element exists in a branch and index
   private boolean hasChild(ArrayList<ArrayList<Integer>> list, int branch, int index) {
      return list.size() > branch && list.get(branch).size() > index && list.get(branch).get(index) != null;
   }

   // check if the tree is a binary search tree
   public boolean isBinarySearchTree() {
      for (int i = 0; i < inorderList.size() - 1; i++) {
         if (inorderList.get(i) > inorderList.get(i + 1)) {
            return false;
         }
      }
      return true;
   }

   // helper method for checking if the tree is balanced
   public boolean isBalanced() {
      return isBalanced(root);
   }

   // check if the tree is balanced recursively
   protected boolean isBalanced(TreeNode node) {
      if (node == null) return true;
      int leftHeight = getHeight(node.left);
      int rightHeight = getHeight(node.right);

      if (Math.abs(leftHeight - rightHeight) > 1) return false;
      return isBalanced(node.left) && isBalanced(node.right);
   }

   // helper method for getting the height of the tree
   public int getHeight() {
      return getHeight(root);
   }

   // get the height of the tree recursively
   protected int getHeight(TreeNode node) {
      if (node == null)
         return -1;
      return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
   }
   
   // create the inorder list of the tree used to check if the tree is a binary search tree
   protected void inorder(TreeNode node) {
      if (node == null) return;
      inorder(node.left);
      inorderList.add(node.element);
      inorder(node.right);
   }

   // helper method for printing the tree
   public void printTree() {
      printTree(root);
   }

   //prints the visual representation of the tree
   protected void printTree(TreeNode node) {
      if (node == null) return;
      for (int i = 0; i < node.branch + 1; i++) { // apply the correct number of spaces for the branch
         System.out.print("   ");
      }
      System.out.println(node.element);
      printTree(node.left);
      printTree(node.right);
   }

   // return an array list of the values in the tree
   public ArrayList<Integer> getInorderList() {
      return inorderList;
   }

   // convert a string to an integer, or return null if the string is not an integer, used in constructor
   private Integer toNumber(String s) {
      try {
         return Integer.parseInt(s);
      }
      catch (NumberFormatException e) {
         return null;
      }
   }

   // convert a string to an array of tokens
   private String[] parseStringToArray(String s) {
      StringBuilder sb = new StringBuilder();
      ArrayList<String> result = new ArrayList<>();
      
      for (int i = 0; i < s.length(); i++) {
         char c = s.charAt(i);
         if (c == ' ') {
            if (sb.length() > 0) {
               result.add(sb.toString());
               sb.setLength(0);
            }
         } 
         else if (c == '(' || c == ')' || c == '*') {
            if (sb.length() > 0) {
               result.add(sb.toString());
               sb.setLength(0);
            }
            result.add(String.valueOf(c));
         } 
         else {
            sb.append(c);
         }
      }
      
      if (sb.length() > 0) {
         result.add(sb.toString());
      }
      
      return result.toArray(new String[0]);
   }
   
   // used in the constructor
   private void validateString(String s) throws InvalidInputSyntax {
      Stack<Character> stack = new Stack<>();
      for (int i = 0; i < s.length(); i++) {
         char c = s.charAt(i);
         // track the parenthesis in a stack
         if (c == '(') {
            stack.push(c);
         }
         else if (c == ')') {
            if (stack.isEmpty()) {
               throw new InvalidInputSyntax("Missing Left Parenthesis");
            }
            stack.pop();
         }
         // check if the data is an integer
         else if (!Character.isDigit(c) && c != '*' && c != ' ') {
            throw new InvalidInputSyntax("Data is Not an Integer");
         }
      }
      // if the stack is not empty after iterating through the string, then there is a missing parenthesis
      if (!stack.isEmpty()) {
         if (stack.pop() == ')') {
            throw new InvalidInputSyntax("Missing Left Parenthesis");
         }
         else {
            throw new InvalidInputSyntax("Missing Right Parenthesis");
         }
      }
   }

   // used in the constructor
   private void validateList(ArrayList<ArrayList<Integer>> list) throws InvalidInputSyntax {
      if (list.get(0).size() != 1)
         throw new InvalidInputSyntax("Root branch should have 1 Node");
   
      for (int i = 0; i < list.size() - 1; i++) {
         // get the num of elements in the branch that are not null
         int notNull = 0;
         for (int j = 0; j < list.get(i).size(); j++) 
            if (list.get(i).get(j) != null) notNull ++; 
   
         // check the next branch has twice as many elements
         if (notNull * 2 > list.get(i + 1).size()) {
            throw new InvalidInputSyntax("Branch " + (i + 1) + " is missing nodes");
         }
         else if (notNull * 2 < list.get(i + 1).size()) {
            throw new InvalidInputSyntax("Branch " + (i + 1) + " has extra nodes");
         }
      }
   }

   // used to create a new node
   protected TreeNode createNewNode(int e) {
      return new TreeNode(e);
   }
}



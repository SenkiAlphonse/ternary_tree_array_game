package com.company;

import java.util.*;

public class Solution {

    public static boolean canWin(int leap, int[] game) {

        if(leap>=game.length || game.length == 1){
            return true;
        }
        else if (!Arrays.stream(game).anyMatch(x -> x==1)) {
            return true;
        }
        else if (leap <= 1) {
            return false;
        }

        GameTree gt = new GameTree();
        gt.insert(0, game, leap);
        for ( int i = 1; i < game.length; i++) {
            if (game[i] == 0 ) {
                gt.insert(i, game, leap);
            }
        }

        return gt.containsNodeNotSmallerThan(game.length-leap);

    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int q = scan.nextInt();
        while (q-- > 0) {
            int n = scan.nextInt();
            int leap = scan.nextInt();

            int[] game = new int[n];
            for (int i = 0; i < n; i++) {
                game[i] = scan.nextInt();
            }

            System.out.println( (canWin(leap, game)) ? "YES" : "NO" );
        }
        scan.close();
    }
}

class Node {
    int value;
    int subtree;
    Node left;
    Node leap;
    Node right;

    Node(int value) {
        this.value = value;
        right = null;
        leap = null;
        left = null;
        subtree = 0;
    }
}

class GameTree {

    private Node root;

    public void insert(int newValue, int[] game, int leap) {
        // create new root if tree is empty
        if (root == null) {
            root = new Node(newValue);
        } else {
            insertRecursive(root, new Node(newValue), game, leap);
        }
    }

    // insertRecursive node into the root (or subroot) of the ternary tree
    private void insertRecursive(Node root, Node newNode, int[] game, int leap) {
        if (newNode == null) {
            return;
        }
        if (root == null) {
            return;
        }
        // determine which node the new node should be a child of, insertRecursive it in that node recursively
        if (newNode.value == root.value-1) {
            if (root.left == null) {
                incrementSubtree(root);
            root.left = newNode;
            }
        }
        else if (newNode.value == root.value+1) {
            if (root.right == null) {
                incrementSubtree(root);
                root.right = newNode;
            }
        }
        else if (leap > 1 && newNode.value == root.value + leap){
            if (root.leap == null) {
                incrementSubtree(root);
                root.leap = newNode;

                addZerosToTheLeft(root.leap, newNode, game, leap);
            }
        }
        else {
            insertRecursive(root.left, newNode, game, leap);
            insertRecursive(root.leap, newNode, game, leap);
            insertRecursive(root.right, newNode, game, leap);
        }
    }

    private void addZerosToTheLeft(Node root, Node newNode, int[] game, int leap) {
        int i = newNode.value-1;
        while (game[i] == 0 && i > 0 ) {
            insertRecursive(root, new Node(i), game, leap);
            i--;
        }
    }

    private void incrementSubtree(Node n) {
        n.subtree = n.subtree + 1;
    }

    public boolean containsNodeNotSmallerThan(int value) {
        return containsNodeNSTRecursive(root, value);
    }

    private boolean containsNodeNSTRecursive(Node current, int value) {
        if (current == null) {
            return false;
        }

        if (value <= current.value) {
            return true;
        }
        
        return (containsNodeNSTRecursive(current.left, value) || containsNodeNSTRecursive(current.right, value) || containsNodeNSTRecursive(current.leap, value));
    }
}


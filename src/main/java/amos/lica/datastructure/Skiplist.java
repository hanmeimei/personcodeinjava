package amos.lica.datastructure;

import java.util.Random;

class Skiplist {

    static int MAX_HEIGHT= 4;

    static class Node {
        int height;
        Node[] posts = new Node[MAX_HEIGHT];
        int key;
    }

    Node sentinel;

    public Skiplist() {
        sentinel = new Node();
        sentinel.height = MAX_HEIGHT;
    }

    public boolean search(int target) {
        int curHeight = sentinel.height - 1;
        Node curNode = sentinel;
        while(curHeight >= 0) {
            if (curNode.key == target && curNode != sentinel) {
                return true;
            }
            if (curNode.posts[curHeight] != null && curNode.posts[curHeight].key <= target) {
                curNode = curNode.posts[curHeight];
            } else {
                curHeight--;
            }
        }
        return false;
    }

    public void add(int num) {
        int targetHeight = 0;
        Random random = new Random();
        do {
            targetHeight++;
        } while(random.nextInt(2) == 1 && targetHeight < MAX_HEIGHT);

        Node[] path = new Node[targetHeight];
        int curHeight = sentinel.height - 1;
        Node curNode = sentinel;

        while(curHeight >= 0) {
            if (curNode.posts[curHeight] != null && curNode.posts[curHeight].key < num) {
                curNode = curNode.posts[curHeight];
            } else {
                if (curHeight <= targetHeight -1) {
                    path[curHeight] = curNode;
                }
                curHeight--;
            }
        }
        Node insert = new Node();
        insert.height = targetHeight;
        insert.key = num;
        for (int i = 0; i < targetHeight; i++) {
            insert.posts[i] = path[i].posts[i];
            path[i].posts[i] = insert;
        }
    }

    public boolean erase(int target) {
        int curHeight = sentinel.height - 1;
        Node curNode = sentinel;
        Node[] path = new Node[sentinel.height];
        Node pre = null;
        boolean found = false;
        while(curHeight >= 0) {
            if (curNode.posts[curHeight] != null && curNode.posts[curHeight].key < target) {
                curNode = curNode.posts[curHeight];
            } else {
                path[curHeight] = curNode;
                curHeight--;
            }
        }
        if (curNode.posts[0] != null && curNode.posts[0].key == target) {
            Node t = curNode.posts[0];
            int targetHeight = t.height;
            for(int i = 0; i < targetHeight; i++) {
                path[i].posts[i] = t.posts[i];
                t.posts[i] = null;
            }
            found = true;
        }
        return found;
    }
}

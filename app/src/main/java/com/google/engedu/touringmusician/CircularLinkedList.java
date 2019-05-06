/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;

import android.graphics.Point;
import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

        Node(Point mPoint){
           this.point = mPoint;
        }
        //insert new node before passed node
        Node(Point mPoint, Node mNode){
            this.point = mPoint;

            mNode.prev.next = this;
            this.prev = mNode.prev;

            this.next = mNode;
            mNode.prev = this;
        }
        //insert new node after passed node
        Node(Node mNode, Point mPoint){
            this.point = mPoint;

            mNode.next.prev = this;
            this.next = mNode.next;

            this.prev = mNode;
            mNode.next = this;
        }
        void setPrev(Node mPrev){
            this.prev = mPrev;
        }
        void setNext(Node mNext){
            this.next = mNext;
        }
    }

    Node head;

    //insertBeginning: Add a point at the beginning of the list
    public void insertBeginning(Point p) {
        if(head != null){
            Node newHead = new Node(p, head);
            head = newHead;
        }
        else {
            head = new Node(p);
            head.setPrev(head);
            head.setNext(head);
        }
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }
    private float differenceDistanceBetweenThree(Point A, Point X, Point B) {
        float distance = distanceBetween(A, X) + distanceBetween(X, B);
        float difference = distanceBetween(A, B);

        return distance - difference;
    }

    //totalDistance: Measures the length of the tour
    public float totalDistance() {
        float total = 0;

        Node currentA = head;
        Node currentB = head.next;

        do{
            total += distanceBetween(currentA.point, currentB.point);
            currentA = currentA.next;
            currentB = currentB.next;
        }while(currentB.point != head.point);


        return total;
    }

    //insertNearest: Add a point after the Node that it is closest to
    public void insertNearest(Point p) {
        if(head == null){
            insertBeginning(p);
        }
        else {
            float closestDistance = distanceBetween(p, head.point);
            Node closestNode = head;

            Node current = head;

            do {
                if (closestDistance > distanceBetween(p, current.point)) {
                    closestDistance = distanceBetween(p, current.point);
                    closestNode = current;
                }
                current = current.next;
            } while (current.point != head.point);

            if (distanceBetween(p, closestNode.next.point) < distanceBetween(p, closestNode.prev.point)) {
                new Node(closestNode, p);
            } else {
                new Node(p, closestNode);
            }
        }
    }

    //insertSmallest: Add a point to the list in the location that minimizes the overall tour length
    public void insertSmallest(Point p) {
        if(head == null){
            insertBeginning(p);
        }
        else {
            Node insertAfter = head;
            float smallestDist = differenceDistanceBetweenThree(head.point, p, head.next.point);

            Node currentNode = head;
            float currentDist;
            do{
                currentDist = differenceDistanceBetweenThree(currentNode.point, p, currentNode.next.point);

                System.out.println("c" + currentDist);
                System.out.println("s" + smallestDist);

                if(Float.compare(currentDist, smallestDist) < 0){
                    smallestDist = currentDist;
                    insertAfter = currentNode;
                }
                currentNode = currentNode.next;
            }while(currentNode != head);

            System.out.println("the smallest is" + smallestDist);

            new Node(insertAfter, p);
        }
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}

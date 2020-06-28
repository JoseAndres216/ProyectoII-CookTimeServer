package Logic.DataStructures.BinarySearchTree;

public class BST<T extends Comparable<T>> {

    private Node<T> root;

    public void insert(T newData) {
        Node<T> newNode = new Node<>(newData);
        //Caso en que el arbol esta vacio
        if (root == null) {
            root = newNode;
            return;
        }
        Node<T> temp = root;
        boolean inserted = false;

        while (!inserted) {
            if (temp.getData().compareTo(newData) == 0) {
                throw new IllegalArgumentException("Repeated node, cannot insert");

            } else if (temp.getData().compareTo(newData) < 0) {
                if (temp.getRight() != null) {
                    temp = temp.getRight();
                } else {
                    temp.setRight(newNode);
                    inserted = true;
                }
            } else if (temp.getData().compareTo(newData) > 0) {
                if (temp.getLeft() != null) {
                    temp = temp.getLeft();
                } else {
                    temp.setLeft(newNode);
                    inserted = true;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "BST{" +
                "root=" + root +
                '}';
    }

    public void delete(T data) {
        Node<T> parent = root;
        Node<T> current = root;
        boolean isLeftChild = false;
        while (current.getData().compareTo(data) != 0) {
            parent = current;
            if (current.getData().compareTo(data) > 0) {
                isLeftChild = true;
                current = current.getLeft();
            } else {
                isLeftChild = false;
                current = current.getRight();
            }
            if (current == null) {
                throw new IllegalArgumentException("The node cannot be deleted, not found.");
            }
        }
        //if i am here that means we have found the node
        //Case 1: if node to be deleted has no children
        if (current.getLeft() == null && current.getRight() == null) {
            if (current == root) {
                root = null;
            }
            if (isLeftChild) {
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }
        }
        //Case 2 : if node to be deleted has only one child
        else if (current.getRight() == null) {
            if (current == root) {
                root = current.getLeft();
            } else if (isLeftChild) {
                parent.setLeft(current.getLeft());
            } else {
                parent.setRight(current.getLeft());
            }
        } else if (current.getLeft() == null) {
            if (current == root) {
                root = current.getRight();
            } else if (isLeftChild) {
                parent.setLeft(current.getRight());
            } else {
                parent.setRight(current.getRight());
            }
        }

        //case 3 node has two children
        else if (current.getLeft() != null && current.getRight() != null) {
//now we have found the minimum element in the right sub tree
            Node<T> successor = getSuccessor(current);
            if (current == root) {
                root = successor;
            } else if (isLeftChild) {
                parent.setLeft(successor);
            } else {
                parent.setRight(successor);
            }
            successor.setLeft(current.getLeft());
        }


    }

    //returns the successor for the delete method
    public Node<T> getSuccessor(Node<T> deleleNode) {
        Node<T> successsor = null;
        Node<T> successsorParent = null;
        Node<T> current = deleleNode.getRight();
        while (current != null) {
            successsorParent = successsor;
            successsor = current;
            current = current.getLeft();
        }

        if (successsor != deleleNode.getRight()) {
            assert successsorParent != null;
            successsorParent.setLeft(successsor.getRight());
            successsor.setRight(deleleNode.getRight());
        }
        return successsor;
    }

    private void printHelper(Node<T> currPtr, String indent, boolean last) {
        // print the tree structure on the screen
        if (currPtr != null) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "     ";
            } else {
                System.out.print("L----");
                indent += "|    ";
            }

            System.out.println(currPtr.getData());

            printHelper(currPtr.getLeft(), indent, false);
            printHelper(currPtr.getRight(), indent, true);
        }
    }

    public void print() {
        this.printHelper(this.root, "  ", true);
    }

    public Node<T> getRoot() {
        return this.root;
    }
}

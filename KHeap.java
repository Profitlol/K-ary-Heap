package avltree;
import java.io.*;
import java.util.*;
/**
 *
 * @author Steven
 */
public class AVLTree {
    
    public Node root;
    List<String> input; // again using this for the output

    public class Node 
    {
        int key;
        int bf;
        Node left;
        Node right;
        Node parent;
        boolean heightInc;
        int height;

        public Node(int k, Node l, Node r) 
        {
            key = k;
            left = l;
            right = r;
            height = 0;
        }

        public Node(int num) 
        {
            this(num, null, null); // children, left, right 
        }
    }

    public AVLTree() 
    {
        root = null;
    }      

    public int height(Node n) 
    {
        if (n.left == null && n.right == null) 
            return 0;         
        else if (n.left == null) 
            return 1 + height(n.right);         
        else if (n.right == null) 
            return 1 + height(n.left);         
        else 
            return 1 + maximum(height(n.left), height(n.right));        
    }

    public int maximum(int x, int y) 
    {
        if (x >= y) 
            return x;         
        else 
            return y;        
    }

    public void getBalanceFactor(Node b) 
    {
        b.bf = height(b.left) - height(b.right);
    }

    public void insert(int key) 
    {
        root = insert(key, root);
    }

    // insert int key onto node z
    public Node insert(int key, Node t) 
    {
        if (t == null) 
            t = new Node(key, null, null);        
        else if (key < t.key) 
        {
            Node p = t;
            t.left = insert(key, t.left);
            t.left.parent = p;
            if (t.bf == 2) 
            {
                if (key < t.left.key) 
                    t = rightRotate(t);                
                else 
                    t = lrRotate(t);              
            }
        } 
        else if (key > t.key) 
        {
            Node p = t;
            t.right = insert(key, t.right);
            t.right.parent = p;
            if (t.bf == 2) 
            {
                if (key > t.right.key) 
                    t = leftRotate(t);                
                else 
                    t = rlRotate(t);          
            }
        } 
        else         
            t.height = maximum(height(t.left), height(t.right));        
        return t;
    }

    // moving node position to another node's position
    public void transplant(Node x, Node y) 
    {
        if (x.parent == null) 
            x = y;         
        else if (x.parent.left == x) 
            x.parent.left = y;        
        else 
            x.parent.right = y;        
        if (y != null) 
            y.parent = x.parent;        
    }

    public Node rightRotate(Node x) 
    {
        Node y = x.left;
        transplant(y, y.right);
        transplant(x, y);
        y.right = x;
        x.parent = y;
        return y;
    }

    public Node leftRotate(Node x) 
    {
        Node y = x.right;
        transplant(y, y.left);
        transplant(x, y);
        y.left = x;
        x.parent = y;
        return y;
    }

    public Node lrRotate(Node x) 
    {
        leftRotate(x.left);
        return rightRotate(x);
    }

    public Node rlRotate(Node x) 
    {
        rightRotate(x.right);
        return leftRotate(x);
    }
    
    // probably dont need
    public Node minValue(Node x) 
    {
        while (x.left != null) 
            x = x.left;        
        return x;
    }

    // probably dont need
    public Node maxValue(Node x) 
    {
        while (x.right != null) 
            x = x.right;        
        return x;
    }
    
    // the logic happens after searchKey completed
    // i.e. search for this key
    public boolean search(Node x, int k) 
    {
        if (x == null) 
            return false;       
        if (x.key == k) 
            return true;        
        if (x.key > k) 
            return search(x.left, k);         
        else 
            return search(x.right, k);        
    }
    
    public Node searchKey(Node x, int y)
    {
        if (y > x.key)
            return searchKey(x.right, y);
        if (y < x.key)
            return searchKey(x.left, y);
        if (y == x.key)
            return x;
        return null;
    }

    public Node successor(Node x) 
    {
        if (x.right != null) 
        {
            Node y = x.right;
            while (y.left != null) 
                y = y.left;            
            return y;
        } 
        else 
        {
            Node y = x.parent;
            while (y != null && x == y.right) 
            {
                x = y;
                y = y.parent;
            }
            return y;
        }
    }

    public Node predecessor(Node x) 
    {
        if (x.left != null) 
        {
            Node y = x.left;
            while (y.right != null) 
                y = y.right;            
            return y;
        }
        Node y = x.parent;
        while (y != null && x == y.left) 
        {
            x = y;
            y = y.parent;
        }
        return y;

    }

    public int rank(Node x, int k) 
    {
        if (x == null) 
            return 0;        
        if (k < x.key) 
            return rank(x.left, k);        
        if (k == x.key) 
            return size(x.left) + 1;        
        return size(x.left) + 1 + rank(x.right, k);
    }

    public int size(Node x) 
    {
        if (x == null)
            return 0;
        else
            return size(x.left) + size(x.right) + 1;        
    }

    public Node select(Node x, int i) 
    {
        if (x == null) 
            return null;        
        if (size(x.left) >= i) 
            return select(x.left, i);        
        if (size(x.left) + 1 == i)
            return x;        
        return select(x.right, i - 1 - (size(x.left)));
    }
    
    //doing same methods like KHeap
    public static void main(String[] args) 
    {		
		String filepath = "AVLtree-input.txt";
		AVLTree inp = new AVLTree();
		inp.loadfile(filepath);
		AVLTree avl = new AVLTree();
		double startTime = System.nanoTime();
		for (String s : inp.input) 
			inp.opRunner(s, avl);		
		double endTime = System.nanoTime();
		double elapsed = endTime - startTime;
		System.out.println(elapsed/1000.0 + "micro-sec");
	}

	public boolean loadfile(String path) 
        {
		input = new ArrayList<String>();
		Scanner filescn;
		int count = 0;
		try {
			filescn = new Scanner(new File("AVLtree-input.txt"));
			while (filescn.hasNextLine()) 
                        {
				String line = filescn.nextLine();
				input.add(line);
				count++;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
                // again debugging statements
		if (input.size() == count) {
			// System.out.println("File successfully loaded " + count );
			return true;
		} else
			// System.out.println("File load unsuccessfull. ");
			return false;
	}

        /// fix print outs here, debug comments are tehre
	public void opRunner(String s, AVLTree avl) 
        {
		String[] op = s.split(" ");	
		if (op[0].compareTo("IN") == 0) 
			avl.insert(Integer.parseInt(op[1]));
		if (op[0].compareTo("SC") == 0) 
                {
			int y = Integer.parseInt(op[1]);
                        Node suc = avl.successor(avl.searchKey(avl.root, y));
			System.out.println("Successor of: " + y + " = "+ suc.key);
		}
		if (op[0].compareTo("SE") == 0) 
                {
			int k = Integer.parseInt(op[1]);
			Node sel = avl.select(avl.root,k);
			System.out.println(k +"th smallest key is: " + sel.key);
		}
		if (op[0].compareTo("RA") == 0) 
                {
			int i = avl.rank(avl.root, Integer.parseInt(op[1]));
			System.out.println("Rank of " + op[1] + " = "+ i);
		}
	}
    
}

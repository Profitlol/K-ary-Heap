package kheap;
import java.util.*;
import java.io.*;
/**
 *
 * @author Steven
 */
public class KHeap 
{

    public Node[] heap;
    public int currentSize;
    public int k;
    List<String> input; // using this for output
    public int eCount = 0; // possible global counter for swtich cases

    public KHeap(int children) 
    {
        k = children;
        heap = new Node[100];
        currentSize = 0;
    }

    public KHeap()
    {
    }
    //getting Node x's n(th) child 
    public Node getChild(Node x, int n) 
    {
        if (n < k && n >= 0) 
            return heap[k * x.pos + n];         
        else 
            return null;        
    }

    public int parentPosition(Node x) 
    {
        return (int) Math.floor((x.pos - 1) / k);
    }

    // if arr is full. i'm just making it 2x bigger
    public void resize()  
    {
        heap = Arrays.copyOf(heap, (heap.length * 2));
    }

    public Node minChild(Node x) 
    {
        int min = x.key;
        int minPos = 0;
        // " i " will be the child's index
        for (int i = x.pos + 1; i <= (k * x.pos) + k; i++) 
        {
            if (heap[i] != null) 
            {
                if (heap[i].key < min) 
                {
                    min = heap[i].key;
                    minPos = i;
                }
            }
        }
        return heap[minPos];
    }

    public void insert(int v) 
    {
        
        if (size() == heap.length) 
            resize();        
        int hole = currentSize;
        Node x = new Node(v);
        // sifting up
        heap[hole] = x;
        x.pos = hole;
        x.parentPos = parentPosition(x); // that Math floor thing       
        while (x.key < heap[parentPosition(x)].key) 
            swap(x, heap[parentPosition(x)]);                
        currentSize++;
    }

    public void swap(Node a, Node b) 
    {
        int tempPos, tempPar;
        tempPos = b.pos;
        tempPar = b.parentPos;
        heap[b.pos] = a;
        heap[a.pos] = b;
        b.pos = a.pos;
        b.parentPos = a.parentPos;
        a.pos = tempPos;
        a.parentPos = tempPar;
    }

    public int size() 
    {
        return currentSize;
    }

    public void siftDown(int d) 
    {
        int temp = d;
        // while the minimum child's value is  < current key
        while (minChild(heap[d]).key < heap[d].key) 
        {
            temp = minChild(heap[temp]).pos;
            swap(heap[temp], minChild(heap[temp]));
        }
    }
    
    public Node extractMin() throws FileNotFoundException 
        {
            //long startTime = System.nanoTime();
            Node min = heap[0];
            heap[0] = heap[currentSize - 1];
            heap[0].pos = 0;
            heap[0].parentPos = 0;
            heap[currentSize - 1] = null;
            currentSize--;
            siftDown(0);
            //long endTime = System.nanoTime();
//            boolean DEBUG = true; // w hat?
//            if (DEBUG) {
//                System.out.println(min + " new min = " + heap[0]);
//            }
//            if (DEBUG) {
//                System.out.println("ExtractMin Time = " + ((endTime - startTime) / 1000));
//            }
            return min;
        } 

    public class Node 
    {
        int parentPos;
        int pos;
        int key;
        int val; // do i need this

        public Node(int index) 
        {
            key = index;
        }

        // this is needed to print the output. i'm not sure why, but i read it
        // on the internet
        @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		return sb.toString();
	}
    }

    // the micro-sec will vary from each run time. 
    // 2 = shittiest since u have to traverse a lot
    // at a certain point of K children it levels out & becomes 
    // relatively same speed. this is what you should notice
    public static void main(String[] args) throws FileNotFoundException 
    {
		String filepath = "karyHeap-input.txt";
		KHeap inp = new KHeap();
		inp.loadfile(filepath);
		for (int i = 2; i <= 10; i += 2) 
                {
			System.out.println("For k=" + i + " children");
			KHeap k = new KHeap(i);
			double start = System.nanoTime();
			for (String s : inp.input) 
				inp.opRunner(s, k);			
			double end = System.nanoTime();
			double elapsed = (end-start)/1000.0;
			System.out.println(elapsed + " micro-sec \n");
		}

	}
        // i will put all the lines of input into a giant ArrayList
        // then i will mess around with the elements in another function
	public boolean loadfile(String path) 
        {
		input = new ArrayList<String>();
		Scanner testfile;
		int count = 0;
		try {
			testfile = new Scanner(new File("karyHeap-input.txt"));
			while (testfile.hasNextLine()) 
                        {
				String line = testfile.nextLine();
				input.add(line);
				count++;
			}
			testfile.close();
		} catch (Exception e) {
			System.out.println(e);
		}
                //down here is just debugging statements
		if (input.size() == count) {
			// System.out.println("File loaded  " + count );
			return true;
		} else
			// System.out.println("SOMETHING'S WRONG LOL. ");
			return false;
	}

	// i noticed that  shit was getting crazy in the main function
        // i needed this so i can do my comparisions without worrying about
        // too many fucking brackets and parens
	public void opRunner(String s, KHeap k) throws FileNotFoundException {
		String[] op = s.split(" ");
		if (op[0].compareTo("IN") == 0) {
			k.insert(Integer.parseInt(op[1]));
		}
		if (op[0].compareTo("EX") == 0) {
			Node n = k.extractMin();
			System.out.println(n); 
                        // made a global eCount variable 
                        //for switch later
		}
	}

}

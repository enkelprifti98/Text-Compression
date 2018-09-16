import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Comparator;
import java.io.*;
import java.util.*;

//Node class used to create the huffman tree
class Node implements Comparator<Node> {
  int frequency;
  char c;

  Node left;
  Node right;

  //This function is used to compare frequencies between 2 nodes
  public int compare(Node c1, Node c2)
  {

      return c1.frequency - c2.frequency;
  }
}

//TextData class reads from a text file and creates ArrayLists containing the
//characters within the text along with their respective frequencies
class TextData {

	private String text;
	private int size;
	private String filepath;


  private ArrayList<Character> characters = new ArrayList<Character>();
  private ArrayList<Integer> frequencies = new ArrayList<Integer>();

  //Constructor
	public TextData(String filepath){
		this.filepath = filepath;
		ReadFromFile();
		CreateData();
	}

  //Functions that get data from the TextData object
	public ArrayList<Character> getCharacters(){
		return this.characters;
	}

	public ArrayList<Integer> getFrequencies(){
		return this.frequencies;
	}

	public int getSize(){
		return this.size;
	}

  public String getText(){
		return this.text;
	}

  //Functions that reads the text file into the text string
	public void ReadFromFile(){

			try{

				File file = new File(this.filepath);

  			BufferedReader br = new BufferedReader(new FileReader(file));

				this.text = "";
				String line = br.readLine();
  			while (line != null){
    			this.text += line + "\n";
					line = br.readLine();
				}

			}
			catch (Exception e) {
				System.out.println("you cant do that");
			}

  }


//Function that adds the characters and frequencies to the ArrayLists
	public void CreateData(){


		//Make a map of all the characters you want to track.
		String indexes = "1234567890AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrS sTtUuVv-.WwXxYyZz\",—!\n:`~@#$?%^&*()=+[]{}\\|/><_;'";

		//Initialize an array to the size of the possible matches.
		int[] count = new int[indexes.length()];

		//Loop through the sentence looking for matches.
		for (int i = 0; i < this.text.length(); i++) {
    	//This will get the index in the array, if it's a character we are tracking
    	int index = indexes.indexOf(this.text.charAt(i));

    	//If it's not a character we are tracking, indexOf returns -1, so skip those.
    	if (index < 0)
        	continue;

    	count[index]++;
		}

		double totalfrequencies = 0;

		for (int i = 0; i < count.length; i++) {
    	if (count[i] < 1)
        	continue;

			totalfrequencies += count[i];
			this.characters.add(indexes.charAt(i));
			this.frequencies.add(count[i]);

		}

		this.size = this.characters.size();

	}

}

//Main program
 class HuffmanProgram {

   static ArrayList<Character> HuffmanCharacters = new ArrayList<Character>();
   static ArrayList<String> HuffmanCodes = new ArrayList<String>();

    //Recursive function to print the huffman codes by traversing the tree
    public static void printHuffman(Node root, String code)
    {

        // base case is when the function reaches a leaf node
        if (root.left
                == null
            && root.right
                   == null
            ) {

            //Prints character
            System.out.println(root.c + ":" + code);
            HuffmanCharacters.add(root.c);
            HuffmanCodes.add(code);
            return;
        }


        //Recursive calls for left and right subtrees
        //Add 0 to the huffman code when going left
        printHuffman(root.left, code + "0");

        //Add 1 to the huffman code when going right
        printHuffman(root.right, code + "1");
    }


    //Function that compresses the text file with Huffman codes
    public static void compressFile(TextData data, ArrayList<Character> HuffmanCharacters, ArrayList<String> HuffmanCodes){
      try{
        File f = new File("compressedFile.txt");
        f.delete();
        f.createNewFile();
        PrintWriter writer = new PrintWriter("compressedFile.txt");

        //Replacing all text characters with matching huffman code one by one
        for (int i = 0; i < data.getText().length(); i++) {
          for (int j = 0; j < HuffmanCharacters.size(); j++){
            if(data.getText().charAt(i) == HuffmanCharacters.get(j)){
              writer.print(HuffmanCodes.get(j)+" ");
            }
          }
    		}

        writer.close();

      }
      catch (Exception IOException) {
      }
    }

    //Function that decompresses the compressed text file
    public static void decompressFile(TextData data, ArrayList<Character> HuffmanCharacters, ArrayList<String> HuffmanCodes){
      try{

        //Using selectionsort to sort characters and codes in increasing frequency
        //This is done because otherwise smaller length codes will replace parts of longer codes
        ArrayList<Character> copycharacters = HuffmanCharacters;
        ArrayList<String> copycodes = HuffmanCodes;
        ArrayList<String> sortedcharacters = new ArrayList<String>();
        ArrayList<String> sortedcodes = new ArrayList<String>();
    		for(int j = copycodes.size();j > 0;j--){
    			double maxamount = 0;
    			int maxindex = 0;
    			for(int i = 0;i<copycodes.size();i++){
    				if(maxamount < copycodes.get(i).length()){
    					maxamount = copycodes.get(i).length();
    					maxindex = i;
    				}
    			}
    			sortedcodes.add(copycodes.get(maxindex));
          sortedcharacters.add(Character.toString(copycharacters.get(maxindex)));

    			copycodes.remove(maxindex);
          copycharacters.remove(maxindex);

    		}

        String decompressedtext = data.getText();
        File f = new File("decompressedFile.txt");
        f.delete();
        f.createNewFile();
        PrintWriter writer = new PrintWriter("decompressedFile.txt");

        //Replacing all huffman codes with their matching characters
          for (int i = 0; i < sortedcodes.size(); i++){
            decompressedtext = decompressedtext.replaceAll(sortedcodes.get(i), sortedcharacters.get(i));
          }

        //These 3 lines clean up spacing
        decompressedtext = decompressedtext.replaceAll("  ", "_");
        decompressedtext = decompressedtext.replaceAll(" ", "");
        decompressedtext = decompressedtext.replaceAll("_", " ");

        writer.print(decompressedtext);
        writer.close();

        System.out.println("Sorted Characters by increasing frequency:");
        System.out.println(sortedcharacters);
        System.out.println("\n");
        System.out.println("Sorted Codes by increasing frequency:");
        System.out.println(sortedcodes);
        System.out.println("\n");

      }
      catch (Exception IOException) {
      }
    }

    // main function
    public static void main(String[] args)
    {

        Scanner s = new Scanner(System.in);
				String filepath = "text.txt";

        //Histogram letters object reads the text file and creates data for letters and frequencies
        TextData data = new TextData(filepath);

        int treesize = 0;

        // creating a priority queue
        // makes a min-priority queue(min-heap).
        PriorityQueue<Node> queue
            = new PriorityQueue<Node>(data.getSize(), new Node());


        //creating new nodes with data and adding them to the priority-queue.
        for (int i = 0; i < data.getSize(); i++) {

            Node newNode = new Node();

            newNode.c = data.getCharacters().get(i);
            newNode.frequency = data.getFrequencies().get(i);

            newNode.left = null;
            newNode.right = null;


            queue.add(newNode);
            treesize++;
        }

        // create a root node
        Node root = null;

        // Get the two minimum values from the heap each time until its size reduces to 1
        while (queue.size() > 1) {


            Node min1 = queue.peek();
            queue.poll();

            Node min2 = queue.peek();
            queue.poll();

            //Creating new node which is the sum of the frequencies of the two minimum nodes
            Node sum = new Node();

            sum.frequency = min1.frequency + min2.frequency;
            sum.c = '-';

            //Setting the two minimum nodes as the children of the sum node
            sum.left = min1;
            sum.right = min2;

            //Making the sum node the root node and adding it to the queue
            root = sum;
            queue.add(sum);
            treesize++;
        }

        // print the codes by traversing the tree
        printHuffman(root, "");


        System.out.println("Treesize is: " + treesize);
        System.out.println("\n");
        System.out.println(HuffmanCharacters);
        System.out.println("\n");
        System.out.println(HuffmanCodes);
        System.out.println("\n");


        compressFile(data, HuffmanCharacters, HuffmanCodes);



        TextData compressedtext = new TextData("compressedFile.txt");

        //size in bits. A bit is either 1 or 0
        int compressedsize = 0;

        //Checking for 1s and 0s in the compressed text and adding them to the size counter
        for (int i = 0; i < compressedtext.getText().length(); i++) {
          if(compressedtext.getText().charAt(i) == '1' || compressedtext.getText().charAt(i) == '0'){
            compressedsize++;
          }
        }


        decompressFile(compressedtext, HuffmanCharacters, HuffmanCodes);

        File original = new File(filepath);
        System.out.println("Original   file size is " + original.length()*8 + " bits or " + original.length() + " bytes.");
        System.out.println("Compressed file size is " + compressedsize + " bits or " + compressedsize/8 + " bytes.");
        System.out.println("\n");


    }
}

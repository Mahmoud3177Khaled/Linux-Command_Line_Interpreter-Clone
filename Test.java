public static void main(String[] args) {
  System.out.println("hello world!");
}

try {
  fileToCopy.createNewFile();

  FileReader inputFile = new FileReader(OgfileToCopy);
  BufferedReader inputFilebf = new BufferedReader(inputFile);
  System.out.println(inputFilebf.readLine());
  FileWriter outputFile = new FileWriter(fileToCopy);

  String line;

  while ((line = inputFilebf.readLine()) != null) { 
      outputFile.write(line);
  }

  inputFile.close();
  outputFile.close();

} catch (IOException ex) {
  System.out.println("Error: Failed to create copied file.");
}

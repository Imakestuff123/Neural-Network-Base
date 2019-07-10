# Neural-Network-Base

This is a simple base for the creation of neural networks. It is meant to be used as a template to help create your own neural networks. I made this in blueJ (java), but anyone with a java compiler should be able to edit it to their own needs. I will most likely continue to update this base, adding more functions such as a gui to display progress, more customizability, and more things that'll make the program easier to use. I have also attached a simple sorting neural network as an example on how to modify the program. 

## Instructions
Open the bluej project file using blueJ, or compile the java classes using a different java IDE.

### Prerequisites
- Any Java IDE that can use .java files

## Authors

Fletcher Li

## License

Mit License

## Acknowledgements
Michael Taylor - for writing the book that I used to learn about, and create this program.

## How to modify the program to fit your needs
You run the program by running the main function in the Draw class.
Go to the Driver class. Every setting should be in the function setNeuralNetworkSettings().

LayerStats (int[])
  - this array determines the number of layers and the number of nodes in each of those layers. 
  - The first digit in the array shows how many nodes are in the input layer
  - After the first digit, all the digits that are not the last two digits in the array correspond to how many hidden lkayerss there are, and how many nodes are in each of those layers
  - The second to lsat digit in the array determines how many nodes are in the output layer
  - The Last digit determines whether bias nodes are enabvled or not, with a 1 or a 0 
  - new int[]{# of nodes in input layer, # of nods in hidden layer1, # of nods in hidden layer2... # of nodes in output layer, determines whether bias nodes exist }
  
LearningRate (double)
  - Determines the magnitude of change when Weights update themselves
  
GradientCehcking (bool)
 -If this is set to true, it will print out the relative error of weights as they update themselves

StepBased (bool)
  - Determines whether the Neural Network will learn and update in real time

FrameSpeed (double)
  - If StepBased is set to true, this will determine how many miliseconds are between each change in frame

NumberofTests (int)
  - Determines how many tests will run before the program loads up

TestingArray (new double[][]) 
  - Where you put in the combination of inputs and outputs that will train your neural network
  - For each unique test you desire ot run, make one double array listening the inputs for that run, and make another array that lists the desired outputs which correspond with such a run.
  - For example, if I wanted one test to have the inputs of 1 for InputNode1 and 0.3 for InputNode2, and the output of 0.6 for OutputNode1, and output of 0.8 for OutputNode2, I would write TestingArray = new double[][] {new double[] {1, 0.3}, new double[] {0.6, 0.8} }

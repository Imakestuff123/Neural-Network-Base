public class Equations
{   
   public static double round(double precision, double number) {
       //precision = 1000d
       return ((double)Math.round(number * precision) / precision);
   }
   public static double lengthdir_x(double xx, double direction) {
       return (Math.cos(direction) * xx);
   }
   public static double lengthdir_y(double yy, double direction) {
       return (Math.sin(direction) * yy);
   }
   public static double CalculateDistance(
      double x1, 
      double y1, 
      double x2, 
      double y2) {       
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
   }
   public static double CalculateAngle(int APositionX, int APositionY, int BPositionX, int BPositionY)
   {
       int dx = BPositionX - APositionY;
       int dy = BPositionY - APositionY;

       return Math.atan2(dy, dx);
   }
   public static double SummationOperator(Node NodeSelected) {
       // netinput = bias value + sigma(n on top, i = 1) XiWi    
       double total = 0;
       for (int i = 0; i <= NodeSelected.InputWeights.size() - 1; i++) {
           Object CurrentWeight = NodeSelected.InputWeights.get(i);
           total += ((Weight)CurrentWeight).getValue() * ((Weight)CurrentWeight).getParent().getValue();
       }
       //total /= NodeSelected.InputWeights.size();
       //System.out.println(total);
       return total;
   }
   public static double SigmoidActivationFunction(double xInput) {
       //f(x) = 1 / (1 + e^(-x))
       return (1 / (1 + Math.pow(Math.E, -xInput)));
   }
   public static double CalculateLocalError(Node OutputNode) {
       // (t - z) ^ 2
       double actualValue = OutputNode.getValue();
       return Math.pow((OutputNode.TargetOutput - actualValue), 2);
   }
   public static double CalculateOutputLayerWeightGradient(Weight CurrentWeight) {
        //(z - t) z(1 - z) parent.output t = target output for target node
        double z = CurrentWeight.getTarget().getValue();
        double out = CurrentWeight.getParent().getValue();
        return ((z - CurrentWeight.getTarget().TargetOutput) * z * (1 - z) * out);
   }
   public static double DeltaZ(Node OutputNode) {
       double t = OutputNode.TargetOutput;
       double z = OutputNode.getValue();
       return ((z - t) * z * (1 - z));
   }
   public static double SigmaDeltaZWeights(Weight Weighti) {
     // First part of Hidden layer weight gradient calculation 
     // do recursiveliy
     double total = 0;
     if (Weighti.getTarget().getType() != 2) {
         for (int i = 0; i <= Weighti.getTarget().OutputWeights.size() - 1; i++) {
             Weight CurrentWeight = (Weight)Weighti.getTarget().OutputWeights.get(i);
             total += SigmaDeltaZWeights(CurrentWeight);
         }
     } else {
         total += DeltaZ(Weighti.getTarget()) * Weighti.getValue();
     }
     return total;
   }
   public static double OutputPartialDerivative(Weight Weighti) {
       //Second part of hidden layer weight gradient calculation
       double Parentout = Weighti.getParent().getValue();
       double Targetout = Weighti.getTarget().getValue();
       return Targetout * (1 - Targetout) * Parentout;
   }
   public static double CalculateHiddenLayerWeightGradient(Weight CurrentWeight) {
       return SigmaDeltaZWeights(CurrentWeight) * OutputPartialDerivative(CurrentWeight); 
   }
   public static double OutputPartialDerivativeBias(Weight Weighti) {
       double Targetout = Weighti.getTarget().getValue();
       return Targetout * (1 - Targetout);
   }
   public static double CalculateHiddenLayerBiasWeightGradient(Weight BiasWeight) {
       return SigmaDeltaZWeights(BiasWeight) * OutputPartialDerivativeBias(BiasWeight);
   }
   
   public static double GradientChecking(Weight Wi, Node[] TotalNodeCountArray) {
       //(Error(Theta + Epsilon) - Error(Theta + Epsilon)) / (2 * Epsilon)
       double Epsilon = 0.0001;
       double newWi = Wi.getValue() + Epsilon;
       double newWi2 = Wi.getValue() - Epsilon;
       //Recalculate forward propagation Total `network error
       double temptotal = 0;
       double[] NodeValues = new double[TotalNodeCountArray.length];
       //calculating nodes of first Equation
       for (int i = 0; i <= TotalNodeCountArray.length - 1; i++) {
           Node CurrentNode = TotalNodeCountArray[i];
           if ((CurrentNode.InputWeights != null)) {
               temptotal = 0;
               //gets sum opperator of all input weights
               for (int ii = 0; ii <= CurrentNode.InputWeights.size() - 1; ii++) {
                   Weight CurrentWeight = (Weight)CurrentNode.InputWeights.get(ii);
                   double WeightValue;
                   if (CurrentWeight == Wi) {
                       WeightValue = newWi;
                   }
                   else WeightValue = CurrentWeight.getValue();
                   temptotal += WeightValue * (CurrentWeight.getParent().getValue());
               }
               NodeValues[i] = SigmoidActivationFunction(temptotal);
           }
       }
       /*for (int i = 0; i <= NodeValues.length - 1; i++) {
           System.out.println(" " + TotalNodeCountArray[i].getNodeCode());
           System.out.println(NodeValues[i]);
       }*/
       
       temptotal = 0;
       //calculating total error in network for first equation
       for (int i = 0; i <= TotalNodeCountArray.length - 1; i++) {
           Node CurrentNode = TotalNodeCountArray[i];
           if (CurrentNode.getType() == 2) {
               double actualValue = NodeValues[i];
               //System.out.println();
               //System.out.println(CurrentNode.getValue());
               //System.out.println(actualValue);
               temptotal += Math.pow((CurrentNode.TargetOutput - actualValue), 2);
           }
       }
       double FirstError = temptotal / 2;
       temptotal = 0;
       NodeValues = new double[TotalNodeCountArray.length];
       for (int i = 0; i <= TotalNodeCountArray.length - 1; i++) {
           Node CurrentNode = TotalNodeCountArray[i];
           if ((CurrentNode.InputWeights != null)) {
               temptotal = 0;
               //gets sum opperator of all input weights
               for (int ii = 0; ii <= CurrentNode.InputWeights.size() - 1; ii++) {
                   Weight CurrentWeight = (Weight)CurrentNode.InputWeights.get(ii);
                   double WeightValue;
                   if (CurrentWeight == Wi) {
                       WeightValue = newWi2;
                   }
                   else WeightValue = CurrentWeight.getValue();
                   temptotal += WeightValue * (CurrentWeight.getParent().getValue());
               }
               NodeValues[i] = SigmoidActivationFunction(temptotal);
           }
       }
       temptotal = 0;
       for (int i = 0; i <= TotalNodeCountArray.length - 1; i++) {
           Node CurrentNode = TotalNodeCountArray[i];
           if (CurrentNode.getType() == 2) {
               double actualValue = NodeValues[i];
               temptotal += Math.pow((CurrentNode.TargetOutput - actualValue), 2);
           }
       }
       
       double SecondError = temptotal / 2;
       double FinalNumericalApproximation = (FirstError - SecondError) / (2 * Epsilon);
       //System.out.println(FinalNumericalApproximation);
       
       double Difference = Wi.CurrentGradient - FinalNumericalApproximation;
       double RelativeError = (FirstError - SecondError) / Math.max(FirstError, SecondError);
       //System.out.println(Difference);
       
       return RelativeError;    
   }
   public static void UpdateGeneralWeightFormula(Weight Weight, double LearningRate) {
       Weight.setValue(Weight.getValue() - (LearningRate * Weight.CurrentGradient));
   }
 
}

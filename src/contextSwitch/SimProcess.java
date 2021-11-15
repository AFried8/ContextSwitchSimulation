package contextSwitch;
import java.util.Random;

public class SimProcess {
	
	int pid;           
	String procName;
	int totalInstructions;
	
	public SimProcess (int pid, String procName, int totalInstructions) {
		this.pid = pid; 
		this.procName = procName;
		this.totalInstructions = totalInstructions;
	}
	
	public ProcessState execute (int i) {
		
		Random rand = new Random();
		//generate a random number from 0-99
		int probability = rand.nextInt(100); 
		
		System.out.println("pid: " + pid + " Process Name: " + procName + " Current Instruction: " + i);
		
		if((i) >= totalInstructions) {			//If the current instruction is equal to the total instruction
			return ProcessState.FINISHED;
		}
		else if (probability < 15){				//If the random number turned out to be less than 15
			return ProcessState.BLOCKED;
		}
		else {									//f the random number turned out to be more than 15
			return ProcessState.READY;
		}
	}

}
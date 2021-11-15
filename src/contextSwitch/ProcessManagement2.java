/*
 * Aliza Fried
 * Operating Systems
 * OS Module 3 Assignment
 * This program is meant to simulate a processor running
 * The processor object executes instructions of the process its running or switches to different processes
 */


package contextSwitch;

import java.util.ArrayList;
import java.util.Random;

public class ProcessManagement2 {

	public static void main(String[] args) {
		
		
		final int QUANTAM = 5;											//Max number of ticks a process may take up on the processor
		SimProcessor processor = new SimProcessor();					
		
		ArrayList<PCB> readyProcesses = new ArrayList<>();				
		ArrayList<PCB> blockedProcesses = new ArrayList<>();			
		
		//Fill the readyProcesses array. All processes start off on the ready list, so the loop instantiates a new PCB+process each
		//time it loops. The process ID, name, and instruction number are passed to the PCB constructor, and
		//the PCB object then creates the Process object with this information. The PCB then references the process
		//it just created.
		for(int i=1; i<11; i++) {
			readyProcesses.add(new PCB(i*1000, "process" + i, i+100));
		}
		
		int ticks = 0;											//Keeps track of how many ticks a process took on the processor
		ProcessState state = ProcessState.READY;				//Set the state of the current process to ready
		processor.setProcess(readyProcesses.get(0).process);	//Set processor to start
		
		for(int i = 1; i<301; i++) {
			System.out.print("Step " + i + ": ");				//Says which step the loop is up to
			
			switch (state) {									//determines which code to execute according to the current process' state				
			
			case READY:
				if(ticks<QUANTAM) {												//If the number of ticks didn't reach the max yet
					if(readyProcesses.size()==0) {								//If there are no processes to execute
						System.out.println("**Processor Idling**");				
					}
					else {														//If there is a processor to execute
					state = processor.executeNextInstruction();					
					ticks++;		
					}
				}
				else {															//If the number of ticks maxed out
					System.out.println("**Quantam Expired**");							
					saveDataToPCB(processor, readyProcesses);		
					readyProcesses.add(readyProcesses.get(0));
					processor = loadNextProcess(processor, readyProcesses);															
					ticks=0;													//reset ticks to zero
					state = ProcessState.READY;									//reset processState to ready
				}
				break;
			
			case BLOCKED:
				System.out.println("**Process Blocked**");
				saveDataToPCB(processor, readyProcesses);				
				blockedProcesses.add(readyProcesses.get(0));				//Add now blocked process to blocked list
				processor = loadNextProcess(processor, readyProcesses);
				ticks = 0;
				state = ProcessState.READY;
				break;
				
			case FINISHED:
				System.out.println("**Process Completed**");
				saveDataToPCB(processor, readyProcesses);
				processor = loadNextProcess(processor, readyProcesses);
				ticks = 0;
				state = ProcessState.READY;
				break;
				
			}
			//re-awake some processes on the blocked list
			updateBlockedList(blockedProcesses, readyProcesses);
		}
				

	}
	
	/**
	 * saveDataToPCB saves the data from the current process to its PCB
	 * @param processor - has the values of the current process that need to be saved
	 * @param readyProcesses - ArrayList that stores the pcbs
	 */
	public static void saveDataToPCB(SimProcessor processor, ArrayList<PCB> readyProcesses) {
		readyProcesses.get(0).setCurrInstruction(processor.currInstruction);  	//Save currInstruction
		readyProcesses.get(0).setAllRegs(processor.getAllRegs());				//Save registers
		System.out.println("\tSaving "+ readyProcesses.get(0));
	}
	
	/**
	 * loadNextProcess clears the current process on the processor and loads the information from the next process
	 * @param processor
	 * @param readyProcesses - list of ready processes that represents the current process and the next one
	 * @return - the processor with the information for the next process
	 */
	public static SimProcessor loadNextProcess(SimProcessor processor, ArrayList<PCB> readyProcesses) {
		readyProcesses.remove(0);												//Remove  the old process from the top of the ready list
		if(readyProcesses.size()==0) {											//If the ready list is empty
			System.out.println("**Processor Idling**");				
		}
		else {																		//If the ready list is not empty
		processor.setProcess(readyProcesses.get(0).process);
		processor.setCurrInstruction(readyProcesses.get(0).getCurrInstruction());   //Loading currInstruction
		processor.setAllRegs(readyProcesses.get(0).getAllRegs());					//Loading registers
		System.out.println("\tRestoring " + readyProcesses.get(0));
		}
		return processor;
	}

	/**
	 * updatedBlockedList method loops through the blocked list and randomly re-awakes around 30% of the processes
	 * @param blockedList
	 * @param readyList
	 */
	public static void updateBlockedList(ArrayList<PCB> blockedList, ArrayList<PCB> readyList) {
		Random rand = new Random();
		for(int i = 0; i<blockedList.size(); i++) {
			int probability = rand.nextInt(100);
			if(probability < 30) {
				readyList.add(blockedList.get(i)); 
				blockedList.remove(i);
			}
		}
	}

}

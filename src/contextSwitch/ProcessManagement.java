/*
 * OS Module 3 Assignment
 * This program is meant to simulate a processor running
 * The processor object executes instructions of the process its running or switches to a different process
 */


package contextSwitch;

import java.util.ArrayList;
import java.util.Random;

public class ProcessManagement {

	public static void main(String[] args) {
		
		
		final int QUANTAM = 5;											//Max number of ticks a process may take up on the processor
		SimProcessor processor = new SimProcessor();					//Processor Object
		
		ArrayList<PCB> readyProcesses = new ArrayList<>();				//ArrayList of PCBs to store ready processes
		ArrayList<PCB> blockedProcesses = new ArrayList<>();			//ArrayList of PCBS to store blocked processes
		
		//Fill the readyProcesses array. All processes start off on the ready list, so the loop instantiates a new PCB/process each
		//time it loops. The process ID, name, and instruction number are passed to the PCB constructor, and
		//the PCB object then creates the Process object with this information. The PCB then references the process
		//it just created.
		for(int i=0; i<10; i++) {
			readyProcesses.add(new PCB(i*1000, "process" + i, i+100));
		}
		
		int ticks = 0;											//Keeps track of how many ticks a process took on the processor
		ProcessState state = ProcessState.READY;				//Set the state of the current process to ready
		processor.setProcess(readyProcesses.get(0).process);	//Set processor to start
		
		for(int i = 1; i<301; i++) {
			System.out.print("Step " + i + ": ");				//Says which step the loop is up to
			
			switch (state) {									//determines which code to execute according to the current process's state				
			
			case READY:
				if(ticks<QUANTAM) {												//If the number of ticks didn't reach the max yet
					if(readyProcesses.size()==0) {								//If the ready process list is empty
						System.out.println("**Processor Idling**");				//Inform user
					}
					else {														//If number of ticks didn't max out yet
					//processor.setProcess(readyProcesses.get(0).process);		//Set the process on the processor to the next ready process
					state = processor.executeNextInstruction();					
					ticks++;		
					}
				}
				else {
					System.out.println("**Quantam Expired**");							//If the number of ticks maxed out
					
					//Starting context switch
					//Saving current info to PCB
					//Set the currInstruction and reg values in PCB of process that just ran from the currInstruction and regs on the processor
					readyProcesses.get(0).setCurrInstruction(processor.currInstruction); 
					readyProcesses.get(0).setAllRegs(processor.getAllRegs());
					readyProcesses.add(readyProcesses.get(0));					//Add this expired process to end of ready list
					System.out.println("\tSaving "+ readyProcesses.get(0));		//Tell User that saving
					readyProcesses.remove(0);									//Remove  the processor from the top of the ready list
					if(readyProcesses.size()==0) {								//If the ready list is empty
						System.out.println("**Processor Idling**");				//Tell user that processor idling
					}
					else {														//If the ready list is not empty
					//Continue context switch
					//Loading next process from readyList onto processor
					processor.setProcess(readyProcesses.get(0).process);
					processor.setCurrInstruction(readyProcesses.get(0).getCurrInstruction());   //Loading currInstruction
					processor.setAllRegs(readyProcesses.get(0).getAllRegs());					//Loading registers
					System.out.println("\tRestoring " + readyProcesses.get(0));
					}
					ticks=0;													//reset ticks to zero
					state = ProcessState.READY;									//reset processState to ready
				}
				break;
			
			case BLOCKED:
				System.out.println("**Process Blocked**");
				//Saving processor values
				readyProcesses.get(0).setCurrInstruction(processor.currInstruction);
				readyProcesses.get(0).setAllRegs(processor.getAllRegs());
				blockedProcesses.add(readyProcesses.get(0));				//Add now blocked process to blocked list
				System.out.println("\tSaving "+ readyProcesses.get(0));
				readyProcesses.remove(0);
				if(readyProcesses.size()==0) {
					System.out.println("**Processor Idling**");
				}
				else {
				processor.setProcess(readyProcesses.get(0).process);
				processor.setCurrInstruction(readyProcesses.get(0).getCurrInstruction());
				processor.setAllRegs(readyProcesses.get(0).getAllRegs());
				System.out.println("\tRestoring " + readyProcesses.get(0));
				}
				ticks = 0;
				state = ProcessState.READY;
				break;
				
			case FINISHED:
				System.out.println("**Process Completed**");
				readyProcesses.get(0).setCurrInstruction(processor.currInstruction);
				readyProcesses.get(0).setAllRegs(processor.getAllRegs());
				System.out.println("\tSaving "+ readyProcesses.get(0));
				readyProcesses.remove(0);
				if(readyProcesses.size()==0) {
					System.out.println("**Processor Idling**");
				}
				else {
				processor.setProcess(readyProcesses.get(0).process);
				processor.setCurrInstruction(readyProcesses.get(0).getCurrInstruction());
				processor.setAllRegs(readyProcesses.get(0).getAllRegs());
				System.out.println("\tRestoring " + readyProcesses.get(0));
				}
				ticks = 0;
				state = ProcessState.READY;
				break;
				
			}
			//re-awake some processes on the blocked list
			updateBlockedList(blockedProcesses, readyProcesses);
		}
				

	}
	
	public static PCB saveDataToPCB(PCB pcb, SimProcessor processor, ArrayList<PCB> readyProcesses) {
		return pcb;
	}
	
	//method to awaken some processes on the blocked list
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

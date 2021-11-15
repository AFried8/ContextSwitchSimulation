package contextSwitch;

import java.util.ArrayList;
import java.util.Random;

public class Processing {

	public static void main(String[] args) {
		
		final int QUANTAM = 5;
		SimProcessor processor = new SimProcessor();
		ArrayList<PCB> readyProcesses = new ArrayList<PCB>();
		ArrayList<PCB> blockedProcesses = new ArrayList<PCB>();
		int ticks = 0;
		ProcessState state;
		
		ArrayList<SimProcess> processes = new ArrayList<SimProcess>();
		for(int i = 1; i < 11; i++) {
			processes.add(new SimProcess(i * 1000, "process" + i, i+100));
		}
		
		ArrayList<PCB> pcbs = new ArrayList<PCB>();
		for(int i = 1; i < 11; i++) {
			pcbs.add(new PCB(processes.get(i-1)));
		}
		
		for(int i=0; i<10; i++) {
			readyProcesses.add(pcbs.get(i));
		}
		PCB current = readyProcesses.get(0);
		
		processor.setProcess(readyProcesses.get(0).getProcess());
		state = ProcessState.READY;
		
		System.out.println(processes);
		System.out.println("Saving "+ readyProcesses.get(0) + "\n\t\tCurrInstruction=" + readyProcesses.get(0).currInstruction + "RegisterValues=" + readyProcesses.get(0).registers);
		
		for(int i = 1; i < 3001; i++) {
						
			/*if(readyProcesses.size()==0) {
				System.out.println("**Processor Idling**");
				updateBlockedList(blockedProcesses, readyProcesses);
			}*/
			
			switch (state) {
			case BLOCKED: 
				System.out.println("**Process Blocked**");
				current = contextSwitchUpdatePCB(current, processor, readyProcesses, i);
				processor = uploadNextPCB(readyProcesses.get(0), processor, i);
				blockedProcesses.add(current);
				state = ProcessState.READY;
				ticks = 0;
				i++;
				break;
			
			
			case FINISHED: 
				System.out.println("**" + processor.process.procName + " finished**");
				current = contextSwitchUpdatePCB(current, processor, readyProcesses, i);
				processor = uploadNextPCB(readyProcesses.get(0), processor, i);
				state = ProcessState.READY;
				ticks = 0;
				i++;
				break;
			
			
			case READY: 
				if(ticks < QUANTAM) {
					System.out.print("Step " + (i) + ": ");
					state = processor.executeNextInstruction();
					ticks++;
				}
				else {
					System.out.println("**Quantam expired**");
					ticks = 0;
					readyProcesses.add(current);
					current = contextSwitchUpdatePCB(current, processor, readyProcesses, i);
					processor = uploadNextPCB(readyProcesses.get(0), processor, i);
					i++;
				}
			break;
			}
			updateBlockedList(blockedProcesses, readyProcesses);
		}		
		
	}
	
	public static PCB contextSwitchUpdatePCB(PCB current, SimProcessor processor, ArrayList<PCB> readyProcesses, int stepNumber) {
		current = readyProcesses.get(0);
		current = updatePCB(current, processor);
		readyProcesses.remove(0);
		if(current.currInstruction < current.process.totalInstructions) {
		contextSwitchMessage(processor, stepNumber, "saving"); }
		return current;
	}
	
	public static PCB updatePCB(PCB pcb, SimProcessor processor) {
		for (int i = 0; i <=3; i++) {
		pcb.setReg(i, processor.getReg(i));
		}
		pcb.currInstruction = processor.getCurrInstruction();
		return pcb;
	}
	
	public static SimProcessor uploadNextPCB(PCB pcb, SimProcessor processor, int stepNumber) {
		processor.setProcess(pcb.getProcess());
		processor.currInstruction = pcb.currInstruction;
		for (int i = 0; i <=3; i++) {
			processor.setReg(i, pcb.getReg(i));
		}
		contextSwitchMessage(processor, (stepNumber+1), "restoring");
		return processor;
	}
	
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
	
	public static void contextSwitchMessage(SimProcessor processor, int stepNumber, String action) {
		StringBuilder message = new StringBuilder();
		message.append("Step " + stepNumber + ": ");
		message.append("Context Switch: "+ action + ' ' + processor.process.procName);
		message.append("\n\t\t Current Instruction: " + processor.currInstruction);
		message.append("\t-R1: " + processor.getReg(0) + "\t-R2: " + processor.getReg(1) + "\t-R3: " + processor.getReg(2) + "\t-R4: " + processor.getReg(3));
		System.out.println(message);
	}

}

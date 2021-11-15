package contextSwitch;

public class PCB {
	
	SimProcess process;
	int currInstruction;
	int[] registers = new int[4];
	
	// Takes a process and sets the process field to it
	public PCB (SimProcess process) {
		this.process = process;	
	}
	
	//Takes data to create a process object, creates a new process object, 
	//and then sets the process field to the new process object
	public PCB(int pid, String procName, int totalInstructions) {
		this.process = new SimProcess (pid, procName, totalInstructions);
	}
	
	public SimProcess getProcess () {
		return process;
	}

	public int getCurrInstruction() {
		return currInstruction;
	}

	public void setCurrInstruction(int currInstruction) {
		this.currInstruction = currInstruction;
	}
	
	public int getReg(int i) {
		return registers[i];
	}
	
	public void setReg(int i, int value) {
		registers[i] = value;
	}
	
	public int[] getAllRegs() {
		return registers;
	}
	
	public void setAllRegs(int[] regs) {
		for(int i = 0; i < 4; i++) {
			registers[i] = regs[i];
		}
	}

}

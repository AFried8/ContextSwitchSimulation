package contextSwitch;
import java.util.Random;

public class SimProcessor {
	
	SimProcess process;
	int[] registers = new int[4];
	int currInstruction;
	
	public SimProcess getProcess() {
		return process;
	}
	
	public void setProcess(SimProcess process) {
		this.process = process;
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
	
	
	public ProcessState executeNextInstruction() {
		Random rand = new Random();
		ProcessState state = process.execute(currInstruction);
		currInstruction++;
		//Set registers to random numbers
		for(int i = 0; i <=3; i++) {
			setReg(i, rand.nextInt(100));
		}
		return state;
	}

}

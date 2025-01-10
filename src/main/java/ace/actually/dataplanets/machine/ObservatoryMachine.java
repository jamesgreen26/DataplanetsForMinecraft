package ace.actually.dataplanets.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;

public class ObservatoryMachine extends WorkableElectricMultiblockMachine {
    public ObservatoryMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        if(!isRemote())
        {
            System.out.println("FORMED!");
        }
    }
}

/** Copyright (c) 2011-2015, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt */
package buildcraft.api.transport.pluggable;

import buildcraft.api.gates.IGateProvider;
import buildcraft.api.statements.StatementSlot;
import buildcraft.api.transport.pipe.IPipe;
import net.minecraft.core.Direction;

import java.util.Iterator;
import java.util.List;

public class ActionIterator implements Iterable<StatementSlot> {
    private final IPipe pipe;

    public ActionIterator(IPipe iPipe) {
        pipe = iPipe;
    }

    @Override
    public Iterator<StatementSlot> iterator() {
        return new It();
    }

    private class It implements Iterator<StatementSlot> {

        private Direction curDir = Direction.values()[0];
        private int index = 0;

        @Override
        public boolean hasNext() {
            return getNext(false) != null;
        }

        @Override
        public StatementSlot next() {
            return getNext(true);
        }

        private StatementSlot getNext(boolean advance) {
            Direction curDir = this.curDir;
            int index = this.index;
            while (true) {
                // List<StatementSlot> lst = pipe.hasGate(curDir) ? pipe.getGate(curDir).getActiveActions() : null;
                List<StatementSlot> lst = pipe.getHolder().getPluggable(curDir) instanceof IGateProvider ? ((IGateProvider) pipe.getHolder().getPluggable(curDir)).getGate().getActiveActions() : null;
                if (lst == null || index >= lst.size()) {
                    if (curDir.ordinal() == 5) {
                        return null;
                    }
                    curDir = Direction.values()[curDir.ordinal() + 1];
                } else {
                    index++;
                    if (advance) {
                        this.curDir = curDir;
                        this.index = index;
                    }
                    return lst.get(index - 1);
                }
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove not supported.");
        }
    }
}

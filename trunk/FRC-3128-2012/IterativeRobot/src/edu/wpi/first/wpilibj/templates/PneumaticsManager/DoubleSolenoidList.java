package edu.wpi.first.wpilibj.templates.PneumaticsManager;

class DoubleSolenoidList {

    private DoubleSolenoid[] itemList;
    private int nextIndex = 0;
    private double expansionFactor = .35;

    DoubleSolenoidList(int sizeOf) {
        itemList = new DoubleSolenoid[sizeOf];
    }

    DoubleSolenoidList() {
        itemList = new DoubleSolenoid[10];
    }

    public int length() {
        return itemList.length;
    }

    public void changeExpansionFactor(double ex) {
        expansionFactor = ex;
    }

    public boolean isNull(int index) {
        return (itemList[index] == null);
    }

    public int nextPointer() {
        return nextIndex;
    }

    public synchronized void expandToSize(int newSize) throws Exception {
        if (newSize < itemList.length) {
            throw new Exception("Illegal size!");
        }
        DoubleSolenoid[] tempArray = new DoubleSolenoid[newSize];
        for (int i = 0; i < itemList.length; i++) {
            tempArray[i] = itemList[i];
        }
        itemList = tempArray;
    }

    public void add(DoubleSolenoid e) throws Exception {
        try {
            itemList[nextIndex] = e;
            nextIndex++;
        } catch (ArrayIndexOutOfBoundsException ex) {
            this.expandToSize((int) Math.ceil(itemList.length + expansionFactor * itemList.length));
            this.add(e);
        }
    }

    public DoubleSolenoid getIndex(int index) throws Exception {
        if (index > itemList.length) {
            throw new Exception("Illegal size!");
        }
        return itemList[index];
    }

    public DoubleSolenoid getEventTemplate(DoubleSolenoid e) throws Exception {
        for (int i = 0; i < nextIndex; i++) {
            if (itemList[i] == e) {
                return itemList[i];
            }
        }
        throw new Exception("Unidentifiable Object!");
    }

    public int getIndexOf(DoubleSolenoid e) throws Exception {
        for (int i = 0; i < nextIndex; i++) {
            if (itemList[i] == e) {
                return i;
            }
        }
        return 0;
    }

    public void removeIndex(int index) throws Exception {
        if (index >= nextIndex) {
            throw new Exception("Illegal size! Array is " + itemList.length + ".");
        }
        itemList[index] = null;
        for (int i = index + 1; i < nextIndex; i++) {
            itemList[i - 1] = itemList[i];
            itemList[i] = null;
        }
        nextIndex--;
    }

    public void removeEventTemplate(DoubleSolenoid e) throws Exception {
        for (int i = 0; i < nextIndex; i++) {
            if (itemList[i] == e) {
                removeIndex(i);
            }
        }
    }

    public boolean existsEventTemplate(DoubleSolenoid e) throws Exception {
        for (int i = 0; i < nextIndex; i++) {
            if (itemList[i] == e) {
                return true;
            }
        }
        return false;
    }
}
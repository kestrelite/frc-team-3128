package edu.wpi.first.wpilibj.templates.EventManager;

class ThreadList {

    private Thread[] itemList;
    private int nextIndex = 0;
    private double expansionFactor = .35;

    ThreadList(int sizeOf) {
        itemList = new Thread[sizeOf];
    }

    ThreadList() {
        itemList = new Thread[10];
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
        Thread[] tempArray = new Thread[newSize];
        for (int i = 0; i < itemList.length; i++) {
            tempArray[i] = itemList[i];
        }
        itemList = tempArray;
    }

    public void add(Thread e) throws Exception {
        try {
            itemList[nextIndex] = e;
            nextIndex++;
        } catch (ArrayIndexOutOfBoundsException ex) {
            this.expandToSize((int) Math.ceil(itemList.length + expansionFactor * itemList.length));
            this.add(e);
        }
    }

    public Thread getIndex(int index) throws Exception {
        if (index > itemList.length) {
            throw new Exception("Illegal size!");
        }
        return itemList[index];
    }

    public Thread getEventTemplate(Thread e) throws Exception {
        for (int i = 0; i < nextIndex; i++) {
            if (itemList[i] == e) {
                return itemList[i];
            }
        }
        throw new Exception("Unidentifiable Object!");
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

    public void removeEventTemplate(Thread e) throws Exception {
        for (int i = 0; i < nextIndex; i++) {
            if (itemList[i] == e) {
                removeIndex(i);
            }
        }
    }

    public boolean existsEventTemplate(Thread e) throws Exception {
        for (int i = 0; i < nextIndex; i++) {
            if (itemList[i] == e) {
                return true;
            }
        }
        return false;
    }
}
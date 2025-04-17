import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyMaxHeap<E extends Comparable<? super E>> implements Iterable<E> {
    private static final int DEFAULT_CAPACITY = 15;
    private int currentSize; // Number of elements in the heap.
    private E[] array; // The heap array where all the elements are stored.
    public MyMaxHeap( ) { this( DEFAULT_CAPACITY ); }
    public MyMaxHeap( int capacity ) {
        currentSize = 0;
        array = (E[])(new Comparable[ capacity + 1 ]);
    }

    /**
     * This constructor accepts an array of elements as argument and
     * makes sure they satisfy the heap order property after this initialisation.
     * @param items is the array consisting of the items to be placed in the heap.
     */
    public MyMaxHeap( E[] items ) {
        currentSize = items.length;
        array = (E[]) new Comparable[ ( currentSize + 2 ) * 11 / 10 ];

        int i = 1;
        for( E item : items ) {
            array[i] = item;
            i++;
        }
        buildHeap( );
    }

    public void insert(E x) {
        if (currentSize == array.length - 1)
            enlargeArray(array.length * 2 + 1);

        // Percolate up algorithm
        int hole = ++currentSize;
        for (array[0] = x; x.compareTo( array[hole / 2]) > 0; hole /= 2)
            array[hole] = array[hole / 2];
        array[hole] = x;
    }

    private void enlargeArray( int newSize ) {
        E[] old = array;
        array = (E[]) new Comparable[ newSize ];
        for( int i = 0; i < old.length; i++ )
            array[ i ] = old[ i ];
    }
    public E findMax( ) {
        if( isEmpty( ) )
            return null;
        return array[ 1 ];
    }
    public E deleteMax( ) {
        if( isEmpty( ) )
            return null;

        E maxItem = findMax( );
        array[ 1 ] = array[ currentSize ];
        currentSize--;
        percolateDown( 1 );

        return maxItem;
    }

    /**
     * Makes the heap satisfy the heap order property.
     */
    private void buildHeap( ) {
        for( int i = currentSize / 2; i > 0; i-- )
            percolateDown( i );
    }
    public boolean isEmpty( ) {
        return currentSize == 0;
    }

    /**
     * This method changes the location of the corresponding element until it satisfies the heap order property.
     * @param hole is the hole to be percolated down.
     */
    private void percolateDown(int hole) {
        int child;
        E tmp = array[hole];

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            if (child != currentSize &&
                    array[child + 1].compareTo(array[child]) > 0)
                child++;
            if (array[child].compareTo(tmp) > 0)
                array[hole] = array[child];
            else
                break;
        }
        array[hole] = tmp;
    }
    public int getSize() { return this.currentSize; }

    @Override
    public Iterator<E> iterator() {
        return new HeapIterator();
    }

    /**
     * A heap iterator class is defined in order to traverse the elements of the heap.
     */
    private class HeapIterator implements Iterator<E> {
        private int current = 1;

        @Override
        public boolean hasNext() {
            return current <= currentSize;
        }
        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E nextElement = array[current];
            current++;
            return nextElement;
        }
    }
}
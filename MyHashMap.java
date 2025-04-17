import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Hash map using open hashing as collision resolution strategy.
 * @param <K> the key of the entries.
 * @param <V> the value of the entries.
 */
public class MyHashMap<K, V> implements Iterable<V>{

    // A nested entry class is defined in order to store key-value pairs in the hash map.
    private static class Entry<K, V> {
        K key;
        V value;

        // Each entry stores the next entry as if it is an element of a linked list.
        Entry<K, V> next;
        Entry(K key, V value){
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    private Entry<K, V>[] table; // The array that is storing the entries.
    private int capacity; // The capacity of the hash table.
    private int size; // Number of entries in the hash map.
    private final float loadFactorThreshold; // Threshold of load factor. When it is exceeded, the hash map is rehashed.

    public MyHashMap(int initialCapacity, float loadFactor){
        this.capacity = initialCapacity;
        this.loadFactorThreshold = loadFactor;
        this.size = 0;
        table = (Entry<K, V>[])(new Entry[capacity]);
    }
    public MyHashMap(){
        this(17, 0.75f);
    }

    /**
     * This method hashes the given entry's key into a number.
     * @param key is the corresponding entry's key.
     * @return an integer representing at which to place the element.
     */
    private int hash(K key){
        if (key == null) {
            throw new IllegalArgumentException("Null key.");
        }
        // The original hash value is brought into the range of the hash table.
        int originalHash = key.hashCode();
        int hash = originalHash % capacity;
        if (hash < 0) {
            hash += capacity;
        }
        return hash;
    }
    public void put(K key, V value){
        int index = hash(key);
        Entry<K, V> currentEntry = table[index];

        // The linked list is traversed until the entry with the corresponding key is found,
        // if it exists, its value is updated.
        while (currentEntry != null) {
            if (currentEntry.key.equals(key)) {
                currentEntry.value = value;
                return;
            }
            currentEntry = currentEntry.next;
        }
        
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;

        // If load factor exceeds the threshold, hash map is rehashed.
        if((float)size / capacity >= loadFactorThreshold){
            rehash();
        }
    }
    public V get(K key){
        int index = hash(key);
        Entry<K, V> currentEntry = table[index];

        // The linked list is traversed until the entry with the corresponding key is found, then its value is returned.
        while (currentEntry != null) {
            if (currentEntry.key.equals(key)) {
                return currentEntry.value;
            }
            currentEntry = currentEntry.next;
        }
        return null; // If no entry with the corresponding key is found.
    }
    public void remove(K key){
        int index = hash(key);
        Entry<K, V> currentEntry = table[index];
        Entry<K, V> previousEntry = null;

        // The linked list is traversed until the entry with the corresponding key is found.
        while (currentEntry != null) {
            if (currentEntry.key.equals(key)) {
                // If it is the first element of the list, the second element becomes the first one.
                if (previousEntry == null) {
                    table[index] = currentEntry.next;
                }
                // It is removed from the list by connecting the previous entry with the next one.
                else {
                    previousEntry.next = currentEntry.next;
                }
                size--;
                return;
            }
            previousEntry = currentEntry;
            currentEntry = currentEntry.next;
        }
    }
    public boolean containsKey(K key) {
        int index = hash(key);
        Entry<K, V> currentEntry = table[index];
        
        while (currentEntry != null) {
            if (currentEntry.key.equals(key)) {
                return true;
            }
            currentEntry = currentEntry.next;
        }
        return false;
    }
    private void rehash(){
        int oldCapacity = capacity;
        // New capacity is chosen as the next prime number greater than twice of the old capacity.
        capacity = nextPrime(2 * oldCapacity);
        Entry<K, V>[] oldTable = table;

        // Create a new table with this updated capacity.
        table = (Entry<K, V>[])(new Entry[capacity]);
        size = 0; // Reset the size of the table and then re-insert every entry to recount.

        // Re-insert all entries into the new table.
        for(int i = 0; i < oldCapacity; i++) {
            Entry<K, V> currentEntry = oldTable[i];
            while (currentEntry != null) {
                put(currentEntry.key, currentEntry.value);
                currentEntry = currentEntry.next;
                size++;
            }
        }
    }
    public static boolean isPrime(int number) {
        if (number <= 1)
            return false;
        if (number == 2)
            return true;
        if (number % 2 == 0)
            return false;
        int sqrt = (int) Math.sqrt(number);
        for (int i = 3; i <= sqrt; i += 2) {
            if (number % i == 0)
                return false;
        }
        return true;
    }
    public static int nextPrime(int number) {
        while (!isPrime(number)) {
            number++;
        }
        return number;
    }
    @Override
    public Iterator<V> iterator() {
        return new HashMapIterator();
    }

    /**
     * A hash map iterator is defined in order to traverse through the hash map.
     */
    private class HashMapIterator implements Iterator<V> {
        private int currentListIndex; // The index of the current linked list.
        private Entry<K, V> currentEntry; // Current entry in the linked list.
        public HashMapIterator() {
            this.currentListIndex = 0;
            this.currentEntry = null;
            nextNonEmptyList();
        }

        /**
         * Advances to the next linked list which is not empty.
         */
        private void nextNonEmptyList() {
            while (currentListIndex < capacity) {
                if (table[currentListIndex] != null) {
                    currentEntry = table[currentListIndex];
                    break;
                }
                currentListIndex++;
            }
            if (currentListIndex >= capacity) {
                currentEntry = null;
            }
        }

        @Override
        public boolean hasNext() {
            if (currentEntry != null) {
                return true;
            }
            return false;
        }

        @Override
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements.");
            }

            V value = currentEntry.value;

            if (currentEntry.next != null) {
                currentEntry = currentEntry.next;
            } else {
                currentListIndex++;
                currentEntry = null;
                nextNonEmptyList();
            }
            return value;
        }
    }
}

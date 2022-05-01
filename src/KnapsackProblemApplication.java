import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class KnapsackProblemApplication {

    public static final int NUMBER_OF_ITEMS = 100000;
    public static final int SIZE_OF_KNAPSACK = 1;
    public static final int MAXIMUM_ITEM_WEIGHT = 100;
    // This item value will be divided by 100 to create 2 decimal places
    public static final int MAXIMUM_ITEM_VALUE = 10000;
    public static final boolean BENCHMARK_CONSOLE_OUTPUT_ENABLED = true;

    public static void main(String... args){

        // Sorted map will order the list during population rather than having to sort an entire list from scratch.
        // Doesn't reduce complexity
        SortedMap<BigDecimal, SortedMap<Integer, Item>> orderedItems = generateAndSortRandomObjects();
        displayItems(orderedItems);

        // Passing in the map of items and getting back the ideal knapsack contents
        List<Item> knapsack = generateOptimizedKnapsackContents(orderedItems);

        displayContents(knapsack);

    }

    /**
     * This method implements the optimization algorithm to determine what items to add to the knapsack
     *
     * @param orderedObjects sorted list of available objects
     * @return list of items added to the knapsack
     */
    private static List<Item> generateOptimizedKnapsackContents(SortedMap<BigDecimal, SortedMap<Integer, Item>> orderedObjects) {

        // Start time to benchmark how long this method takes
        long startTime = System.currentTimeMillis();

        List<Item> knapsack = new ArrayList<>();
        int remainingWeight = SIZE_OF_KNAPSACK;

        for(BigDecimal key : orderedObjects.keySet()){
            SortedMap<Integer, Item> items = orderedObjects.get(key);
            for(Integer itemKey : items.keySet()) {

                Item item = items.get(itemKey);
                if (item.getObjectWeight() <= remainingWeight) {
                    knapsack.add(item);
                    remainingWeight = remainingWeight - item.getObjectWeight();
                }

            }
            if (remainingWeight == 0) break;
        }

        if (BENCHMARK_CONSOLE_OUTPUT_ENABLED) {
            // Calculating execution time and outputting to console
            long endTime = System.currentTimeMillis();
            System.out.println("\nTotal execution time: " + (endTime - startTime) + "ms\n");
        }

        return knapsack;
    }

    /**
     * This is where we generate the random items and presort them into the map to be sent to the
     * optimization algorithm
     *
     * @return sorted map of all items
     */
    private static SortedMap<BigDecimal,SortedMap<Integer, Item>> generateAndSortRandomObjects() {

        SortedMap<BigDecimal, SortedMap<Integer, Item>> orderedItems = new TreeMap<>(Comparator.reverseOrder());
        for(int count = 0; count < NUMBER_OF_ITEMS; count++){
            // Generate a new item
            Item item= generateRandomItem();
            BigDecimal priceRatio = item.getObjectValue().divide(new BigDecimal(item.getObjectWeight()), 2, RoundingMode.CEILING);
            SortedMap<Integer, Item> listOfItems = orderedItems.get(priceRatio);
            if (orderedItems.get(priceRatio) == null){
                listOfItems = new TreeMap<>();
            }
            // Add the item into the sorted list with the key being the value per weight
            listOfItems.put(item.getObjectWeight(), item);
            orderedItems.put(priceRatio, listOfItems);
        }

        return orderedItems;
    }

    /**
     * This method will generate a random item with a maximum weight and value set by the final variables
     * at the top of this class.  Weight has +1 added to it so we never encounter a zero-weight item
     *
     * @return generated item
     */
    private static Item generateRandomItem() {
        Random seed = new Random();
        return new Item(seed.nextInt(MAXIMUM_ITEM_WEIGHT) + 1,
                new BigDecimal(BigInteger
                        .valueOf(new Random().nextInt(MAXIMUM_ITEM_VALUE)))
                        .divide(new BigDecimal(100)));
    }

    /**
     * Utility method for displaying the available items in the knapsack in the console log
     *
     * @param orderedItems
     */
    private static void displayItems(SortedMap<BigDecimal, SortedMap<Integer, Item>> orderedItems) {
        System.out.println("Available items for knapsack:");
        System.out.println("-----------------------------");
        for(BigDecimal key : orderedItems.keySet()){
            SortedMap<Integer, Item> items = orderedItems.get(key);
            for(Integer itemKey : items.keySet()) {
                Item item = items.get(itemKey);
                System.out.println("Weight: " + item.getObjectWeight() + " Value: " + item.getObjectValue() + " Ratio:" + key);
            }
        }
    }

    /**
     * Utility method for displaying the final assortment of items that have been added to the knapsack
     *
     * @param knapsack
     */
    private static void displayContents(List<Item> knapsack) {
        System.out.println("\n\nOptimized items in knapsack:");
        System.out.println("----------------------------");
        int totalWeight = 0;
        BigDecimal totalValue = new BigDecimal(0);
        for(Item item : knapsack){
            System.out.println("Weight: "+item.getObjectWeight()+" Value: "+item.getObjectValue());
            totalWeight = totalWeight + item.getObjectWeight();
            totalValue = totalValue.add(item.getObjectValue());
        }
        System.out.println("Total weight: " + totalWeight);
        System.out.println("Total value: " + totalValue);
    }
}

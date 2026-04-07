/**
 * what is an Array?
 * An array is a special type of object used to store multiple values in a single variable. 
 * Each value (also called an element) has a numeric index, starting from 0. 
 * Arrays in JavaScript are dynamic, meaning they can grow or shrink in size and can hold values of different data types.
 */

// 1. Creating an Array
const fruits = ["Apple", "Banana", "Cherry"];
console.log("Initial Array:", fruits);

// 2. Accessing Elements
console.log("First Fruit:", fruits[0]); // Output: Apple
console.log("Array Length:", fruits.length); // Output: 3

// 3. Adding/Removing Elements (End of array)
fruits.push("Orange"); // Adds to the end
console.log("After Push:", fruits); 

let lastFruit = fruits.pop(); // Removes from the end
console.log("Popped Fruit:", lastFruit);
console.log("After Pop:", fruits);

// 4. Adding/Removing Elements (Beginning of array)
fruits.unshift("Strawberry"); // Adds to the beginning
console.log("After Unshift:", fruits);

let firstFruit = fruits.shift(); // Removes from the beginning
console.log("Shifted Fruit:", firstFruit);
console.log("After Shift:", fruits);

// 5. Finding Index of an element
let index = fruits.indexOf("Banana");
console.log("Index of Banana:", index); // Output: 1

// 6. Splice - Adding/Removing at specific positions
// Syntax: splice(start, deleteCount, item1, item2, ...)
fruits.splice(1, 1, "Grapes", "Mango"); // Removes 1 element at index 1 and adds Grapes and Mango
console.log("After Splice:", fruits);

// 7. Slice - Creating a shallow copy of a portion
const citrus = fruits.slice(1, 3); // Elements from index 1 to 2
console.log("Sliced Array:", citrus);

// 8. Iterating over Arrays
console.log("\n--- Iterating using forEach ---");
fruits.forEach((fruit, idx) => {
    console.log(`${idx}: ${fruit}`);
});

//merging two arrays
const tropicalFruits = ["Pineapple", "Papaya"];
const allFruits = fruits.concat(tropicalFruits);
console.log("Merged Array:", allFruits);

// 9. Checking if an element exists
console.log("Does the array include 'Mango'?", fruits.includes("Mango")); // Output: true

// 10. Sorting an Array
fruits.sort();
console.log("Sorted Array:", fruits);

// 11. Reversing an Array
fruits.reverse();
console.log("Reversed Array:", fruits);

// 12. Joining Array elements into a string
const fruitString = fruits.join(", ");
console.log("Joined String:", fruitString);

// 13. Converting a string back to an array
const newFruitsArray = fruitString.split(", ");
console.log("New Array from String:", newFruitsArray);

//merging two arrays using spread operator
const moreFruits = ["Kiwi", "Peach"];
const combinedFruits = [...fruits, ...moreFruits];
console.log("Combined Array using Spread Operator:", combinedFruits);

//map function to create a new array with the lengths of each fruit name
const fruitLengths = fruits.map(fruit => fruit.length);
console.log("Array of Fruit Name Lengths:", fruitLengths);

//filter function to create a new array with fruits that have more than 5 characters
const longFruits = fruits.filter(fruit => fruit.length > 5);
console.log("Fruits with more than 5 characters:", longFruits);
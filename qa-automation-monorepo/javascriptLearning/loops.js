/**
 * Loops in JavaScript
 * 1. for loop: repeats a block of code a specified number of times
 * 2. while loop: repeats a block of code while a specified condition is true
 * 3. do...while loop: similar to while, but executes the block once before checking the condition
 * 4. for...in loop: iterates over the properties of an object
 * 5. for...of loop: iterates over the values of an iterable object (like an array)
 * 6. forEach: array method that executes a function once for each array element
 */

// 1. For Loop
console.log("--- For Loop ---");
for (let i = 0; i < 5; i++) {
    console.log("Iteration:", i);
}

// 2. While Loop
console.log("\n--- While Loop ---");
let count = 0;
while (count < 3) {
    console.log("Count is:", count);
    count++;
}

// 3. Do...While Loop
console.log("\n--- Do...While Loop ---");
let j = 0;
do {
    console.log("Value of j:", j);
    j++;
} while (j < 2);

// 4. For...In Loop (Used for Objects)
console.log("\n--- For...In Loop ---");
const person = { fname: "John", lname: "Doe", age: 25 };
for (let key in person) {
    console.log(key + ": " + person[key]);
}

// 5. For...Of Loop (Used for Arrays/Iterables)
console.log("\n--- For...Of Loop ---");
const colors = ["Red", "Green", "Blue"];
for (let color of colors) {
    console.log("Color:", color);
}

// 6. forEach (Array Method)
console.log("\n--- forEach ---");
const fruits = ["Apple", "Banana", "Mango"];
fruits.forEach((fruit, index) => {
    console.log(index + ": " + fruit);
});

// Break and Continue
console.log("\n--- Break and Continue ---");
for (let i = 1; i <= 5; i++) {
    if (i === 2) {
        continue; // Skip the rest of this iteration
    }
    if (i === 4) {
        break; // Exit the loop entirely
    }
    console.log("Number:", i);
}

/**
 * A Promise is an object representing the eventual completion or failure of an asynchronous operation.
 * It can be in one of three states:
 * 1. Pending: Initial state, neither fulfilled nor rejected.
 * 2. Fulfilled: The operation completed successfully.
 * 3. Rejected: The operation failed.
 */

function checkInventory(item) {
    return new Promise((resolve, reject) => {
        console.log(`Checking inventory for ${item}...`);
        
        setTimeout(() => {
            const stock = {
                "laptop": 5,
                "phone": 0
            };

            if (stock[item] > 0) {
                resolve(`${item} is available in stock.`);
            } else {
                reject(`${item} is out of stock.`);
            }
        }, 2000);
    });
}

// Consuming the Promise using .then() and .catch()
checkInventory("laptop")
    .then((message) => {
        console.log("Success:", message);
    })
    .catch((error) => {
        console.error("Error:", error);
    })
    .finally(() => {
        console.log("Inventory check completed.");
    });

// Consuming the Promise using Async/Await (Modern Syntax)
async function processOrder(item) {
    try {
        console.log("\n--- Starting Async/Await Process ---");
        const result = await checkInventory(item);
        console.log("Result:", result);
    } catch (error) {
        console.error("Caught Error:", error);
    }
}

processOrder("phone");

/**
 * Promise.all() example:
 * Useful when you want to wait for multiple asynchronous operations to finish.
 */
const p1 = Promise.resolve("Task 1 Complete");
const p2 = new Promise((resolve) => setTimeout(() => resolve("Task 2 Complete"), 1000));
const p3 = Promise.resolve("Task 3 Complete");

Promise.all([p1, p2, p3]).then((values) => {
    console.log("\nAll promises resolved:", values);
});

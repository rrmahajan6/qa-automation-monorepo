//callback function is a function that is passed as an argument to another function and is executed after some operation has been completed.
function performOperation(num1, num2, operation) {
    return operation(num1, num2);
}

function add(a, b) {
    return a + b;
}

function subtract(a, b) {
    return a - b;
}

console.log("Addition of 10 and 5 is:", performOperation(10, 5, add));
console.log("Subtraction of 10 and 5 is:", performOperation(10, 5, subtract));

//javascript is asynchronous, which means that it can execute multiple operations at the same time without blocking the main thread. Callback functions are often used in asynchronous programming to handle the results of asynchronous operations, such as fetching data from an API or reading a file. When the asynchronous operation is complete, the callback function is called with the result of the operation, allowing you to handle the result without blocking the main thread.
function fetchData(callback) {
    setTimeout(() => {
        const data = { id: 1, name: "John Doe" };
        callback(data);
    }, 2000); // Simulating an asynchronous operation with a delay of 2 seconds
}

fetchData((result) => {
    console.log("Data fetched:", result);
});
console.log("Program continues...");
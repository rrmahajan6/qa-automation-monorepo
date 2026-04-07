/**
 * function is set of statements that performs a task or calculates a value. To use a function, you must define it somewhere in the scope from which you wish to call it.
 * two stages of using a function: function definition and function invocation.
 * function definition: a function definition consists of the function keyword, followed by:
 * 1. The name of the function.
 * 2. A list of parameters to the function, enclosed in parentheses and separated by commas.
 * 3. The JavaScript statements that define the function, enclosed in curly brackets, { }.
 * to invoke a function, you can use the function name followed by parentheses. If the function takes parameters, you can pass them inside the parentheses.
 * Types of functions:
 * Named functions: A named function is defined with a name and can be called by that name.
 * Anonymous functions: An anonymous function is a function without a name. They are often used as arguments to other functions or assigned to variables.
 * Arrow functions: Arrow functions provide a shorter syntax for writing functions and do not have their own `this` context.
 */

function addTwoNumbers(num1, num2){
    let sum = num1 + num2;
    return sum;
}

let sum = addTwoNumbers(5, 10);
console.log("Sum of 5 and 10 is:", sum);
//parameters are the names listed in the function definition, while arguments are the real values passed to the function when it is invoked. In the above example, num1 and num2 are parameters, while 5 and 10 are arguments.
//any type of data can be passed as an argument to a function, including numbers, strings, arrays, objects, and even other functions. Functions can also return values using the return statement. If a function does not have a return statement, it will return undefined by default.
// we can provide any number of arguments to a function, no check is done on the number of arguments passed to a function. If you pass more arguments than the function expects, the extra arguments will be ignored. If you pass fewer arguments than the function expects, the missing parameters will be set to undefined.

// Example of an anonymous function assigned to a variable
const sumOfNumbers = function(num1, num2){
    let sum = num1 + num2;
    return sum;
}
console.log("Sum of 7 and 3 is:", sumOfNumbers(7, 3));

//passing default parameters to a function
function greet(name = "Guest") {
    return "Hello, " + name + "!";
}
console.log(greet()); // Output: Hello, Guest!
console.log(greet("Alice")); // Output: Hello, Alice!

function fullName(firstname, middlename, lastname){
    if(middlename){
        return firstname + " " + middlename + " " + lastname;
    }else {
        return firstname + " " + lastname;
    }
}
console.log(fullName("John", "Doe")); // Output: John Doe
console.log(fullName("John", "Michael", "Doe")); // Output: John Michael Doe
console.log(fullName())
console.log(fullName("John")) // Output: John undefined

//passing an object as an argument to a function
function displayPersonInfo(person) {
    return "Name: " + person.name + ", Age: " + person.age;
}
const person1 = { name: "Alice", age: 30 };
console.log(displayPersonInfo(person1)); // Output: Name: Alice, Age: 30

//arguments object: The arguments object is an array-like object that contains the values of the arguments passed to a function. It is available within all non-arrow functions and can be used to access the arguments passed to the function, regardless of the number of parameters defined in the function.
function sumAllNumbers(){
    console.log("Arguments object:", arguments);
    console.log(arguments.length); // Number of arguments passed
    console.log(arguments[3]); // Fourth argument
}

sumAllNumbers(1, 2, 3, 4, 5, 6, 7);

//self invoking function: A self-invoking function is a function that is executed immediately after it is defined. It is also known as an Immediately Invoked Function Expression (IIFE). The syntax for a self-invoking function is as follows:
(function add(num1, num2) {
    let sum = num1 + num2;
    console.log(`Sum of ${num1} and ${num2} is:`, sum);
    console.log("This is a self-invoking function!");
})(2,4);

// Arrow function example
function multiply(num1, num2) {
    return num1 * num2;
}
//converted to arrow function
//()=>{} is the syntax for an arrow function. If the function has only one parameter, you can omit the parentheses around the parameter. If the function body contains only a single expression, you can omit the curly braces and the return keyword, and the expression will be returned automatically.
const multiplyArrow = (num1, num2) => {
    return num1 * num2;
}
console.log("Product of 5 and 6 is:", multiplyArrow(5, 6));

//no argument arrow function
const greetArrow = () => {
    return "Hello, World!";
}
console.log(greetArrow()); // Output: Hello, World!

//single argument arrow function
const square = num => num * num;
console.log("Square of 4 is:", square(4)); // Output: Square of 4 is: 16

//multiple arguments arrow function
const add = (a, b) => a + b;
console.log("Sum of 3 and 7 is:", add(3, 7)); // Output: Sum of 3 and 7 is: 10
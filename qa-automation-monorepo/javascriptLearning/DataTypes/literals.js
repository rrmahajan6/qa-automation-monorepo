/**
 * Data types in Javascript
 * 1. Primitive data types: string, number, boolean, null, undefined, symbol
 * 2. Non-primitive data types: object (including arrays and functions)
 */
let string = "Hello, World!";
let string2 = 'Hello, JavaScript!';
let string3 = `Hello, ${string}!`;
let number = 42;
let boolean = true;
let nullValue = null;
let undefinedValue;
let symbol = Symbol("symbol");

let object = {string: string, number: number, boolean: boolean};
let array = [string, number, boolean];
let function1 = function() {console.log("This is a function");};

console.log(string);
console.log(string2);
console.log(string3);
console.log(number);
console.log(boolean);
console.log(nullValue);
console.log(undefinedValue);
console.log(symbol);
console.log(object);
console.log(array);
console.log(function1);
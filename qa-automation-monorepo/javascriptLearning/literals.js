/**
 * Data types in Javascript
 * 1. Primitive data types: string, number, boolean, null, undefined, symbol
 * 2. Non-primitive data types: object (including arrays and functions)
 */
"use strict"; // enable strict mode to catch common coding mistakes and unsafe actions  
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
console.log("-----------------------------");
console.log(typeof string); // string
console.log(typeof number); // number
console.log(typeof boolean); // boolean
console.log(typeof nullValue); // object (this is a known bug in JavaScript)
console.log(typeof undefinedValue); // undefined
console.log(typeof symbol); // symbol
console.log(typeof object); // object
console.log(typeof array); // object (arrays are a type of object)
console.log(typeof function1); // function
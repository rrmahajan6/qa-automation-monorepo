/**
 * Operators in JavaScript
 * 1. Arithmetic Operators: +, -, *, /, %, **, ++, --
 * 2. Assignment Operators: =, +=, -=, *=, /=, %=, **=
 * 3. Comparison Operators: ==, ===, !=, !==, >, <, >=, <=
 * 4. Logical Operators: &&, ||, !
 * 5. Ternary Operator: condition ? exprIfTrue : exprIfFalse
 */
// 1. Arithmetic Operators
let x = 10;
let y = 3;
console.log("Addition:", x + y);        // 13
console.log("Subtraction:", x - y);     // 7
console.log("Multiplication:", x * y);  // 30
console.log("Division:", x / y);        // 3.333...
console.log("Remainder (Modulus):", x % y); // 1
console.log("Exponentiation:", x ** y); // 1000 (10^3)

// 2. Assignment Operators
let z = 5;
z += 2; // z = z + 2
console.log("Assignment (+=):", z); // 7

// 3. Comparison Operators
console.log("Equal to (value):", 5 == "5");       // true
console.log("Strict Equal (value & type):", 5 === "5"); // false
console.log("Not Equal:", 5 != 6);                // true
console.log("Greater than:", 10 > 5);             // true

// 4. Logical Operators
let isAdult = true;
let hasLicense = false;
console.log("Logical AND (&&):", isAdult && hasLicense); // false
console.log("Logical OR (||):", isAdult || hasLicense);  // true
console.log("Logical NOT (!):", !isAdult);               // false

// 5. Ternary Operator
let age = 20;
let status = (age >= 18) ? "Adult" : "Minor";
console.log("Ternary Result:", status); // Adult

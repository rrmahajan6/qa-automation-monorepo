/**
 * variables rules
 * variable name should start with letter, _ or $
 * variable name should not be a reserved keyword
 * variable name should not contain space
 * variable name should be meaningful
 * variable name should be in camelCase
 * multiple variables can be declared in a single line separated by comma
 * variable can be declared without initialization
 * variable can be initialized at the time of declaration or later
 * variable can be re-assigned a new value (except const)
 * when you declare a variable using var, it is function scoped and can be re-declared and updated
 * when you declare a variable using let, it is block scoped and can be updated but not re-declared
 * when you declare a variable using const, it is block scoped and cannot be updated or re-declared
 * when you just decare a variable without initialization, it is assigned the value undefined
 */
var a = 10;
let b = 20;
const c = 30;
//const variable must be initialized at the time of declaration
//const d; //SyntaxError: Missing initializer in const declaration
//use capital letters for constant values

console.log(a);
console.log(b);
console.log(c);

/**
 * variable scoping rules
 * variables declared with var are function scoped
 * variables declared with let and const are block scoped
 */
let counter = 0;
if (true) {
    let counter = 1; // this counter is different from the outer counter
    console.log("inside the if block",counter); // 1
}
console.log("outside the if block",counter); // 0

var var_counter = 0;
if (true) {
    var var_counter = 1;
    console.log("inside the if block",var_counter); // 1
}
console.log("outside the if block",var_counter); // 1


